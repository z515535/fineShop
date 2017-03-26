package com.fineShop.test;

import java.lang.reflect.Method;

import com.fineShop.search.mapper.Sentence;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月25日 下午2:38:04<p>
* 类说明
* 
*/
public class Test {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		Method method = Sentence.class.getMethod("setType", String.class);
		System.out.println(method.getDeclaringClass());
	}
}
