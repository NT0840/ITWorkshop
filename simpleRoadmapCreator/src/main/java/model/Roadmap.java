package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Roadmap implements Serializable{
	private RoadmapId roadmapId;
	private ArrayList<ParentElement> parentElements;
	private ArrayList<ChildElement> childElements;
	
	public Roadmap() { }
	public Roadmap(RoadmapId roadmapId, ArrayList<ParentElement> parentElements,
			ArrayList<ChildElement> childElements) {
		super();
		this.roadmapId = roadmapId;
		this.parentElements = parentElements;
		this.childElements = childElements;
	}
	
	public RoadmapId getRoadmapId() {
		return roadmapId;
	}
	public void setRoadmapId(RoadmapId roadmapId) {
		this.roadmapId = roadmapId;
	}
	public List<ParentElement> getParentElements() {
		return new ArrayList<>(parentElements);
	}
	public void setParentElements(List<ParentElement> parentElements) {
		this.parentElements = new ArrayList<>(parentElements);
	}
	public List<ChildElement> getChildElements() {
		return new ArrayList<>(childElements);
	}
	public void setChildElements(List<ChildElement> childElements) {
		this.childElements = new ArrayList<>(childElements);
	}
}
