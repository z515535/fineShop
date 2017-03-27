package com.fineShop.search.mapper;
/**
* @author 作者 wugf:
* @version 创建时间：2017年3月23日 下午8:27:49<p>
* 类说明		此枚举定义操作es类型
* 
*/
public enum CommandType {
	select("select"),
    update("update"),
    insert("insert"),
    delete("selete");
	
	private String value;
	
	CommandType(String value){
		this.value = value;
	}
	
	CommandType(Integer index){
		this.value = formatString(index);
	}
	
	public static String formatString(Integer index){
		for (int i=0; i<CommandType.values().length; i++) {
			if (i == index) {
				return CommandType.values()[i].value;
			}
		}
		return null;
	}
	
	public static Integer formatIndex(String value){
		for (int i=0; i<CommandType.values().length; i++) {
			if (CommandType.values()[i].value.equals(value)) {
				return i;
			}
		}
		return -1;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
