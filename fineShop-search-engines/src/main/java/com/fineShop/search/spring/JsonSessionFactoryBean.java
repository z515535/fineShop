package com.fineShop.search.spring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月22日 下午10:48:45<p>
* 类说明
* 
*/
public class JsonSessionFactoryBean implements InitializingBean,ApplicationListener<ApplicationEvent>{
	
	private static boolean isStart = false;
	private RegistProxyManager registProxyManager;
	private String packagePath;
	
	public void onApplicationEvent(ApplicationEvent event) {
		if (!isStart) {
			isStart = true;
			System.err.println("spring 启动");
			//加载配置文件
			
			//扫描代理接口并注册代理对象
			registProxyManager.setPackagePath(packagePath);
			registProxyManager.registProxy();
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
}
