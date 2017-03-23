package com.fineShop.test;



import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.children.Children;
import org.elasticsearch.search.aggregations.bucket.geogrid.GeoHashGrid;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.ReverseNested;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/spring/spring-*.xml")
public class BucketAggs {
	
	@Resource
	private Client client;
	
	/*
	 统计不为空的数据
	GET lovego/product/_search
	{
	  "size": 0,
	  "aggs": {
	    "agg": {
	      "missing": {
	        "field": "guide_price"
	      }
	    }
	  }
	}*/
	@Test
	public void missing(){
		SearchResponse response = client.prepareSearch("lovego").setTypes("product")
				.addAggregation(AggregationBuilders.missing("agg").field("guide_price")).get();
		
		Missing missing = response.getAggregations().get("agg");
		System.out.println(missing.getDocCount());
	}
	
	
	/**
	 * 嵌套聚合     使用场景，映射中有属性嵌套的
	 * 
		PUT library/issue/_mapping
		{
		  "issue" : {
		    "properties": {
		      "tags" : {"type": "string"},
		      "comments" :{
		        "type": "nested",
		        "properties": {
		          "username" : {"type" : "string"},
		          "comment" : {"type" : "string"}
		        }
		      }
		    }
		  }
		}
		
		GET library/issue/_search
		{
		  "size": 0,
		   "aggs": {
		     "agg": {
		       "nested": {
		         "path": "comments"
		       },
		       "aggs": {
		         "username": {
		           "terms": {
		             "field": "comments.username"
		           }
		         }
		       }
		     }
		   }
		}
*/
	@Test
	public void nested(){
		SearchResponse response = client.prepareSearch("library").setTypes("issue")
		.addAggregation(AggregationBuilders.nested("agg").path("comments")
		.subAggregation(AggregationBuilders.terms("username").field("comments.username"))).get();
		
		Nested nested = response.getAggregations().get("agg");
		StringTerms terms = nested.getAggregations().get("username");

		for(Bucket bucket : terms.getBuckets()){
			System.out.println(bucket.getKeyAsString());
			System.out.println(bucket.getDocCount());
		}
	}
	
	/*
	#反向嵌套查询
	GET library/issue/_search
	{
	  "size": 0,
	  "aggs": {
	    "comments": {
	      "nested": {
	        "path": "comments"
	      },
	      "aggs": {
	        "top_usernames": {
	          "terms": {
	            "field": "comments.username"
	          },
	          "aggs": {
	            "comments_to_issue": {
	              "reverse_nested": {},
	              "aggs": {
	                "tags": {
	                  "terms": {
	                    "field": "tags"
	                  }
	                }
	              }
	            }
	          }
	        }
	      }
	    }
	  }
	}
	*/
	@Test
	public void reverseNested(){
		SearchResponse response = client.prepareSearch("library").setTypes("issue")
				.addAggregation(AggregationBuilders.nested("comments").path("comments")
				.subAggregation(AggregationBuilders.terms("top_usernames").field("comments.username")
				.subAggregation(AggregationBuilders.reverseNested("comments_to_issue")
				.subAggregation(AggregationBuilders.terms("tags").field("tags"))))).get();
		
		Nested nested = response.getAggregations().get("comments");
		StringTerms terms = nested.getAggregations().get("top_usernames");
		
		for (Bucket bucket : terms.getBuckets()) {
			ReverseNested reverseNested = bucket.getAggregations().get("comments_to_issue");
			StringTerms terms2 = reverseNested.getAggregations().get("tags");
			
			for (Bucket bucket2 : terms2.getBuckets()) {
				System.out.println(bucket2.getKeyAsString());
				System.out.println(bucket2.getDocCount());
			}
		}
	}
	
	
	/*  父子文档聚合
	PUT staff/employee/_mapping
	{
	  "properties": {
	    "name" : {"type": "string"},
	    "dob" : {"type": "date"},
	    "hobby" : {"type": "string"}
	  }
	  , "_parent": {
	    "type": "branch"
	  }
	}


	PUT staff/branch/_mapping
	{
	  "properties": {
	    "name" : {"type": "string"},
	    "city" : {"type": "string"},
	    "country" : {"type": "string"}
	  }
	}
	
	
	GET staff/branch/_search
	{
	  "size": 0,
	  "aggs": {
	    "branch": {
	      "terms": {
	        "field": "name"
	      },
	      "aggs": {
	        "to_children": {
	          "children": {
	            "type": "employee"
	          },
	          "aggs": {
	            "employee": {
	              "terms": {
	                "field": "hobby"
	              }
	            }
	          }
	        }
	      }
	    }
	  }
	}
	*/
	
