package com.fineShop.test;

import javax.annotation.Resource;

import org.apache.lucene.search.ConstantScoreQuery;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
* @author 作者 wugf:
* @version 创建时间：2017年3月19日 下午9:42:21<p>
* 类说明		复合查询
* 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/spring/spring-*.xml")
public class CompoundTest {
	
	@Resource
	private Client client;
	
	/*
	# 常数得分查询
	# 在多条件 or 查询时可以指定权值，权值越高打分越高
	GET library/user/_search
	{
	  "query": {
	    "bool": {
	      "should": [
	        {
	          "constant_score": {
	            "filter": {
	              "term": {
	                "id": "1"
	              }
	            },
	            "boost": 2
	          }
	        },
	        {
	          "constant_score": {
	            "filter": {
	              "term": {
	                "username": "test"
	              }
	            },
	            "boost": 1
	          }
	        }
	      ]
	    }
	  }
	}*/
	@Test
	public void constantScore(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("id", 1).boost(2.0f))).get();
		
		System.out.println(response.toString());
	}
	
	/*
	# bool 过滤查询
	# must 相当于 and
	# should 相当于 or
	# must_not 相当于 not 
	GET library/user/_search
	{
	  "query": {
	    "bool": {
	      "must": [
	        {
	          "term": {
	            "username": "test2"
	          }
	        }
	      ],
	      "should": [
	        {
	          "term": {
	            "id": "10"
	          }
	        }
	      ], 
	      "must_not": [
	        {
	          "term": {
	            "id": "20"
	          }
	        }
	      ]
	    }
	  }
	}
	*/
	
	@Test
	public void bool(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.termQuery("username", "test2"))
						.should(QueryBuilders.termQuery("id", 10))
						.mustNot(QueryBuilders.termQuery("id", 20))).get();
		
		System.out.println(response.toString());
	}
	
	/*
	#最佳字段匹配调优
	#tie_breaker参数会让dis_max查询的行为更像是dis_max和bool的一种折中。它会通过下面的方式改变分值计算过程：
	#取得最佳匹配查询子句的_score。
	#将其它每个匹配的子句的分值乘以tie_breaker。
	#将以上得到的分值进行累加并规范化。
	GET library/user/_search
	{
	  "query": {
	    "dis_max": {
	      "tie_breaker": 0.7,
	      "boost": 1.2,
	      "queries": [
	        {
	          "match": {"username": "test2"}
	        },
	        {
	          "match": {"password": "test2"}
	        }
	      ]
	    }
	  }
	}
	*/
	@Test
	public void disMax(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.disMaxQuery()
						.add(QueryBuilders.matchQuery("username", "test2"))
						.add(QueryBuilders.matchQuery("password", "test2"))
						.boost(1.2f)
						.tieBreaker(0.7f))
				.get();
		
		System.out.println(response.toString());
	}
	
	/*
	#函数匹配得分查询
	# 匹配条件越多，分数越高
	GET library/user/_search
	{
	  "query": {
	    "function_score": {
	      "filter": {
	        "term": {
	          "@version": "1"
	        }
	      }, 
	      "functions": [
	        {
	          "filter": {"term": {"username": "test"}},
	          "weight": 1
	        },
	        {
	          "filter": {"term": {"id": 7}},
	          "weight": 3
	        }
	      ]
	    }
	  }
	}
	*/
	
	@Test
	public void function(){
		
	}
	
	/*
	#boosting 查询
	#可以有效用来降级查询结果，不限bool的 not子句。boosting仍保留不包含的索引词，但是降低了它们的分数

	# positive 包含的
	# negative 不包含的

	GET library/user/_search
	{
	  "query": {
	    "boosting": {
	      "positive": {
	        "match": {
	          "username": "test2"
	        }
	      },
	      "negative": {
	        "term": {
	           "id": "29"
	        }
	      },
	      "negative_boost": 0.2
	    }
	  }
	}
	*/
	@Test
	public void boosting(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.setQuery(QueryBuilders.boostingQuery()
						.positive(QueryBuilders.matchQuery("username", "test2"))
						.negative(QueryBuilders.termQuery("id", 29))
						.negativeBoost(0.2f)).get();
		System.out.println(response);
	}
	
	/*
	# 指定索引查询
	# 搜索其他不匹配的列表索引时会执行 no_match_query
	GET _search
	{
	  "query": {
	    "indices": {
	      "indices": [
	        "library",
	        "library2"
	      ],
	      "query": {
	        "term": {
	          "username": "test1"
	        }
	      },
	      "no_match_query": {
	       "term": {
	         "name": "test3"
	       }
	      }
	    }
	  }
	}*/
	@Test
	public void indices(){
		SearchResponse response = client.prepareSearch()
				.setQuery(QueryBuilders.indicesQuery(QueryBuilders.termQuery("username", "test1"), "library","library2")
						.noMatchQuery(QueryBuilders.termQuery("name", "test3"))).get();
		
		System.out.println(response);
	}
	
	
}
