package com.fineShop.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.script.Template;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.avg.AvgBucketBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.SortParseElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.fineShop.script.ScriptUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月6日 下午10:54:05<p>
* 类说明
* 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/spring/spring-*.xml")
public class SearchApi {
	
	@Resource
	private Client client;
	
	//深度分页
	//当Elasticsearch响应请求时，它必须确定docs的顺序，排列响应结果。如果请求的页数较少
	//（假设每页20个docs）, Elasticsearch不会有什么问题，但是如果页数较大时，比如请求第20页，
	//Elasticsearch不得不取出第1页到第20页的所有docs，再去除第1页到第19页的docs，得到第20页的docs。 
	//解决的方法就是使用Scroll。因为Elasticsearch要做一些操作（确定之前页数的docs）为每一次请求，
	//所以，我们可以让Elasticsearch储存这些信息为之后的查询请求。这样做的缺点是，我们不能永远的
	//储存这些信息，因为存储资源是有限的。所以Elasticsearch中可以设定我们需要存储这些信息的时长。
	
	@Test
	public void scrolls(){
		QueryBuilder qb = new TermQueryBuilder("nickname", "lock");

		SearchResponse scrollResp = client.prepareSearch()
		        .addSort(SortParseElement.DOC_FIELD_NAME, SortOrder.ASC)
		        .setScroll(new TimeValue(60000))
		        .setQuery(qb)
		        .setSize(1).execute().actionGet();
		
		 for (SearchHit hit : scrollResp.getHits().getHits()) {
		      System.out.println( hit.getSourceAsString());
		 }
		 System.out.println(scrollResp.getHits().getTotalHits());
	}
	
	@Test
	public void term() throws InterruptedException, ExecutionException{
		QueryBuilder term = QueryBuilders.termQuery("nickname", "lock");
		//可以设置多个 query
		//QueryBuilder term2 = QueryBuilders.termQuery("id", "2");
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(term).execute().get();
		//get() actionGet()的区别是前者同步执行，后者是异步  使用Future机制
		for (SearchHit hit : response.getHits().getHits()){
			System.out.println(hit.getSourceAsString());
		}
	}
	
	@Test
	public void terms(){
		QueryBuilder terms = QueryBuilders.termsQuery("password", "test","test2");
		SearchRequestBuilder request = client.prepareSearch("library").setTypes("user").setQuery(terms);
		SearchResponse response = request.get();
		
		System.out.println(response.getHits().getTotalHits());
		for(SearchHit hit : response.getHits().getHits()){
			System.out.println(hit.getSourceAsString());
		}
	}
	
	@Test
	public void match(){
		QueryBuilder match = QueryBuilders.matchQuery("nickname", "老张");
		SearchRequestBuilder requestBuilder = client.prepareSearch("library").setTypes("user").setQuery(match);
		SearchResponse response = requestBuilder.get();
		for (SearchHit hit : response.getHits().getHits()){
			System.out.println(hit.getSourceAsString());
		}
	}
	
	@Test
	public void matchPhrase(){
		QueryBuilder matchPhrase = QueryBuilders.matchPhraseQuery("content", "elasticsearch book");
		SearchRequestBuilder requestBuilder = client.prepareSearch("library").setTypes("books").setQuery(matchPhrase);
		SearchResponse response = requestBuilder.get();
		
		for (SearchHit hit : response.getHits().getHits()){
			System.out.println(hit.getSourceAsString());
		}
	}
	
	
	//批量操作
	@Test
	public void multiSearch(){
		
		//QueryBuilders.queryStringQuery
		//使用分词器查询所有字段
		
		SearchRequestBuilder sb1 = 
				client.prepareSearch().setQuery(QueryBuilders.queryStringQuery("elasticsearch")).setSize(100);
		
		SearchRequestBuilder sb2 = client.prepareSearch().setQuery(QueryBuilders.matchQuery("nickname", "张山")).setSize(100);
		
		MultiSearchResponse response = client.prepareMultiSearch()
				.add(sb1)
				.add(sb2).execute().actionGet();
		
		//总条数
		long nbHits = 0;
		
		for(MultiSearchResponse.Item item : response.getResponses()){
			SearchResponse sr = item.getResponse();
			nbHits += sr.getHits().getTotalHits();
			
			for(SearchHit hit : sr.getHits().getHits()){
				System.out.println(hit.getSourceAsString());
				System.out.println(hit.getScore());
			}
		}
		System.out.println(nbHits);
	}
	
	
	
	/*GET library/user/_search
	{
	  "aggs": {
	    "initName": {
	      "terms": {
	        "field": "password"
	      }
	    }
	  }
	}*/
	@Test
	public void aggs() throws IOException{
		SearchRequestBuilder requestBuilder = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.matchAllQuery())
				.addAggregation(AggregationBuilders
				.terms("initName").field("password"));
		