	@Test
	public void childrenAndParent(){
		SearchResponse response = client.prepareSearch("staff").setTypes("branch").setSize(0)
				.addAggregation(AggregationBuilders.terms("branch").field("name")
				.subAggregation(AggregationBuilders.children("to_children").childType("employee")
				.subAggregation(AggregationBuilders.terms("employee").field("hobby")))).get();
		
		StringTerms terms = response.getAggregations().get("branch");
		
		for (Bucket bucket : terms.getBuckets()) {
			Children children = bucket.getAggregations().get("to_children");
			StringTerms terms2 = children.getAggregations().get("employee");
			
			for (Bucket bucket2 : terms2.getBuckets()) {
				System.out.println(bucket2.getKeyAsString());
				System.out.println(bucket2.getDocCount());
			}
		}
		//toString response 可以直接返回es请求的json数据
		//System.out.println(response.toString());
		
	}
	
	
	/*
	聚合排序，默认是根据key排序（_term） ，也可以使用 _count 排序
	GET lovego/product/_search
	{
	  "size": 0, 
	  "aggs": {
	    "product": {
	      "terms": {
	        "field": "product_name",
	        "size": 10,
	        "order": {
	          "_term": "asc"
	        }
	      }
	    }
	  }
	}
	*/
	@Test
	public void order(){
		SearchResponse response = client.prepareSearch("lovego").setTypes("product").setSize(0)
				.addAggregation(AggregationBuilders.terms("agg").field("product_name")
						.order(Terms.Order.count(true))).get();
		
		StringTerms terms = response.getAggregations().get("agg");
		for (Bucket bucket : terms.getBuckets()) {
			System.out.println(bucket.getKey());
			System.out.println(bucket.getDocCount());
		}
	}
	
	
	/*
	 * significant_terms 聚合可以在你数据集中找到一些 异常 的指标。
	 * 这些异常的数据指标通常比我们预估出现的频次要更频繁，这些统计上的异常指标通常象征着数据里的某些有趣信息
	 
	 
	  GET lovego/product/_search
		{
		  "size": 0,
		  "aggs": {
		    "agg": {
		      "significant_terms": {
		        "field": "product_name"
		      }
		    }
		  }
		}
	 */
	
	@Test
	public void significantTerms(){
		SearchResponse response = client.prepareSearch("lovego").setTypes("product").setSize(0)
				.addAggregation(AggregationBuilders.significantTerms("agg").field("product_name")).get();
		
		SignificantTerms terms = response.getAggregations().get("agg");
		
		for (SignificantTerms.Bucket bucket : terms.getBuckets()) {
			System.out.println(bucket.getKeyAsString());
			System.out.println(bucket.getDocCount());
		}
		
		//System.out.println(response.toString());
	}
	
	
	/**
	 范围聚合  from 是包含, to 是不包含
	 GET lovego/product/_search
	{
	  "size": 0,
	  "aggs": {
	    "agg": {
	      "range": {
	        "field": "product_price",
	        "ranges": [
	            {"to": 50},
	            {"from": 50,"to": 100}
	        ]
	      }
	    }
	  }
	}
	*/
	
