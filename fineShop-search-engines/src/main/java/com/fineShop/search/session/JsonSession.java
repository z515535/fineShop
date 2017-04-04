package com.fineShop.search.session;

import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;

import com.fineShop.search.mapper.Sentence;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月23日 下午8:22:12<p>
* 类说明		引擎操作基本接口<p>
* 该接口作用是规范此引擎生成代理对象对外提供操作es公共方法的规范
* 
*/
public interface JsonSession {
	/**
	 * 根据map参数查询并返回json数据
	 * @param sentence
	 * @param param
	 * @return
	 */
	String selectStrinByMap(Sentence sentence, Map<String, Object> param);
	
	/**
	 * 根据map参数查询并返回response对象
	 * @param sentence
	 * @param param
	 * @return
	 */
	SearchResponse selectResponseByMap(Sentence sentence, Map<String, Object> param);
	
	/**
	 * 根据实体查询并返回json字符串
	 * @param sentence
	 * @param obj
	 * @return
	 */
	String selectStringByObject(Sentence sentence, Object obj);
	
	/**
	 * 根据实体查询并返回response对象
	 * @param sentence
	 * @param obj
	 * @return
	 */
	SearchResponse selectResponseByObject(Sentence sentence, Object obj);
	
	/**
	 * 根据map更新并返回json字符串
	 * @param sentence
	 * @param param
	 * @return
	 */
	String updateStringByeMap(Sentence sentence, Map<String, Object> param);
	
	/**
	 * 根据map更新并返回response对象
	 * @param sentence
	 * @param param
	 * @return
	 */
	UpdateResponse updateResponseByMap(Sentence sentence, Map<String, Object> param);
	
	/**
	 * 根据实体更新并返回json字符串
	 * @param sentence
	 * @param obj
	 * @return
	 */
	String updateStringByObject(Sentence sentence, Object obj);
	
	/**
	 * 根据实体更新并返回response对象
	 * @param sentence
	 * @param obj
	 * @return
	 */
	UpdateResponse updateResponseByObject(Sentence sentence, Object obj);
	
	/**
	 * 根据map插入并返回json字符串
	 * @param sentence
	 * @param param
	 * @return
	 */
	String insertStringByMap(Sentence sentence, Map<String, Object> param);
	
	/**
	 * 根据map插入并返回response对象
	 * @param sentence
	 * @param param
	 * @return
	 */
	IndexResponse insertResponseByMap(Sentence sentence, Map<String, Object> param);
	
	/**
	 * 根据实体插入并返回json对象
	 * @param sentence
	 * @param obj
	 * @return
	 */
	String insertStringByObject(Sentence sentence, Object obj);
	
	/**
	 * 根据实体插入并返回response实体
	 * @param sentence
	 * @param obj
	 * @return
	 */
	IndexResponse indexResponseByObject(Sentence sentence, Object obj);
	
	/**
	 * 根据map删除并返回json字符串
	 * @param sentence
	 * @param param
	 * @return
	 */
	String deleteStringByMap(Sentence sentence, Map<String, Object> param);
	
	/**
	 * 根据map删除并返回response对象
	 * @param sentence
	 * @param param
	 * @return
	 */
	DeleteResponse deleteResponseByMap(Sentence sentence, Map<String, Object> param);
	
	/**
	 * 根据实体删除并返回json对象
	 * @param sentence
	 * @param obj
	 * @return
	 */
	String deleteStringByObject(Sentence sentence, Object obj);
	
	/**
	 * 根据实体删除并返回response实体
	 * @param sentence
	 * @param obj
	 * @return
	 */
	DeleteResponse deleteResponseByObject(Sentence sentence, Object obj);
}
