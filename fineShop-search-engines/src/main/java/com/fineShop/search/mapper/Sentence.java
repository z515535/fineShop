package com.fineShop.search.mapper;
/**
* @author 作者 wugf:
* @version 创建时间：2017年3月23日 下午8:32:49<p>
* 类说明		配置语句实体
* 
*/
public class Sentence {
	private Integer actionType;			//执行类型
	private String methodName;				//方法名称
	private String index;					//索引
	private String type;					//类型
	private String resource;				//执行语句
	
	public Sentence(){}

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
	public Integer getActionType() {
		return actionType;
	}
	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sentence other = (Sentence) obj;
		if (this.methodName.equals(other.getMethodName()))
			return true;
		return false;
	}
}
