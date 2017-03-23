package com.fineShop.search.session;

import java.util.List;

import com.fineShop.search.mapper.Sentence;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月23日 下午8:25:34<p>
* 类说明		此实体对应的是xml配置文件
* 
*/
public class Configuration {
	private String index;
	private String type;
	private Class<?> mapperInterface;
	private List<Sentence> sentences;
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Class<?> getMapperInterface() {
		return mapperInterface;
	}
	public void setMapperInterface(Class<?> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}
	public List<Sentence> getSentences() {
		return sentences;
	}
	public void setSentences(List<Sentence> sentences) {
		this.sentences = sentences;
	}
}
