package model;

import java.io.Serializable;

public class UpdateItem implements Serializable{
	private String itemName;
	private String itemContent;
	
	public UpdateItem() { }
	public UpdateItem(String itemName, String itemContent) {
		super();
		this.itemName = itemName;
		this.itemContent = itemContent;
	}
	
	public String getItemName() { return itemName; }
	public void setItemName(String itemName) { this.itemName = itemName; }
	public String getItemContent() { return itemContent; }
	public void setItemContent(String itemContent) { this.itemContent = itemContent; }
}