		SearchResponse response  =requestBuilder.get();
		StringTerms terms = response.getAggregations().get("initName");
		for(Bucket bucket : terms.getBuckets()){
			System.out.println(bucket.getKey());
			System.out.println(bucket.getDocCount());
			System.out.println("-------------------");
		}
	}
	
	
	/*GET library/books/_search
	{
	  "aggs": {
	    "test" :{
	      "date_histogram": {
	        "field": "publish_date",
	        "interval": "day",
	        "format": "yyyy-MM-dd"
	      }
	    }
	  }
	}*/
	@Test
	public void dateHistogram(){
		SearchRequestBuilder requestBuilder = client.prepareSearch("library").setTypes("books")
				.setQuery(QueryBuilders.matchAllQuery())
				.addAggregation(AggregationBuilders
				.dateHistogram("test").
				field("publish_date").
				interval(DateHistogramInterval.DAY).
				format("yyyy-MM-dd"));
		SearchResponse response = requestBuilder.get();
		Histogram histogram = response.getAggregations().get("test");
		
		for(Histogram.Bucket bucket : histogram.getBuckets()){
			System.out.println(bucket.getKeyAsString());
			System.out.println(bucket.getKey());
			System.out.println(bucket.getDocCount());
			System.out.println("------------");
		}
	}
	
	//批量聚合
	/*GET library/books/_search
	{
	  "aggs": {
	    "initName": {
	      "terms": {
	        "field": "title"
	      }
	    },
	    "test" :{
	      "date_histogram": {
	        "field": "publish_date",
	        "interval": "day",
	        "format": "yyyy-MM-dd"
	      }
	    }
	  }
	}*/
	@Test
	public void multiAggs(){
		SearchRequestBuilder requestBuilder = client.prepareSearch("library").setTypes("books")
				.setQuery(QueryBuilders.matchAllQuery())
				.addAggregation(AggregationBuilders
				.terms("initName").field("title"))
				
				.addAggregation(AggregationBuilders
				.dateHistogram("test")
				.field("publish_date")
				.interval(DateHistogramInterval.DAY)
				.format("yyyy-MM-dd"));
		
		SearchResponse response = requestBuilder.get();
		
		StringTerms terms = response.getAggregations().get("initName");
		
		for(Bucket bucket : terms.getBuckets()){
			System.out.println(bucket.getKey());
			System.out.println(bucket.getDocCount());
			System.out.println("------------");
		}
		
		System.out.println("[[[[[[[[[[[]]]]]]]]]");
		
		Histogram histogram =response.getAggregations().get("test");
		for(Histogram.Bucket bucket : histogram.getBuckets()){
			System.out.println(bucket.getKeyAsString());
			System.out.println(bucket.getKey());
			System.out.println(bucket.getDocCount());
			System.out.println("------------");
		}
	}
	
	/*GET library/user/_search
	{
	  "aggs": {
	    "test1": {
	      "terms": {
	        "field": "nickname"
	      },
	      "aggs": {
	        "test2" : {
	          "date_histogram": {
	            "field": "@timestamp",
	            "interval": "month",
	            "format": "yyyy-MM-dd"
	          },
	          "aggs": {
	            "test3": {
	              "avg": {
	                "field": "id"
	              }
	            }
	          }
	        }
	      }
	    }
	  }
	}*/
	//聚合嵌套
	@Test
	public void subAggs(){
		SearchResponse response = client.prepareSearch("library").setTypes("user").addAggregation(
				AggregationBuilders.terms("test1").field("nickname")
				.subAggregation(AggregationBuilders.dateHistogram("test2").field("@timestamp")
				.interval(DateHistogramInterval.DAY).format("yyyy-MM-dd")
				.subAggregation(AggregationBuilders.avg("test3").field("id")))).get();
		
		StringTerms terms = response.getAggregations().get("test1");
		
		//获取第一层的嵌套聚合
		/*for(Bucket bucket : terms.getBuckets()){
			System.out.println(bucket.getKeyAsString());
		}*/
		
		//获取第二层的嵌套聚合
		/*for(Bucket bucket : terms.getBuckets()){
			Histogram histogram = bucket.getAggregations().get("test2");
			for(Histogram.Bucket bucket2 : histogram.getBuckets()){
				System.out.println(bucket2.getKeyAsString());
			}
		}*/
		
		//获取第三层的嵌套聚合
		for(Bucket bucket : terms.getBuckets()){
			Histogram histogram = bucket.getAggregations().get("test2");
			for(Histogram.Bucket bucket2 : histogram.getBuckets()){
				InternalAvg avg = bucket2.getAggregations().get("test3");
				System.out.println(avg.getName());
			}
		}
	}
	
	/**
	 * 设置每个分片返回的最大数量条数 ： terminate_after
	 *
		GET library/_search
		{
		  "query": {
		    "match_all": {}
		  },
		  "terminate_after":1
		}*/
	@Test
	public void TerminateAfter(){
		SearchResponse response = client.prepareSearch("library").setTerminateAfter(1).get();
		
		//是否有返回
		if(response.isTerminatedEarly()){
			for (SearchHit hit : response.getHits().getHits()){
				System.out.println(hit.getSourceAsString());
			}
		}
	}
	
	//  使用脚本查询,脚本占位符为{{key}}
	@Test
	public void template(){
		String script = ScriptUtil.getScript(ScriptUtil.class.getResource("").getPath()+"/search.script");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nickname", "张");
		
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.templateQuery(script, params)).get();
		for(SearchHit hit : response.getHits().getHits()){
			System.out.println(hit.getSourceAsString());
		}
	}
}
