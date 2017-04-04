package com.fineShop.search.proxy.generate;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.elasticsearch.index.mapper.MapperException;

import com.fineShop.search.mapper.Analysis;
import com.fineShop.search.mapper.MapperMethod;
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
	
	private static final long serialVersionUID = 1L;
	
	private final JsonSession jsonSession;
	private final Class<T> mapperInterface;
	private final Map<Method, Sentence> sentenceCache = new ConcurrentHashMap<Method, Sentence>();		//代理方法和xml配置映射关系
	private final Map<Method, MapperMethod> methodCache;	//被代理方法和底层具体实现方法映射关系
	private final XmlConfigure xmlConfigure;
	
	public MapperProxy(JsonSession jsonSession, Class<T> mapperInterface,XmlConfigure xmlConfigure, Map<Method, MapperMethod> methodCache) {
		this.jsonSession = jsonSession;
		this.xmlConfigure = xmlConfigure;
		this.mapperInterface = mapperInterface;
		this.methodCache = methodCache;
		this.methodCache.putAll(Analysis.getMethodCache(mapperInterface, xmlConfigure));
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			try{ 
				return method.invoke(this, args);
			} catch (Exception e) {
				// TODO
			}
		}
		
		return execute(method, args);
	}
	
	/**
	 * 执行代理
	 * @param invokeMethod
	 * @param args
	 * @return
	 */
	private Object execute(Method invokeMethod, Object[] args){
		MapperMethod mapperMethod = methodCache.get(invokeMethod);
		Object result = null;
		try {
			if (mapperMethod == null) {
				throw new MapperException("解析代理接口与被代理接口方法映射异常，请检查代理类的实现或被代理接口的参数与返回值");
			}
			
			String key = invokeMethod.getName();
			Sentence sentence = xmlConfigure.getSentences().get(key);
			List<Object> params = new ArrayList<Object>(args.length + 1);
			params.add(sentence);
			params.addAll(Arrays.asList(args));
			
			result =  mapperMethod.getProxyMethod().invoke(jsonSession, params.toArray());
		} catch (Exception e) {
			System.err.println("代理方法调用异常");
			e.printStackTrace();
		}
		
		return result;
	}

	public JsonSession getJsonSession() {
		return jsonSession;
	}

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}

	public Map<Method, Sentence> getSentenceCache() {
		return sentenceCache;
	}

	public Map<Method, MapperMethod> getMethodCache() {
		return methodCache;
	}

	public XmlConfigure getXmlConfigure() {
		return xmlConfigure;
	}
}
