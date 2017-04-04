package com.fineShop.search.session;

import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
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
	
	public String selectStrinByMap(Sentence sentence, Map<String, Object> param) {
		return this.selectResponseByMap(sentence, param).toString();
	}

	public SearchResponse selectResponseByMap(Sentence sentence, Map<String, Object> param) {
		String a[] = {"username","id"};
		String b[] = {};
		Map<String, Object> paramMap = (Map<String, Object>) param;
		SearchResponse response = client.prepareSearch(sentence.getIndex()).setTypes(sentence.getType())
				.setQuery(QueryBuilders.templateQuery(sentence.getResource(), paramMap)).setFetchSource(a, null).get();
		return response;
	}

	public String selectStringByObject(Sentence sentence, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchResponse selectResponseByObject(Sentence sentence, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public String updateStringByeMap(Sentence sentence, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	public UpdateResponse updateResponseByMap(Sentence sentence, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	public String updateStringByObject(Sentence sentence, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public UpdateResponse updateResponseByObject(Sentence sentence, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public String insertStringByMap(Sentence sentence, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	public IndexResponse insertResponseByMap(Sentence sentence, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	public String insertStringByObject(Sentence sentence, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public IndexResponse indexResponseByObject(Sentence sentence, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public String deleteStringByMap(Sentence sentence, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	public DeleteResponse deleteResponseByMap(Sentence sentence, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	public String deleteStringByObject(Sentence sentence, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public DeleteResponse deleteResponseByObject(Sentence sentence, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}
}
