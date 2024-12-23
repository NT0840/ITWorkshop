package model;

import java.io.Serializable;

public class ErrorMsg implements Serializable{
	private static final long serialVersionUID = 1L; // バージョン番号を設定
	private String fieldName;
	private String message;
	
	public ErrorMsg() { }
	public ErrorMsg(String fieldName, String message) {
		this.fieldName = fieldName;
		this.message = message;
	}
	
	public String getFieldName() { return fieldName; }
	public void setFieldName(String fieldName) { this.fieldName = fieldName; }
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
}
