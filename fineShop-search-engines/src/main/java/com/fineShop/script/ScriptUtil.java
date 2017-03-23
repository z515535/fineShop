package com.fineShop.script;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
* @author 作者 wugf:
* @version 创建时间：2017年3月9日 下午9:53:55<p>
* 类说明
* 
*/
public class ScriptUtil {
	private ScriptUtil(){}
	
	public static String getScript(String path){
		BufferedReader reader = null;
		StringBuilder script = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String line = null;
			script = new StringBuilder();
			while((line=reader.readLine()) != null){
				script.append(line);
				script.append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return script.toString();
	}
}
