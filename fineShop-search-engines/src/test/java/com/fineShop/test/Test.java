package com.fineShop.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.fineShop.search.mapper.Analysis;
import com.fineShop.search.mapper.Sentence;
import com.fineShop.search.proxy.generate.MapperProxy;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月25日 下午2:38:04<p>
* 类说明
* 
*/
public class Test {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		Method method = Sentence.class.getMethod("setResource",String.class);
		/*System.out.println(method.getDeclaringClass());
		System.out.println(method.getName());*/
		System.out.println(Arrays.asList(method.getParameterTypes()));
		System.out.println(method.getReturnType());
		
		
		List<Class> list1 = new ArrayList<Class>();
		List<Class> list2 = new ArrayList<Class>();
		list1.add(MapperProxy.class);
		list2.add(MapperProxy.class);
		
		System.out.println(CollectionUtils.isEqualCollection(list1, list2));
		
		System.out.println(String.class == String.class);
	}
}
