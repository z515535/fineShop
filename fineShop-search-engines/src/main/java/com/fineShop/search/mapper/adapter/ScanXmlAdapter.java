package com.fineShop.search.mapper.adapter;

import java.util.Set;

import com.fineShop.search.mapper.XmlConfigure;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月26日 下午10:46:23<p>
* 类说明		xml加载适配接口
* 
*/
public interface ScanXmlAdapter {
	/**
	 * 根据路径加载其文件夹下的所有xml文件并返回其封装对象
	 * @param mapperLocations
	 * @return
	 */
	Set<XmlConfigure> scanAndLoadConfig(String mapperLocations);
}
