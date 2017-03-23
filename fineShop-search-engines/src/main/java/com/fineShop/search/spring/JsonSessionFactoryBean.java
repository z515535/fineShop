package com.fineShop.search.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.fineShop.search.mapper.Sentence;
import com.fineShop.search.session.JsonSession;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月22日 下午10:48:45<p>
* 类说明
* 
*/
public class JsonSessionFactoryBean implements FactoryBean<JsonSession>,InitializingBean,ApplicationListener<ApplicationEvent>{
	
	private static boolean isStart = false;
	private JsonSession test = new Test();
	
	public JsonSession getObject() throws Exception {
		// TODO Auto-generated method stub
		return test;
	}

	public Class<? extends JsonSession> getObjectType() {
		return this.test == null ? Test.class : this.test.getClass();
	}

	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		// TODO Auto-generated method stub
		if (!isStart) {
			isStart = true;
			System.err.println("spring 启动");
		}
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	class Test implements JsonSession{

		public String search(Sentence sentence, Object param) {
			return "hello";
		}
		
	}

}
