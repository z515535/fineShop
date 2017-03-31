package com.fineShop.search.mapper;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;


/**
* @author 作者 wugf:
* @version 创建时间：2017年3月31日 下午9:47:56<p>
* 类说明	 : 方法对象信息
* 
*/
public class MethodInfo {
	/**
	 * 参数类型
	 */
	private List<Class<?>> parameterTypes;
	
	/**
	 * 方法
	 */
	private Method method;
	
	/**
	 * 返回类型
	 */
	private Class<?> returnType;
	
	/**
	 * 参数是否为空
	 */
	private Boolean parameterIsNull;
	
	/**
	 * 返回值是否为空
	 */
	private Boolean isVoid;
	
	public MethodInfo(){}
	
	public List<Class<?>> getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(List<Class<?>> parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Boolean getParameterIsNull() {
		return parameterIsNull;
	}

	public void setParameterIsNull(Boolean parameterIsNull) {
		this.parameterIsNull = parameterIsNull;
	}

	public Boolean getIsVoid() {
		return isVoid;
	}

	public void setIsVoid(Boolean isVoid) {
		this.isVoid = isVoid;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		MethodInfo other = (MethodInfo) obj;
		if (CollectionUtils.isEqualCollection(this.parameterTypes, other.getParameterTypes()) &&
				this.parameterIsNull == other.getParameterIsNull() &&
				this.returnType == other.getReturnType() &&
				this.isVoid == other.getIsVoid()) {
			return true;
		}
		return false;
	}
	
 }
