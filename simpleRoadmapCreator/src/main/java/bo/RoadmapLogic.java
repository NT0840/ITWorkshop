package bo;

import java.util.ArrayList;
import java.util.List;

import dao.ChildElementsDAO;
import dao.ParentElementsDAO;
import dao.RoadmapIdsDAO;
import model.ChildElement;
import model.ParentElement;
import model.Roadmap;
import model.RoadmapId;
import model.UserId;

public class RoadmapLogic {
	public List<Roadmap> findByUserId(UserId userId) {
		List<Roadmap> roadmaps = new ArrayList<>();
		
		// ユーザーIDに紐づくRoadmapIdの取得
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		List<RoadmapId> roadmapIds = roadmapIdsDao.findByUserId(userId);
		
		for(RoadmapId roadmapId : roadmapIds) {
			// ユーザーIDに紐づくParentElementの取得(roadmapIdごと)
			ParentElementsDAO parentElementsDao = new ParentElementsDAO();
			List<ParentElement> parentElements = parentElementsDao.findByRoadmapId(roadmapId);
			
			// ユーザーIDに紐づくChildElementの取得(roadmapIdごと)
			List<List<ChildElement>> childElements = new ArrayList<>();
			if(!parentElements.isEmpty()) {
				ChildElementsDAO childElementDao = new ChildElementsDAO();
				childElements = childElementDao.findByParentElements(parentElements);
			}
			
			// 戻り値とする配列roadmapsに追加
			Roadmap roadmap = new Roadmap(roadmapId, parentElements, childElements);
			roadmaps.add(roadmap);
		}
		
		return roadmaps; 
	}
	
	public int findRoadmapIdMax(UserId userId) {
		// ユーザーIDに紐づくRoadmapIdの最大値の取得
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		int roadmapIdMax = roadmapIdsDao.findRoadmapIdMaxByUserId(userId);
		
		return roadmapIdMax;
	}
	
	public boolean createRoadmap(Roadmap roadmap) {
		// ユーザーIDに紐づくRoadmapIdの最大値の取得
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean roadmapIdInsert = roadmapIdsDao.insert(roadmap.getRoadmapId());
		
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		Boolean parentElementsInsert= parentElementsDao.insertByList(roadmap.getParentElements());
		
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean childElementsInsert = childElementsDao.insertByList(roadmap.getChildElements());
		
		return roadmapIdInsert && parentElementsInsert && childElementsInsert; 
	}
}
