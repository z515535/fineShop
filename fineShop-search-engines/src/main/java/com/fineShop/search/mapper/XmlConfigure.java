package com.fineShop.search.mapper;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月26日 下午10:35:11<p>
* 类说明		xml 配置文件对象
* 
*/
public class XmlConfigure {
	private String namespace;
	private String index;
	private String type;
	private Map<String, Sentence> Sentences;
	
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		if (StringUtils.isEmpty(index)) {
			this.index = "_all";
		} else {
			this.index = index;
		}
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, Sentence> getSentences() {
		return Sentences;
	}
	public void setSentences(Map<String, Sentence> sentences) {
		Sentences = sentences;
	}
}
