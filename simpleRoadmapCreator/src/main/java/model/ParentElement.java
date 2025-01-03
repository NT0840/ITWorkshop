package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class ParentElement implements Serializable {
	private static final long serialVersionUID = 1L; // バージョン番号を設定
	private String userId;
	private int roadmapId;
	private int parentNum;
	private String parentName;
	private int parentStatusNum;
	private String parentDescription;
	private Timestamp parentCreateAt;
	private Timestamp parentUpdateAt;
	
	public ParentElement() { }
	public ParentElement(String userId, int roadmapId, int parentNum, String parentName, int parentStatusNum,
			String parentDescription, Timestamp parentCreateAt, Timestamp parentUpdateAt) {
		this.userId = userId;
		this.roadmapId = roadmapId;
		this.parentNum = parentNum;
		this.parentName = parentName;
		this.parentStatusNum = parentStatusNum;
		this.parentDescription = parentDescription;
		this.parentCreateAt = parentCreateAt;
		this.parentUpdateAt = parentUpdateAt;
	}
	
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public int getRoadmapId() { return roadmapId; }
	public void setRoadmapId(int roadmapId) { this.roadmapId = roadmapId; }
	public int getParentNum() { return parentNum; }
	public void setParentNum(int parentNum) { this.parentNum = parentNum; }
	public String getParentName() { return parentName; }
	public void setParentName(String parentName) { this.parentName = parentName; }
	public int getParentStatusNum() { return parentStatusNum; }
	public void setParentStatusNum(int parentStatusNum) { this.parentStatusNum = parentStatusNum; }
	public String getParentDescription() { return parentDescription; }
	public void setParentDescription(String parentDescription) { this.parentDescription = parentDescription; }
	public Timestamp getParentCreateAt() { return parentCreateAt; }
	public void setParentCreateAt(Timestamp parentCreateAt) { this.parentCreateAt = parentCreateAt; }
	public Timestamp getParentUpdateAt() { return parentUpdateAt; }
	public void setParentUpdateAt(Timestamp parentUpdateAt) { this.parentUpdateAt = parentUpdateAt; }
}
