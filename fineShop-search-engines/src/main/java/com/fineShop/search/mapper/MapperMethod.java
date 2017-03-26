package com.fineShop.search.mapper;

import java.lang.reflect.Method;

/**
 * @author 作者 wugf:
 * @version 创建时间：2017年3月23日 下午9:55:50
 *          <p>
 *          类说明 需要代理接口方法和基类方法的映射
 * 
 */
public class MapperMethod {
	private Method method;
	private String source;
	private Object[] params;

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}
}
