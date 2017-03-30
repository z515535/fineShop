package com.fineShop.search.spring;

import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.fineShop.search.mapper.XmlConfigure;
import com.fineShop.search.mapper.adapter.ScanXmlAdapter;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月22日 下午10:48:45<p>
* 类说明
* 
*/
public class JsonSessionFactoryBean implements InitializingBean,ApplicationListener<ApplicationEvent>{
	
	private static boolean isStart = false;
	private RegistProxyManager registProxyManager;
	private ScanXmlAdapter scanXmlAdapter = new ScanXmlManager();
	private String packagePath;
	private String mapperLocations;
	
	public void onApplicationEvent(ApplicationEvent event) {
		if (!isStart) {
			isStart = true;
			System.err.println("spring 启动");
			//加载配置文件
			Set<XmlConfigure> xmlConfigureSet = scanXmlAdapter.scanAndLoadConfig(mapperLocations);
			
			//扫描代理接口并注册代理对象
			registProxyManager.setPackagePath(packagePath);
			registProxyManager.registProxy(xmlConfigureSet);
		}
	}
	
	//校验 是否已注入RegistProxyManager
	public void afterPropertiesSet() throws Exception {
		if (this.registProxyManager == null) {
			throw new RuntimeException("搜索框架启动错误，初始化注入RegistProxyManager异常!");
		}
			
	}

	public void setRegistProxyManager(RegistProxyManager registProxyManager) {
		this.registProxyManager = registProxyManager;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	public void setMapperLocations(String mapperLocations) {
		this.mapperLocations = mapperLocations;
	}
	
}
