package com.fineShop.entity;

import java.util.Map;


/**
* @author 作者 wugf:
* @version 创建时间：2017年3月25日 下午2:46:54<p>
* 类说明
* 
*/
public interface UserDao {
	public String getName(Map<String, Object> param);
	
	public String update(Map<String, Object> param);
}
