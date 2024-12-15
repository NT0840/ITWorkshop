package model;

import java.io.Serializable;

public class UserId implements Serializable{
	private static final long serialVersionUID = 1L; // バージョン番号を設定
	private String userId;

	public UserId() { }
	public UserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
}
