package com.fineShop.search.mapper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import com.fineShop.search.mapper.adapter.ScanXmlAdapter;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月26日 下午10:50:44<p>
* 类说明		扫描加载xml配置文件管理器
* 
*/
public class ScanXmlManager implements ScanXmlAdapter{
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	public Set<XmlConfigure> scanAndLoadConfig(String mapperLocations) {
		Set<String> pathSet = getAllXmlPath(mapperLocations);
		// TODO 解析
		return null;
	}

	/**
	 * 加载配置目录下的所有文件路径
	 * @return
	 */
	private Set<String> getAllXmlPath(String mapperLocations){
		Set<String> xmlPathSet = new LinkedHashSet<String>();
		try {
			Resource[] resources = this.resourcePatternResolver.getResources(mapperLocations);
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				String xmlPath = resource.getURI().getPath();
				xmlPathSet.add(xmlPath);
			}
		} catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		} 
		return xmlPathSet;
	}
}
