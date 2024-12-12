package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class RoadmapId implements Serializable{
	private String userId;
	private int roadmapId;
	private String roadmapName;
	private Timestamp roadmapCreateAt;
	private Timestamp roadmapUpdateAt;
	
	public RoadmapId() { }
	public RoadmapId(String userId, int roadmapId, String roadmapName, Timestamp roadmapCreateAt, Timestamp roadmapUpdateAt) {
		this.userId = userId;
		this.roadmapId = roadmapId;
		this.roadmapName = roadmapName;
		this.roadmapCreateAt = roadmapCreateAt;
		this.roadmapUpdateAt = roadmapUpdateAt;
	}
	
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public int getRoadmapId() { return roadmapId; }
	public void setRoadmapId(int roadmapId) { this.roadmapId = roadmapId; }
	public String getRoadmapName() { return roadmapName; }
	public void setRoadmapName(String roadmapName) { this.roadmapName = roadmapName; }
	public Timestamp getCreateAt() { return roadmapCreateAt; }
	public void setCreateAt(Timestamp roadmapCreateAt) { this.roadmapCreateAt = roadmapCreateAt; }
	public Timestamp getUpdateAt() { return roadmapUpdateAt; }
	public void setUpdateAt(Timestamp roadmapUpdateAt) { this.roadmapUpdateAt = roadmapUpdateAt; }
}
