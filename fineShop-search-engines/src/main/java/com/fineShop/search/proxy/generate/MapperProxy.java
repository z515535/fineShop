package com.fineShop.search.proxy.generate;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fineShop.search.mapper.Analysis;
import com.fineShop.search.mapper.MapperMethod;
import com.fineShop.search.mapper.MethodInfo;
import com.fineShop.search.mapper.Sentence;
import com.fineShop.search.mapper.XmlConfigure;
import com.fineShop.search.session.JsonSession;

/**
 * @author 作者 wugf:
 * @version 创建时间：2017年3月25日 下午12:33:27
 *          <p>
 *          类说明
 * 
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {
	private final JsonSession jsonSession;
	private final Class<T> mapperInterface;
	private final Map<Method, Sentence> sentenceCache = new ConcurrentHashMap<Method, Sentence>();		//代理方法和xml配置映射关系
	private final Map<Method, MapperMethod> methodCache;	//被代理方法和底层具体实现方法映射关系
	private List<MethodInfo> proxyMethodInfos = Collections.synchronizedList(new ArrayList<MethodInfo>());	//基类方法信息
	private List<MethodInfo> interfaceMethodInfos = Collections.synchronizedList(new ArrayList<MethodInfo>());	//被代理接口的方法信息
	private final XmlConfigure xmlConfigure;
	
	public MapperProxy(JsonSession jsonSession, Class<T> mapperInterface,XmlConfigure xmlConfigure, Map<Method, MapperMethod> methodCache) {
		this.jsonSession = jsonSession;
		this.xmlConfigure = xmlConfigure;
		this.mapperInterface = mapperInterface;
		this.methodCache = methodCache;
		proxyMethodInfos.addAll(Analysis.getMethodInfoByClass(jsonSession.getClass()));
		interfaceMethodInfos.addAll(Analysis.getMethodInfoByClass(mapperInterface));
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			try{ 
				return method.invoke(this, args);
			} catch (Exception e) {
				// TODO
			}
		}
		
		//找出代理method和基类method的映射关系
		//TODO execute
		return "ok!";
	}
	
	private Object execute(Method invokeMethod){
		Sentence sentence = Analysis.getSentenceByMethod(invokeMethod, xmlConfigure);
		sentenceCache.put(invokeMethod, sentence);
		
		return null;
	}
}
