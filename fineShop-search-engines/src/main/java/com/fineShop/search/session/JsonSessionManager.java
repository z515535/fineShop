package com.fineShop.search.session;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.fineShop.search.mapper.Sentence;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月23日 下午9:15:31<p>
* 类说明		操作es 基类
* 
*/
public class JsonSessionManager implements JsonSession{
	
	private Client client;
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public String search(Sentence sentence, Object param) {
		
		Map<String, Object> paramMap = (Map<String, Object>) param;
		
	/*	SearchResponse response = client.prepareSearch(sentence.getIndex())
				.setQuery(QueryBuilders.templateQuery(sentence.getResource(), paramMap)).get();*/
		SearchResponse response = client.prepareSearch("library").setTypes("user")
		.setQuery(QueryBuilders.termQuery("id", 1)).get();
		return response.toString();
	}
}
