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
	private Method proxyMethod;
	private Sentence sentence;
	
	public MapperMethod(Method proxyMethod, Sentence sentence) {
		this.proxyMethod = proxyMethod;
		this.sentence = sentence;
	}

	public Sentence getSentence() {
		return sentence;
	}

	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	public Method getProxyMethod() {
		return proxyMethod;
	}

	public void setProxyMethod(Method proxyMethod) {
		this.proxyMethod = proxyMethod;
	}
	
}
