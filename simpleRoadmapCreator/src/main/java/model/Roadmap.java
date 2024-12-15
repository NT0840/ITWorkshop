package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Roadmap implements Serializable{
	private static final long serialVersionUID = 1L; // バージョン番号を設定
	private RoadmapId roadmapId;
	private List<ParentElement> parentElements;
	private List<List<ChildElement>> childElements; // 所属親要素番号、子要素番号で管理
	
	public Roadmap() { }
	public Roadmap(RoadmapId roadmapId, List<ParentElement> parentElements,
			List<List<ChildElement>> childElements) {
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
	public List<List<ChildElement>> getChildElements() {
	    List<List<ChildElement>> copy = new ArrayList<>();
	    for (List<ChildElement> childList : childElements) {
	        copy.add(new ArrayList<>(childList));
	    }
	    return copy;
	}
	public void setChildElements(List<List<ChildElement>> childElements) {
        this.childElements = new ArrayList<>();
        for (List<ChildElement> childList : childElements) {
            this.childElements.add(new ArrayList<>(childList));
        }
	}
}
