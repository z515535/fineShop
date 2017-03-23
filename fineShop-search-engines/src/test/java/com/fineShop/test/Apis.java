package com.fineShop.test;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



/**
* @author 作者 wugf:
* @version 创建时间：2017年3月4日 下午4:02:22<p>
* 类说明
* 
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/spring/spring-*.xml")
public class Apis {
	
	@Resource
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Resource
	private Client client;
	
	@Test
	public void test(){
		try {  
            ActionFuture<ClusterHealthResponse> health = client.admin().cluster().health(new ClusterHealthRequest());  
            ClusterHealthStatus status = health.actionGet().getStatus();  
            if (status.value() == ClusterHealthStatus.RED.value()) {  
                throw new RuntimeException("elasticsearch cluster health status is red.");  
            }  
            System.out.println(status.value());
        } catch (Exception e) {  
        }  
	}
	
	@Test
	public void put(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 36);
		map.put("nickname", "put2");
		map.put("password", "123456");
		
		IndexResponse response =client.prepareIndex("library", "user", "36").setSource(map).get();
		//这里只能判断是否创建，如果es已存在该doc 会更新改doc和返回false
		System.out.println(response.isCreated());
	}
	
	@Test
	public void get(){
		Long start = System.currentTimeMillis();
		GetResponse response = client.prepareGet("library", "user", "1")
				.setOperationThreaded(false).get();	//线程安全
		
		// 返回map (相对耗时)
		Map<String, Object> result = response.getSource();
		System.out.println(result);
		
		// 返回 json (相对省时)
		String json = response.getSourceAsString();
		System.out.println(json);
		
		System.out.println(System.currentTimeMillis() - start);
	}
	
	@Test
	public void delete(){
		DeleteResponse response = client.prepareDelete("library", "user", "3").get();
		System.out.println(response.isFound());		//是否删除成功
	}
	
	@Test
	public void update(){
		
		// 官方推荐使用 jsonBuilder 去构建josn数据
		/*UpdateRequest updateRequest = new UpdateRequest("library", "user", "35")
		        .doc(XContentFactory.jsonBuilder()
		            .startObject()
		                .field("nickname", "jsonBuilder")
		            .endObject());
		client.update(updateRequest).get();*/
		
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("nickname", "for Java");
		
		UpdateRequest request = new UpdateRequest("library", "user", "35");
		request.doc(updateMap);
		try {
			UpdateResponse response = client.update(request).get();
			System.out.println(response.isCreated());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test 
	public void multiGet(){
		MultiGetResponse response = client.prepareMultiGet()
				.add("library","user","1")	
				.add("library","user","2","3","4")		//这里是 mget的ids写法
				.get();
		
		for(MultiGetItemResponse itemResponse : response){
			GetResponse getResponse = itemResponse.getResponse();
			if(getResponse.isExists()){
				System.out.println(getResponse.getSourceAsString());
			}
		}
	}
	
	
	@Test
	public void bulk(){
		// 只能执行 增删改
		BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
		
		Map<String, Object> addMap = new HashMap<String, Object>();
		addMap.put("nickname", "bulks");
		addMap.put("password", "456789");
		
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("nickname", "批量操作");
		
		bulkRequestBuilder.add(new IndexRequest("library","user","38").source(addMap))
		.add(new DeleteRequest("library","user","32"))
		.add(new UpdateRequest("library","user","100").doc(updateMap));
		
		BulkResponse response = bulkRequestBuilder.get();
		
		//批量处理是否有失败的（冤头债主模式）
		System.out.println(response.hasFailures());
		
		
		//可以具体定位到哪个 request 失败
		for(BulkItemResponse itemResponse : response.getItems()){
			//只要是 true就是失败的
			System.out.println(itemResponse.isFailed());
		}
	}
	
	//批处理处理器
	public void BulkProcessors(){
		BulkProcessor bulkProcessor = BulkProcessor.builder(client, new Listener() {
			
			//调用前  预处理
			public void beforeBulk(long executionId, BulkRequest request) {
				System.out.println("提交前");
			}
			
			//出现异常之后
			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
				System.out.println("异常");
			}
			
			//调用之后
			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
				// TODO Auto-generated method stub
				System.out.println("提交后");
			}
			//设置5秒提交一次
		}).setFlushInterval(TimeValue.timeValueSeconds(5)).build();
		
		Map<String, Object> addMap = new HashMap<String, Object>();
		addMap.put("nickname", "bulks");
		addMap.put("password", "456789");
		bulkProcessor.add(new IndexRequest("library","user","39").source(addMap));
		bulkProcessor.flush();
	}
	
	@Test
	public void test2(){
		BulkProcessors();
	}
	
	
	
}
