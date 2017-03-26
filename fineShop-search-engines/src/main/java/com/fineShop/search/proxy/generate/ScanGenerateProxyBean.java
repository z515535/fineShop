package com.fineShop.search.proxy.generate;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import com.fineShop.search.proxy.ScanProxyAdapter;
import com.fineShop.search.session.JsonSession;

/**
 * @author 作者 wugf:
 * @version 创建时间：2017年3月25日 下午5:56:54
 *          <p>
 *          类说明 代理类扫描器，用于扫描配置包下所有需要代理的接口class
 * 
 */
public class ScanGenerateProxyBean implements ScanProxyAdapter {
	protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
			this.resourcePatternResolver);

	private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

	private JsonSession jsonSession;
	
	private final Map<Class<?>, MapperProxyFactory<?>> proxyFactoryCache = new ConcurrentHashMap<Class<?>, MapperProxyFactory<?>>();

	public ScanGenerateProxyBean(JsonSession jsonSession) {
		this.jsonSession = jsonSession;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<Map<String, Object>> ScanAndGenerateProxy(String packagePath){
		Set<Class<?>> clazzs = getClassFile(packagePath);
		Set<Map<String, Object>> proxys = new HashSet<Map<String, Object>>(clazzs.size());
		
		if (CollectionUtils.isNotEmpty(clazzs)) {
			for (Class<?> mapperInterface : clazzs) {
				MapperProxyFactory<?> mapperProxyFactory = proxyFactoryCache.get(mapperInterface);
				if (mapperProxyFactory == null) {
					mapperProxyFactory = new MapperProxyFactory(mapperInterface);
					proxyFactoryCache.put(mapperInterface, mapperProxyFactory);
				}
				Map<String, Object> mapperProxy = new HashMap<String, Object>(1);
				mapperProxy.put(generateAlias(mapperInterface), mapperProxyFactory.newInstance(jsonSession));
				proxys.add(mapperProxy);
			}
		}
		return proxys;
	}

	/**
	 * 获取指定包下的接口class对象
	 * @param basePackage
	 * @return
	 */
	public Set<Class<?>> getClassFile(String basePackage) {
		Set<Class<?>> candidates = new LinkedHashSet<Class<?>>();
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(basePackage) + "/" + this.resourcePattern;
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
				String clazzName = metadataReader.getClassMetadata().getClassName();
				Class<?> clazz = Class.forName(clazzName);
				if (clazz.isInterface()) {
					candidates.add(clazz);
				}
			}
		} catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		} catch (ClassNotFoundException e) {
			throw new BeanDefinitionStoreException("Scan package err!", e);
		}
		return candidates;
	}
	
	/**
	 * 生成别名
	 * 规则 获取clazz SimpleName 转首字母小写
	 * @param clazz
	 * @return
	 */
	private String generateAlias(Class<?> clazz){
		StringBuilder sb = new StringBuilder(clazz.getSimpleName());
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}

	public Map<Class<?>, MapperProxyFactory<?>> getProxyFactoryCache() {
		return proxyFactoryCache;
	}
}
