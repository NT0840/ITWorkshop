package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class ChildElement implements Serializable {
	private static final long serialVersionUID = 1L; // バージョン番号を設定
	private String userId;
	private int roadmapId;
	private int parentNum;
	private int childNum;
	private String childName;
	private int childTagNum;
	private int childStatusNum;
	private String childDescription;
	private Timestamp childCreateAt;
	private Timestamp childUpdateAt;
	
	public ChildElement() { }
	public ChildElement(String userId, int roadmapId, int parentNum, int childNum, String childName, int childTagNum,
			int childStatusNum, String childDescription, Timestamp childCreateAt, Timestamp childUpdateAt) {
		super();
		this.userId = userId;
		this.roadmapId = roadmapId;
		this.parentNum = parentNum;
		this.childNum = childNum;
		this.childName = childName;
		this.childTagNum = childTagNum;
		this.childStatusNum = childStatusNum;
		this.childDescription = childDescription;
		this.childCreateAt = childCreateAt;
		this.childUpdateAt = childUpdateAt;
	}
	
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public int getRoadmapId() { return roadmapId; }
	public void setRoadmapId(int roadmapId) { this.roadmapId = roadmapId; }
	public int getParentNum() { return parentNum; }
	public void setParentNum(int parentNum) { this.parentNum = parentNum; }
	public int getChildNum() { return childNum; }
	public void setChildNum(int childNum) { this.childNum = childNum; }
	public String getChildName() { return childName; }
	public void setChildName(String childName) { this.childName = childName; }
	public int getChildTagNum() { return childTagNum; }
	public void setChildTagNum(int childTagNum) { this.childTagNum = childTagNum; }
	public int getChildStatusNum() { return childStatusNum; }
	public void setChildStatusNum(int childStatusNum) { this.childStatusNum = childStatusNum; }
	public String getChildDescription() { return childDescription; }
	public void setChildDescription(String childDescription) { this.childDescription = childDescription; }
	public Timestamp getChildCreateAt() { return childCreateAt; }
	public void setChildCreateAt(Timestamp childCreateAt) { this.childCreateAt = childCreateAt; }
	public Timestamp getChildUpdateAt() { return childUpdateAt; }
	public void setChildUpdateAt(Timestamp childUpdateAt) { this.childUpdateAt = childUpdateAt; }
}
