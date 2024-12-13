package model;

import java.io.Serializable;

public class Email implements Serializable{
	private String email;

	public Email() { }
	public Email(String email) {
		super();
		this.email = email;
	}

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
}
