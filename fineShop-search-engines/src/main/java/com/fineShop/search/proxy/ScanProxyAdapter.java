package com.fineShop.search.proxy;

import java.util.Map;
import java.util.Set;

import com.fineShop.search.mapper.XmlConfigure;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月25日 下午5:52:04<p>
* 类说明		代理接口扫描适配器
* 
*/
public interface ScanProxyAdapter {
	
	/**
	 * 扫描需被代理接口并返回代理别名和代理对象
	 * @param packagePath
	 * 				被代理接口包路径
	 * @param xmlConfiguresSet
	 * 				接口对应映射xml文件实体
	 * @return
	 */
	Set<Map<String, Object>> ScanAndGenerateProxy(String packagePath, Set<XmlConfigure> xmlConfiguresSet);
}
