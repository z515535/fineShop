package com.fineShop.test;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentile;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanks;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats.Bounds;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

 
/**
* @author 作者 wugf:
* @version 创建时间：2017年3月10日 下午10:56:20<p>
* 类说明		聚合 demo
* 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/spring/spring-*.xml")
public class Aggs {

	@Resource
	private Client client;
	
	
	/**
	 * 下面返回对象的 Min Max 等都继承了  SingleValue ， 通用的话可以使用它
	 */
	
	/*GET library/user/_search
	{
	  "size": 0, 
	  "aggs": {
	    "test": {
	      "min": {
	        "field": "id"
	      }
	    }
	  }
	}*/
	@Test
	public void min(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.addAggregation(AggregationBuilders.min("test").field("id")).get();
		
		Min min = response.getAggregations().get("test");
		System.out.println(min.getValue());
	}
	
	/*GET library/user/_search
	{
	  "size": 0
	  , "aggs": {
	    "test": {
	      "max": {
	        "field": "id"
	      }
	    }
	  }
	}*/
	@Test
	public void max(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.addAggregation(AggregationBuilders.max("test").field("id")).get();
		
		Max max = response.getAggregations().get("test");
		System.out.println(max.getValue());
	}
	
	/*GET library/user/_search
	{
	  "size": 0,
	  "aggs": {
	    "test": {
	      "sum": {
	        "field": "id"
	      }
	    }
	  }
	}*/
	@Test
	public void sum(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.addAggregation(AggregationBuilders.sum("test").field("id")).get();
		
		Sum sum = response.getAggregations().get("test");
		System.out.println(sum.getValue());
	}
	
	/*GET library/user/_search
	{
	  "size": 0,
	  "aggs": {
	    "test": {
	      "avg": {
	        "field": "id"
	      }
	    }
	  }
	}*/
	@Test
	public void avg(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.addAggregation(AggregationBuilders.avg("test").field("id")).get();
		
		Avg avg = response.getAggregations().get("test");
		System.out.println(avg.getValue());
	}
	
	/**
	 汇总的聚合信息 stats
	 GET library/user/_search
	{
	  "size": 0,
	  "aggs": {
	    "test": {
	      "stats": {
	        "field": "id"
	      }
	    }
	  }
	}*/
	@Test
	public void stats(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.addAggregation(AggregationBuilders.stats("test").field("id")).get();
		
		Stats stats = response.getAggregations().get("test");
		System.out.println("min:"+stats.getMin());
		System.out.println("max:"+stats.getMax());
		System.out.println("avg:"+stats.getAvg());
		System.out.println("count:"+stats.getCount());
		System.out.println("sum:"+stats.getSum());
	}
	
	/**
	 * 高级统计
	 */
	@Test
	public void extendedStats(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.addAggregation(AggregationBuilders.extendedStats("test").field("id")).get();
		
		ExtendedStats extendedStats = response.getAggregations().get("test");
		System.out.println("min:"+extendedStats.getMin());
		System.out.println("max:"+extendedStats.getMax());
		System.out.println("avg:"+extendedStats.getAvg());
		System.out.println("count:"+extendedStats.getCount());
		System.out.println("sum:"+extendedStats.getSum());
		
		//高级属性
		System.out.println("sum_of_squares:"+extendedStats.getSumOfSquares());			//平方和
		System.out.println("variance"+extendedStats.getVariance());						//平方差
		System.out.println("deviation:"+extendedStats.getStdDeviation());				//偏移值
		System.out.println("upper:"+extendedStats.getStdDeviationBound(Bounds.UPPER));	//获取上偏移值
		System.out.println("lower"+extendedStats.getStdDeviationBound(Bounds.LOWER));	//获取下偏移值
	}
	
	
	/*GET library/user/_search
	{
	  "size": 0,
	  "aggs": {
	    "test" : {
	      "value_count": {
	        "field": "id"
	      }
	    }
	  }
	}*/
	@Test
	public void count(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.addAggregation(AggregationBuilders.count("test").field("id")).get();
		
		ValueCount count = response.getAggregations().get("test");
		System.out.println(count.getValue());
	}
	
	
	/*
	 百分比统计
	 # percentiles 基于百分比统计 percents 百分之多少的数据在它value的范围内
	GET library/user/_search
	{
	  "size": 0,
	  "aggs": {
	    "test": {
	      "percentiles": {
	        "field": "status",
	        "percents": [
	          1,
	          5,
	          25,
	          50,
	          75,
	          95,
	          99
	        ]
	      }
	    }
	  }
	}*/
	@Test
	public void percentiles(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.addAggregation(AggregationBuilders.percentiles("test").field("status")
				.percentiles(1.0, 5.0, 10.0, 20.0, 30.0, 75.0, 95.0, 99.0))	//这些是百分比参数
				.get();
		
		Percentiles percentiles = response.getAggregations().get("test");
		
		for(Percentile percentile : percentiles){
			System.out.println(percentile.getPercent() + ":" + percentile.getValue());
		}
	}
	
	/*
	 范围百分比
	 # 有百分之多少的数据落在 values的范围内
	GET library/user/_search
	{
	  "size": 0,
	  "aggs": {
	    "test" : {
	      "percentile_ranks": {
	        "field": "status",
	        "values": [
	          0,
	          1
	        ]
	      }
	    }
	  }
	}*/
	@Test
	public void percentileRanks(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
		.addAggregation(AggregationBuilders.percentileRanks("test")
		.field("status")
		.percentiles(0,1)).get();
		
		PercentileRanks percentileRanks = response.getAggregations().get("test");
		for(Percentile percentile : percentileRanks){
			System.out.println(percentile.getPercent() + ":" + percentile.getValue());
		}
	}
	
	
	/*# 去重统计 count
	GET library/user/_search
	{
	  "size": 0
	  , "aggs": {
	    "test": {
	      "cardinality": {
	        "field": "nickname"
	      }
	    }
	  }	
	}*/
	@Test
	public void cardinality(){
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.addAggregation(AggregationBuilders.cardinality("test")
				.field("nickname")).get();
		
		Cardinality cardinality = response.getAggregations().get("test");
		System.out.println(cardinality.getValue());
	}
	
	/**
	 * 类似mysql 的top
	 */
	@Test
	public void topHits(){
		//获取前10条记录
		SearchResponse response = client.prepareSearch("library").setTypes("user")
				.addAggregation(AggregationBuilders.topHits("test")
				.setSize(10)).get();
		
		TopHits topHits = response.getAggregations().get("test");
		
		for(SearchHit hit : topHits.getHits().getHits()){
			System.out.println(hit.getSourceAsString());
		}
	}
	
	
}
