package model;

import java.io.Serializable;

public class Email implements Serializable{
	private static final long serialVersionUID = 1L; // バージョン番号を設定
	private String email;

	public Email() { }
	public Email(String email) {
		super();
		this.email = email;
	}

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
}
