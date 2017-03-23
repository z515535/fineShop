package com.fineShop.search.session;

import com.fineShop.search.mapper.Sentence;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月23日 下午8:22:12<p>
* 类说明		引擎操作基本接口<p>
* 该接口作用是规范此引擎生成代理对象对外提供操作es公共方法的规范
* 
*/
public interface JsonSession {
	String search(Sentence sentence, Object param);
}
