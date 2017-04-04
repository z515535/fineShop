package com.fineShop.search.exception;
/**
* @author 作者 wugf:
* @version 创建时间：2017年4月2日 下午12:12:38<p>
* 类说明 : 出现此类错误引起的原因有 
* 	xml配置文件和接口方法名或数量不一致
* 	xml配置文件解析错误
* 
*/
public class MappingException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private String retCd ;  //异常对应的返回码
    private String msgDes;  //异常对应的描述信息
    
    public MappingException(){}
    
    public MappingException(String message){
    	super(message);
    	this.msgDes = message;
    }
    
    public MappingException(String retCd, String message){
    	super();
    	this.retCd = retCd;
    	this.retCd = message;
    }

	public String getRetCd() {
		return retCd;
	}

	public void setRetCd(String retCd) {
		this.retCd = retCd;
	}

	public String getMsgDes() {
		return msgDes;
	}

	public void setMsgDes(String msgDes) {
		this.msgDes = msgDes;
	}
    
}
