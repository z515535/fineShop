package com.fineShop.search.spring;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.fineShop.search.proxy.ScanProxyAdapter;
import com.fineShop.search.proxy.generate.ScanGenerateProxyBean;
import com.fineShop.search.session.JsonSession;

/**
 * 代理注册管理器
* @author 作者 wugf:
* @version 创建时间：2017年3月25日 下午3:38:13<p>
* 类说明		将托管接口生成的代理对象注册到spring 容器中
* 
*/
public class RegistProxyManager implements BeanFactoryPostProcessor{

	private DefaultListableBeanFactory beanFactory;
	
	private ScanProxyAdapter scanProxyAdapter;
	private JsonSession jsonSession;
	private String packagePath;
	
	public RegistProxyManager(JsonSession jsonSession) {
		this.jsonSession = jsonSession;
		this.scanProxyAdapter = new ScanGenerateProxyBean(jsonSession);
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}
	
	/**
	 * 将扫描到的接口根据其生成的别名注册到beanFactory中
	 */
	public void registProxy(){
		Set<Map<String, Object>> proxys = scanProxyAdapter.ScanAndGenerateProxy(packagePath);
		for (Map<String, Object> proxy : proxys) {
			for (Map.Entry<String, Object> proxyEntry : proxy.entrySet()) {
				beanFactory.registerSingleton(proxyEntry.getKey(), proxyEntry.getValue());
			}
		}
	}
	
	public void setScanProxyAdapter(ScanProxyAdapter scanProxyAdapter) {
		this.scanProxyAdapter = scanProxyAdapter;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	public JsonSession getJsonSession() {
		return jsonSession;
	}

	public ScanProxyAdapter getScanProxyAdapter() {
		return scanProxyAdapter;
	}	
}