	@Test
	public void range() {
		SearchResponse response = client.prepareSearch("lovego").setTypes("product").setSize(0)
				.addAggregation(AggregationBuilders.range("agg").field("product_price")
						.addRange(0,50).addRange(50, 100)).get();
		
		Range range = response.getAggregations().get("agg");
		
		for (Range.Bucket bucket : range.getBuckets()) {
			System.out.println(bucket.getKeyAsString());
			System.out.println(bucket.getDocCount());
		}
	}
	
	
	/**
	 根据日期范围聚合数据
	 GET lovego/product/_search
	 {
		  "size": 0,
		  "aggs": {
		    "dateRange": {
		      "date_range": {
		        "field": "shelf_date",
		        "format": "yyyy-MM-dd", 
		        "ranges": [
		          {
		            "from": "now-100d/d",
		            "to": "now"
		          }
		        ]
		      }
		    }
		  }
		}
		*/
	@Test
	public void dateRange(){
		SearchResponse response = client.prepareSearch("lovego").setTypes("product").setSize(0)
				.addAggregation(AggregationBuilders.dateRange("dateRange").addRange("now-100d/d", "now").field("shelf_date").format("yyyy-MM-dd")).get();
		
		Range range = response.getAggregations().get("dateRange");
		
		for (Range.Bucket bucket : range.getBuckets()) {
			System.out.println(bucket.getToAsString());
			System.out.println(bucket.getFromAsString());
			System.out.println(bucket.getDocCount());
		}
		//System.out.println(response.toString());
	}
	
	/*
	 数据直方图
	GET lovego/product/_search
	{
	  "size": 0,
	  "aggs": {
	    "agg" : {
	      "histogram": {
	        "field": "product_price",
	        "interval": 50,
	        "min_doc_count": 1
	      }
	    }
	  }
	}
	*/
	@Test
	public void histogram(){
		SearchResponse response = client.prepareSearch("lovego").setTypes("product").setSize(0)
				.addAggregation(AggregationBuilders.histogram("agg").field("product_price").interval(50).minDocCount(1)).get();
		
		Histogram histogram = response.getAggregations().get("agg");
		
		for (Histogram.Bucket bucket : histogram.getBuckets()) {
			System.out.println(bucket.getKeyAsString());
			System.out.println(bucket.getDocCount());
		}
//		System.out.println(response);
	}
	
	/*
	根据经纬度范围查询数据 (需要定义一个原点)  距离单位，默认为米
	GET library/map/_search
	{
	  "size": 0, 
	  "aggs" : {
	      "rings_around_amsterdam" : {
	          "geo_distance" : {
	              "field" : "host",
	              "origin" : "49.23, 86.15",
	              "ranges" : [
	                  { "to" : 100 },
	                  { "from" : 100, "to" : 300 },
	                  { "from" : 300 }
	              ]
	          }
	      }
	   } 
	}
	*/
	@Test
	public void geoDistince(){
		SearchResponse response = client.prepareSearch("library").setTypes("map").setSize(0)
				.addAggregation(AggregationBuilders.geoDistance("agg").field("host")
						.point(new GeoPoint(49.23,86.15)).unit(DistanceUnit.METERS)
						.addUnboundedTo(100)
						.addRange(100, 300)
						.addUnboundedFrom(300)).get();
		
		Range agg = response.getAggregations().get("agg");
		for (Range.Bucket bucket : agg.getBuckets()) {
			System.out.println(bucket.getKeyAsString());
			System.out.println(bucket.getDocCount());
		}
//		System.out.println(response);
	}
	
	@Test
	public void geoHashGrid(){
		SearchResponse response = client.prepareSearch("library").setTypes("map").setSize(0)
				.addAggregation(AggregationBuilders.geohashGrid("agg").field("host").precision(3)).get();
		
		GeoHashGrid geoHashGrid = response.getAggregations().get("agg");
		for (GeoHashGrid.Bucket bucket : geoHashGrid.getBuckets()) {
			System.out.println(bucket.getKeyAsString());
			System.out.println(bucket.getDocCount());
		}
	}
}	
