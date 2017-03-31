package com.fineShop.search.mapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	 * 根据被代理方法获取其配置信息
	 * @return
	 */
	public static Sentence getSentenceByMethod(Method method, XmlConfigure xmlConfigure){
		String methodName = method.getName();
		for (Sentence sentence : xmlConfigure.getSentences()) {
			if (methodName.equals(sentence.getMethodName())) {
				return sentence;
			}
		}
		return null;
	}
	
	/**
	 * 根据class解析其方法信息
	 * @return
	 */
	public static List<MethodInfo> getMethodInfoByClass(Class<?> clazz){
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
	 * 根据代理接口获取代理对象方法映射关系
	 * @return
	 */
	public Map<Method, MapperMethod>  getMapperMethod(List<MethodInfo> interfaceMethodInfos,
			List<MethodInfo> proxyMethodInfos, Map<Method, Sentence> sentenceCache){
		
		Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();
		
		for (MethodInfo methodInfo : interfaceMethodInfos) {
			for (MethodInfo proxyMethodInfo : proxyMethodInfos) {
				if (methodInfo.equals(proxyMethodInfo)) {
					MapperMethod mapperMethod = new MapperMethod(methodInfo.getMethod(), proxyMethodInfo.getMethod(), sentenceCache.get(methodInfo));
					methodCache.put(methodInfo.getMethod(), mapperMethod);
				}
			}
		}
		return methodCache;
	}
}
