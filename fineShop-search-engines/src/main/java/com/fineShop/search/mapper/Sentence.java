package com.fineShop.search.mapper;
/**
* @author 作者 wugf:
* @version 创建时间：2017年3月23日 下午8:32:49<p>
* 类说明		配置语句实体
* 
*/
public class Sentence {
	private CommandType actionType;
	private String methodName;
	private String index = "_all";
	private String type;
	private Class<?>[] paramsType;
	private Class<?> resultType;
	private String resource;
	
	public Sentence(){}

	public Sentence(CommandType actionType, String methodName, String index, String type, Class<?>[] paramsType,
			Class<?> resultType, String resource) {
		this.actionType = actionType;
		this.methodName = methodName;
		this.index = index;
		this.type = type;
		this.paramsType = paramsType;
		this.resultType = resultType;
		this.resource = resource;
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
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
