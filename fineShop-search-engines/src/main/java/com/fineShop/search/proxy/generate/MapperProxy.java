package com.fineShop.search.proxy.generate;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.fineShop.search.mapper.MapperMethod;
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
	private final Map<Method, MapperMethod> methodCache;
	
	public MapperProxy(JsonSession jsonSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
		this.jsonSession = jsonSession;
		this.mapperInterface = mapperInterface;
		this.methodCache = methodCache;
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
}
