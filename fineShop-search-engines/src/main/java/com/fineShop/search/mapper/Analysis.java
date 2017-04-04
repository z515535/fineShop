package com.fineShop.search.mapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.elasticsearch.index.mapper.MapperException;

import com.fineShop.search.session.JsonSession;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月31日 下午9:11:21<p>
* 类说明	 ： 该类主要分析配置文件和被代理接口方法的映射关系
* 以及代理方法和被代理方法的关联关系
* 
*/
public class Analysis {
	private Analysis(){}
	
	/**
	 * 根据class解析其方法信息
	 * @return
	 */
	private static List<MethodInfo> getMethodInfoByClass(Class<?> clazz){
		Method[] methods = clazz.getMethods();
		List<MethodInfo> methodInfos = null;
		
		if (methods.length > 0) {
			
			methodInfos = new ArrayList<MethodInfo>(methods.length);
			
			MethodInfo methodInfo = null;
			for (Method method : methods) {
				methodInfo = new MethodInfo();
				methodInfo.setMethod(method);
				
				Class<?>[] paramTyps = method.getParameterTypes();
				if (paramTyps.length > 0) {
					methodInfo.setParameterIsNull(false);
					methodInfo.setParameterTypes(Arrays.asList(paramTyps));
				} else {
					methodInfo.setParameterIsNull(true);
				}
				
				Class<?> returnType = method.getReturnType();
				if (returnType.getSimpleName().equals("void")) {
					methodInfo.setIsVoid(true);
				} else {
					methodInfo.setIsVoid(false);
					methodInfo.setReturnType(returnType);
				}
				
				methodInfos.add(methodInfo);
			}
		}
		
		return methodInfos;
	}
	
	/**
	 * 获取代理接口和被代理接口的映射
	 * @param mapperInterface
	 * 					被代理接口
	 * @param proxyInterface
	 * 					代理接口
	 * @param xmlConfigure
	 * 					mapping xml
	 * @return
	 */
	public static Map<Method, MapperMethod> getMethodCache(Class<?> mapperInterface, XmlConfigure xmlConfigure){
		
		List<MethodInfo> mapperMethodInfos = getMethodInfoByClass(mapperInterface);
		List<MethodInfo> proxyMethodInfos = getMethodInfoByClass(JsonSession.class);
		
		Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();
		
		for (MethodInfo methodInfo : mapperMethodInfos) {
			String key = methodInfo.getMethod().getName();
			Sentence sentence = xmlConfigure.getSentences().get(key);
			Integer actionType = sentence.getActionType();
			MapperMethod mapperMethod = null;
			
			mapperMethod = getProxyMetnod(actionType, xmlConfigure, methodInfo, proxyMethodInfos);
			methodCache.put(methodInfo.getMethod(), mapperMethod);
		}
		return methodCache;
	}
	
	/**
	 * 据被代理方法信息获取代理对象对应的方法
	 * @param actionType
	 * 				执行类型
	 * @param xmlConfigure
	 * 				xml配置文件
	 * @param methodInfo
	 * 				被代理方法信息
	 * @param proxyMethodInfos
	 * 				代理接口方法集合
	 * @return
	 */
	private static MapperMethod getProxyMetnod(Integer actionType,XmlConfigure xmlConfigure, 
						 MethodInfo methodInfo,List<MethodInfo> proxyMethodInfos) {
		
		String key = methodInfo.getMethod().getName();
		Sentence sentence = xmlConfigure.getSentences().get(key);
		
		for (MethodInfo proxyMethodInfo : proxyMethodInfos) {
			
			// 判断执行类型
			if (proxyMethodInfo.getMethod().getName().startsWith(CommandType.formatString(actionType))) {
				if (methodInfo.equals(proxyMethodInfo)) {
					if (sentence == null) {
						throw new MapperException(String.format("命名空间为%s的配置文件配置异常",xmlConfigure.getNamespace()));
					}
					MapperMethod mapperMethod = new MapperMethod(proxyMethodInfo.getMethod(), xmlConfigure.getSentences().get(key));
					return mapperMethod;
				}
			}
		}
		
		throw new MapperException(String.format("命名空间为%s的配置文件配置异常",xmlConfigure.getNamespace()));
	}
}
