package model;

import java.io.Serializable;

public class UserId implements Serializable{
	private String userId;

	public UserId() { }
	public UserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
}
