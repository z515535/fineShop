package com.fineShop.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fineShop.entity.UserDao;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月23日 下午10:40:12<p>
* 类说明
* 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/spring/spring-*.xml")
public class MapperTest {
	@Resource
	private UserDao userDao;
	@Test
	public void test(){
		System.out.println(userDao.getName());
	}
}
