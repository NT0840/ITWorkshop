package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

public class Account implements Serializable{
	private String userId;
	private String pass;
	private String email;
	private UUID uuid;
	private Boolean isVarified;
	private Timestamp createAt;
	
	public Account() { }
	public Account(String userId, String pass, String email, UUID uuid, Boolean isVarified, Timestamp createAt) {
		this.userId = userId;
		this.pass = pass;
		this.email = email;
		this.uuid = uuid;
		this.isVarified = isVarified;
		this.createAt = createAt;
	}
	
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public String getPass() { return pass; }
	public void setPass(String pass) { this.pass = pass; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public UUID getUuid() { return uuid; }
	public void setUuid(UUID uuid) { this.uuid = uuid; }
	public Boolean getIsVarified() { return isVarified; }
	public void setIsVarified(Boolean isVarified) { this.isVarified = isVarified; }
	public Timestamp getCreateAt() { return createAt; }
	public void setCreateAt(Timestamp createAt) { this.createAt = createAt; }
}
