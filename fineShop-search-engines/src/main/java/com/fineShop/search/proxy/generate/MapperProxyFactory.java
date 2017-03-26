package com.fineShop.search.proxy.generate;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.fineShop.search.mapper.MapperMethod;
import com.fineShop.search.session.JsonSession;

/**
 * @author 作者 wugf:
 * @version 创建时间：2017年3月23日 下午9:42:06
 *          <p>
 *          类说明 Mapper代理工厂，为接口动态生成代理
 * 
 */
public class MapperProxyFactory<T> {
	private final Class<T> mapperInterface;
	private final Map<Method, MapperMethod> mapperCache = new ConcurrentHashMap<Method, MapperMethod>();

	public MapperProxyFactory(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	@SuppressWarnings("unchecked")
	protected T newInstance(MapperProxy<T> mapperProxy) {
		return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface },
				mapperProxy);
	}
	
	public T newInstance(JsonSession jsonSession){
		// TODO 解析当前代理接口和代理基类的关系,然后传入mapperCache
		MapperProxy<T> mapperProxy = new MapperProxy<T>(jsonSession, mapperInterface, mapperCache);
		return newInstance(mapperProxy);
	}

	public Map<Method, MapperMethod> getMapperCache() {
		return mapperCache;
	}

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}
	
}
