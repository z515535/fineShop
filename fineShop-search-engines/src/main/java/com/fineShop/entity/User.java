package com.fineShop.entity;

import java.io.Serializable;

/**
* @author 作者 wugf:
* @version 创建时间：2017年3月5日 下午9:19:10<p>
* 类说明		es 对应实体
* 
*/
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String username;
	
	private Long ststus;
	
	private String nickname;
	
	private String password;
	
	private Long version;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getStstus() {
		return ststus;
	}
	public void setStstus(Long ststus) {
		this.ststus = ststus;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
}
