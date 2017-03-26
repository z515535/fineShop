package com.fineShop.search.spring.adapter;

import java.util.Set;

import com.fineShop.search.mapper.XmlConfigure;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月26日 下午10:32:29<p>
* 类说明		xml资源加载适配器
* 
*/
public interface XmlResourceAdapter {
	/**
	 * 加载指定文件夹下的xml配置文件并转换成实体
	 * @return
	 */
	Set<XmlConfigure> loadXml2Entity();
}
