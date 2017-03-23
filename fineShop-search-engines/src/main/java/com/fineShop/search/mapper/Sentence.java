package com.fineShop.search.mapper;
/**
* @author 作者 wugf:
* @version 创建时间：2017年3月23日 下午8:32:49<p>
* 类说明		语句实体
* 
*/
public class Sentence {
	private String resource;
	private String index = "_all";
	private String type;
	private CommandType actionType;
	private Class<?>[] paramsType;
	private Class<?> resultType;
	
	public Sentence(){}
	
	public Sentence(String resource, String index, String type, CommandType actionType, Class<?>[] paramsType,
			Class<?> resultType) {
		super();
		this.resource = resource;
		this.index = index;
		this.type = type;
		this.actionType = actionType;
		this.paramsType = paramsType;
		this.resultType = resultType;
	}

	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
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
	public CommandType getActionType() {
		return actionType;
	}
	public void setActionType(CommandType actionType) {
		this.actionType = actionType;
	}
	public Class<?>[] getParamsType() {
		return paramsType;
	}
	public void setParamsType(Class<?>[] paramsType) {
		this.paramsType = paramsType;
	}
	public Class<?> getResultType() {
		return resultType;
	}
	public void setResultType(Class<?> resultType) {
		this.resultType = resultType;
	}
}
