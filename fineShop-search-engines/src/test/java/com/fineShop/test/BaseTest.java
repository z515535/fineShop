package com.fineShop.test;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.elasticsearch.common.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.annotation.Id;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fineShop.entity.UserDao;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月30日 下午11:06:09<p>
* 类说明
* 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/spring/spring-*.xml")
public class BaseTest {

	@Resource
	private UserDao userDao;
	
	@Test
	public void test(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("password", "test");
		System.out.println(userDao.getName( params));
	}
}
