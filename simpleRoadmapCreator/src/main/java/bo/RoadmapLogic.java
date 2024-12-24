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
	
	public Roadmap findByRoadmapId(RoadmapId roadmapId) {
		// ロードマップIDに紐づくParentElementの取得(roadmapIdごと)
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		List<ParentElement> parentElements = parentElementsDao.findByRoadmapId(roadmapId);
		
		// ロードマップIDに紐づくChildElementの取得(roadmapIdごと)
		List<List<ChildElement>> childElements = new ArrayList<>();
		if(!parentElements.isEmpty()) {
			ChildElementsDAO childElementDao = new ChildElementsDAO();
			childElements = childElementDao.findByParentElements(parentElements);
		}
		
		// 戻り値とするroadmapに組み立てる
		Roadmap roadmap = new Roadmap(roadmapId, parentElements, childElements);
		
		return roadmap; 
	}
	
	public int findRoadmapIdMax(UserId userId) {
		// ユーザーIDに紐づくroadmapIdの最大値の取得
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		int roadmapIdMax = roadmapIdsDao.findRoadmapIdMaxByUserId(userId);
		
		return roadmapIdMax;
	}
	
	public int findParentNumMax(String userId, int roadmapId) {
		// ユーザーIDとroadmapIdに紐づくparentNumの最大値の取得
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		int parentNumMax = parentElementsDao.findParentNumMax(userId, roadmapId);
		
		return parentNumMax;
	}
	
	public int findChildNumMax(String userId, int roadmapId, int parentNum) {
		// ユーザーIDとroadmapIdとparentNumに紐づくchildNumの最大値の取得
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		int childNumMax = childElementsDao.findChildNumMax(userId, roadmapId, parentNum);
		
		return childNumMax;
	}
	
	public boolean createRoadmap(Roadmap roadmap) {
		// roadmapの新規作成
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean isRoadmapIdInsert = roadmapIdsDao.insert(roadmap.getRoadmapId());
		
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		Boolean isParentElementsInsert= parentElementsDao.insertByList(roadmap.getParentElements());
		
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean isChildElementsInsert = childElementsDao.insertByList(roadmap.getChildElements());
		
		return isRoadmapIdInsert && isParentElementsInsert && isChildElementsInsert; 
	}
	
	public boolean createParentElement(ParentElement parentElement) {
		// 親要素の新規作成時のデータベース保存操作
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		Boolean isParentElementsInsert= parentElementsDao.insert(parentElement);
		
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean isRoadmapIdUpdate = roadmapIdsDao.setUpdateAt(parentElement.getUserId(), parentElement.getRoadmapId());
		
		return isParentElementsInsert && isRoadmapIdUpdate; 
	}
	
	public boolean createChildElement(ChildElement childElement) {
		// 子要素の新規作成時のデータベース保存操作
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean isChildElementsInsert= childElementsDao.insert(childElement);
		
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean isRoadmapIdUpdate = roadmapIdsDao.setUpdateAt(childElement.getUserId(), childElement.getRoadmapId());
		
		return isChildElementsInsert && isRoadmapIdUpdate; 
	}
	
	public boolean updateParentElement(ParentElement parentElement) {
		// ParentElementの内容でデータベースを上書きする処理
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		Boolean isParentElementUpdate= parentElementsDao.updateWithoutParentNum(parentElement);
		
		// ROADMAP_IDテーブルにてUPDATE_ATを現在時刻で更新
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean roadmapIdUpdate= roadmapIdsDao.setUpdateAt(parentElement.getUserId(), parentElement.getRoadmapId());

		return isParentElementUpdate && roadmapIdUpdate; 
	}
	
	public boolean updateParentElementWithInsert(ParentElement parentElement, ParentElement currentParentElement) {
		// currentParentElementの内容をParentElementの内容に変更するデータベース処理
		// 変更前の要素の削除、挿入場所の確保、挿入処理
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		Boolean isParentElementDelete= parentElementsDao.delete(currentParentElement);
		Boolean isParentElementUpdate= parentElementsDao.updateAfterDelete(parentElement, currentParentElement);
		Boolean isParentElementInsert;
		if(currentParentElement.getParentNum() < parentElement.getParentNum()) {
			isParentElementInsert = parentElementsDao.insertBigger(parentElement);
		} else {
			isParentElementInsert = parentElementsDao.insert(parentElement);
		}

		Boolean isParentCheck = isParentElementDelete && isParentElementUpdate && isParentElementInsert;
		
		// 親要素に紐づく子要素が持つparentNumを変更(子要素を持つ親要素のみ)
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean isChildCheck = true;
		List<ChildElement> childElements = childElementsDao.findByParentElement(currentParentElement);
		
		if(childElements != null && !childElements.isEmpty()) {
			Boolean isChildElementsDelete = childElementsDao.deleteByParentElement(currentParentElement);
			Boolean isChildElementsUpdate = childElementsDao.updateAfterDeleteByParentElement(parentElement, currentParentElement);
			Boolean isChildElementsInsert;
			
			if(currentParentElement.getParentNum() < parentElement.getParentNum()) {
				isChildElementsInsert = childElementsDao.insertBiggerByParentElement(childElements, parentElement);
			} else {
				isChildElementsInsert = childElementsDao.insertSmallerByParentElement(childElements, parentElement);
			}
			
			isChildCheck = isChildElementsDelete && isChildElementsUpdate && isChildElementsInsert;
		} else {
			isChildCheck = childElementsDao.updateAfterDeleteByParentElement(parentElement, currentParentElement);
		}
		
		// ROADMAP_IDテーブルにてUPDATE_ATを現在時刻で更新
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean roadmapIdUpdate= roadmapIdsDao.setUpdateAt(parentElement.getUserId(), parentElement.getRoadmapId());

		return isParentCheck && isChildCheck && roadmapIdUpdate; 
	}
	
	public boolean updateChildElement(ChildElement childElement) {
		// ChildElementの内容でデータベースを上書きする処理
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean isChildElementUpdate= childElementsDao.updateWithoutChildNum(childElement);
		
		// ROADMAP_IDテーブルにてUPDATE_ATを現在時刻で更新
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean roadmapIdUpdate= roadmapIdsDao.setUpdateAt(childElement.getUserId(), childElement.getRoadmapId());

		return isChildElementUpdate && roadmapIdUpdate; 
	}
	
	public boolean updateChildElementWithInsert(ChildElement childElement, ChildElement currentChildElement) {
		// currentChildElementの内容をChildElementの内容に変更するデータベース処理
		// 変更前の要素の削除、挿入場所の確保、挿入処理
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean isChildElementDelete= childElementsDao.delete(currentChildElement);
		Boolean isChildElementUpdate= childElementsDao.updateAfterDelete(childElement, currentChildElement);
		Boolean isChildElementInsert;
		if(currentChildElement.getChildNum() < childElement.getChildNum()) {
			isChildElementInsert = childElementsDao.insertBigger(childElement);
		} else {
			isChildElementInsert = childElementsDao.insert(childElement);
		}

		Boolean isChildCheck = isChildElementDelete && isChildElementUpdate && isChildElementInsert;
		
		// ROADMAP_IDテーブルにてUPDATE_ATを現在時刻で更新
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean isRoadmapIdUpdate= roadmapIdsDao.setUpdateAt(childElement.getUserId(), childElement.getRoadmapId());

		return isChildCheck && isRoadmapIdUpdate; 
	}
	
	public boolean copyRoadmap(Roadmap roadmap) {
		// roadmapのコピー
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean isRoadmapIdInsert = roadmapIdsDao.insert(roadmap.getRoadmapId());
		
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		Boolean isParentElementInsert = parentElementsDao.insertByList(roadmap.getParentElements());
		
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean isChildElementInsert = childElementsDao.insertByList(roadmap.getChildElements());
		
		return isRoadmapIdInsert && isParentElementInsert && isChildElementInsert; 
	}
	
	public boolean deleteRoadmapByUserId(UserId userId) {
		// ユーザーIDに紐づくロードマップの削除
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean isRoadmapIdDelete = roadmapIdsDao.deleteByUserId(userId);
		
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		Boolean isParentElementDelete = parentElementsDao.deleteByUserId(userId);
		
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean isChildElementDelete = childElementsDao.deleteByUserId(userId);
		
		return isRoadmapIdDelete && isParentElementDelete && isChildElementDelete; 
	}
	
	public boolean deleteRoadmap(RoadmapId roadmapId) {
		// roadmapの削除
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean isRoadmapIdDelete = roadmapIdsDao.delete(roadmapId);
		Boolean isRoadmapIdUpdate = roadmapIdsDao.updateAfterDelete(roadmapId);
		Boolean isRoadmapIdCheck = isRoadmapIdDelete && isRoadmapIdUpdate;
		
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		Boolean isParentElementDelete = parentElementsDao.deleteByRoadmapId(roadmapId);
		Boolean isParentElementUpdate = parentElementsDao.updateAfterDeleteByRoadmapId(roadmapId);
		Boolean isParentCheck = isParentElementDelete && isParentElementUpdate;
		
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean isChildElementDelete = childElementsDao.deleteByRoadmapId(roadmapId);
		Boolean isChildElementUpdate = childElementsDao.updateAfterDeleteByRoadmapId(roadmapId);
		Boolean isChildCheck = isChildElementDelete && isChildElementUpdate;
		
		return isRoadmapIdCheck && isParentCheck && isChildCheck; 
	}
	
	public boolean deleteParentElement(ParentElement parentElement) {
		// 親要素の削除
		ParentElementsDAO parentElementsDao = new ParentElementsDAO();
		Boolean isParentElementDelete = parentElementsDao.delete(parentElement);
		Boolean isParentElementUpdate = parentElementsDao.updateAfterDelete(parentElement);
		Boolean isParentCheck = isParentElementDelete && isParentElementUpdate;
		
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean isChildElementDelete = childElementsDao.deleteByParentElement(parentElement);
		Boolean isChildElementUpdate = childElementsDao.updateAfterDeleteByParentElement(parentElement);
		Boolean isChildCheck = isChildElementDelete && isChildElementUpdate;

		// ROADMAP_IDテーブルにてUPDATE_ATを現在時刻で更新
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean isRoadmapIdUpdate= roadmapIdsDao.setUpdateAt(parentElement.getUserId(), parentElement.getRoadmapId());
		
		return isRoadmapIdUpdate && isParentCheck && isChildCheck; 
	}
	
	public boolean deleteChildElement(ChildElement childElement) {
		// 子要素の削除
		ChildElementsDAO childElementsDao = new ChildElementsDAO();
		Boolean isChildElementDelete = childElementsDao.delete(childElement);
		Boolean isChildElementUpdate = childElementsDao.updateAfterDelete(childElement);
		Boolean isChildCheck = isChildElementDelete && isChildElementUpdate;

		// ROADMAP_IDテーブルにてUPDATE_ATを現在時刻で更新
		RoadmapIdsDAO roadmapIdsDao = new RoadmapIdsDAO();
		Boolean isRoadmapIdUpdate= roadmapIdsDao.setUpdateAt(childElement.getUserId(), childElement.getRoadmapId());
		
		return isRoadmapIdUpdate && isChildCheck; 
	}
}

	

