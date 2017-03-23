package com.fineShop.test;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.SimpleQueryStringBuilder.Operator;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 全文搜索
 * @author acer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/spring/spring-*.xml")
public class FullTextTest {

	@Resource
	private Client client;
	
	/*
	 全搜索
	GET library/books/_search
	{
	  "query": {
	    "match_all": {}
	  }
	}*/
	@Test
	public void matchAll(){
		SearchResponse response = client.prepareSearch("library").setTypes("books")
				.setQuery(QueryBuilders.matchAllQuery()).get();
		
		for (SearchHit hit : response.getHits().getHits()) {
			System.out.println(hit.getSourceAsString());
		}
		//System.out.println(response.toString());
	}
	
	/*
	 * 关键词搜索
	GET library/books/_search
	{
	  "query": {
	    "match": {
	      "name": "java"
	    }
	  }
	}
	*/
	@Test
	public void match(){
		SearchResponse response = client.prepareSearch("library").setTypes("books")
				.setQuery(QueryBuilders.matchQuery("name", "java")).get();
		
		for (SearchHit hit : response.getHits().getHits()) {
			System.out.println(hit.getSourceAsString());
		}
	}
	
	
	/*
	 多字段关键词搜索
	GET lovego/product/_search
	{
	  "query": {
	    "multi_match": {
	      "query": "色",
	      "fields": ["product_name","product_title"]
	    }
	  }
	}*/
	@Test
	public void multiMatch(){
		SearchResponse response = client.prepareSearch("lovego").setTypes("product")
				.setQuery(QueryBuilders.multiMatchQuery("色", "product_name","product_title")).get();
		
		for (SearchHit hit : response.getHits().getHits()) {
			System.out.println(hit.getSourceAsString());
		}
	}
	
	
	/*
	 * 具有大于0.1％的文档频率（例如"this"和"is"）的词语将被视为共同词语。
	GET lovego/product/_search
	{
	  "query": {
	        "common": {
	            "product_name": {
	                "query": "苹果iPhone",
	                "cutoff_frequency": 0.1
	            }
	        }
	    }
	}
	*/
	@Test
	public void common(){
		SearchResponse response = client.prepareSearch("lovego").setTypes("product")
				.setQuery(QueryBuilders.commonTermsQuery("product_name", "iphone 6").cutoffFrequency(0.001f)).get();
		
		for (SearchHit hit : response.getHits().getHits()) {
			System.out.println(hit.getSourceAsString());
		}
	}
	
	/*
	# 全文检索  query_string _all,也可以指定字段 支持前后缀等模糊匹配
	# + 表示AND操作
	| 表示或操作
	- 否定单个令牌
	" 包装多个令牌以表示用于搜索的短语
	* 在术语的末尾表示前缀查询
	(and)表示优先级
	~N 后一个字表示编辑距离（模糊性）
	~N 后一个短语表示消耗量
	GET library/user/_search
	{
	  "query": {
	    "query_string": {
	      "default_field": "_all",
	      "query": "test* -test6"
	    }
	  }
	}
	*/
	@Test
	public void quertString(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.queryStringQuery("test* -test6")).get();
		
		System.err.println(response);
	}
	/*
	# 与query_string相比，它针对的是字段搜索，但拥有字符搜索的功能
	# 可以指定运算符 default_operator 默认为 or
	GET library/user/_search
	{
	  "query": {
	    "simple_query_string": {
	      "query": "test* -test6",
	      "fields": ["username"],
	      "default_operator": "AND"
	    }
	  }
	}
	*/
	@Test
	public void simpleQueryString(){
		SearchResponse response =  client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.simpleQueryStringQuery("test* -test6")
						.defaultOperator(Operator.AND).field("username").field("password")).get();
		
		System.out.println(response.toString());
	}
}
