package com.fineShop.test;

import javax.annotation.Resource;
import javax.management.Query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Wildcard;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月18日 下午11:54:17<p>
* 类说明   术语级别搜索   精确搜索
* 
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/spring/spring-*.xml")
public class TermLevelTest {
	@Resource
	private Client client;
	
	/*
	#精确字段搜索
	GET library/user/_search
	{
	  "query": {
	    "term": {
	      "username": "test"
	      }
	  }
	}
	*/
	@Test
	public void term(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.termQuery("username", "test")).get();
		
		System.out.println(response.toString());
	}
	
	/*
	# 精确多字段搜索 类似mysql 的 in
	GET library/user/_search
	{
	  "query": {
	    "terms": {
	      "username": [
	        "test1",
	        "test2"
	      ]
	    }
	  }
	  */
	@Test
	public void terms(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.termsQuery("username", "test1","test2")).get();
		
		System.out.println(response.toString());
	}
	
	/*
	GET library/user/_search
	{
	  "query": {
	    "range": {
	      "id": {
	        "gte": 10,
	        "lte": 20
	      }
	    }
	  }
	}
	*/
	@Test
	public void range(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.rangeQuery("id").from(10).to(20)
						.includeLower(true).includeUpper(true)).get();
		//includeLower （true ）	 包括较低的值意味着from是gt何时false或gte何时true  默认 true
		//includeUpper （false ） 包括上限值意味着to是lt何时false或lte何时true  默认 true
		System.out.println(response.toString());
	}
	
	/*
	 过滤指定字段不为空的数据
	GET library/user/_search
	{
	  "query": {
	    "filtered": {
	      "filter": {
	        "exists": {
	          "field": "version"
	        }
	      }
	    }
	  }
	}
	*/
	@Test
	public void exists(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.existsQuery("version")).get();
		
		System.out.println(response.toString());
	}
	
	/*
	#空值查询
	GET library/user/_search
	{
	  "query": {
	    "filtered": {
	      "filter": {
	        "missing": {
	          "field": "version"
	        }
	      }
	    }
	  }
	}
	*/
	@Test
	public void missing(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.missingQuery("version")).get();
		
		System.out.println(response);
	}
	
	/*
	#前缀匹配查询
	GET library/user/_search
	{
	  "query": {
	    "prefix": {
	      "username": "test"
	    }
	  }
	}
	*/
	@Test
	public void prefix(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.prefixQuery("username", "test")).get();
		
		System.out.println(response.toString());
	}
	
	/*
	 通配符查询
	 # * 全模糊匹配
	 # ? 单个字符匹配
	GET library/user/_search
	{
	  "query": {
	    "wildcard": {
	      "username": "*es*"
	    }
	  }
	}
	*/
	@Test
	public void wildcard(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.wildcardQuery("username", "*es*")).get();
		
		System.out.println(response.toString());
	}
	
	/*
	# 正则匹配搜素
	GET library/user/_search
	{
	 "query": {
	   "regexp" : {
	     "nickname" : "[\u4e00-\u9fa5]"
	   }
	 }
	}
	 */
	@Test
	public void regexp(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.regexpQuery("nickname", "[\u4e00-\u9fa5]")).get();
		
		System.out.println(response.toString());
	}
	
	/*
	#模糊词查询
	GET library/user/_search
	{
	  "query": {
	    "fuzzy": {
	      "nickname": {"value": "张"}
	    }
	  }
	}
	*/
	@Test
	public void fuzzy(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.fuzzyQuery("nickname", "张")).get();
		
		System.out.println(response.toString());
	}
	
	/*
	#fuzziness  基于value前后  可以指定范围 1d(天数)  或者数字
	#本例子就是查询id [8,12] 的数据
	GET library/user/_search
	{
	  "query": {
	    "fuzzy": {
	      "id": {"value": "10" , "fuzziness" : 2}
	    }
	  }
	}
	*/
	@Test
	public void fuzzy2(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.fuzzyQuery("id", 10).fuzziness(Fuzziness.fromEdits(2))).get();
		
		System.out.println(response.toString());
	}
	
	/*
	# 类型查询(指定类型查询)
	GET library/_search
	{
	  "query": {
	    "type" : {"value" : "user"}
	  }
	}
	*/
	@Test
	public void type(){
		SearchResponse response = client.prepareSearch("library")
				.setQuery(QueryBuilders.typeQuery("user")).get();
		
		System.out.println(response.toString());
	}
	
	/*
	#类型查询  根据映射类型查询
	# 类似mget
	GET library/user/_search
	{
	 "query": {
	    "ids" : {"values" : [1,2,3,4,5]}
	 }
	}
	*/
	@Test
	public void test(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.idsQuery().ids("1","2","3","4","5")).get();
		
		System.out.println(response.toString());
	}
	
	@Test
	public void test3(){
		System.out.println(1);
	}
}
