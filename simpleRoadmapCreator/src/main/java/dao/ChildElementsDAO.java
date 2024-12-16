package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import listener.ApplicationListener;
import model.ChildElement;
import model.ParentElement;

public class ChildElementsDAO {
	
	public List<List<ChildElement>> findByParentElements(List<ParentElement> parentElements) {
		List<List<ChildElement>> childElements = new ArrayList<>();
		
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "SELECT * FROM CHILD_ELEMENTS WHERE USER_ID = ? AND ROADMAP_ID = ? AND PARENT_NUM = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			// roadmapIdごとの子要素を取得するため、ユーザーIDとroadmapIdはこの処理内では同一
			pStmt.setString(1, parentElements.get(0).getUserId());
			pStmt.setInt(2, parentElements.get(0).getRoadmapId());
			
			for(int i = 0; i < parentElements.size(); i++) {
				pStmt.setInt(3, parentElements.get(i).getParentNum());
				// SELECTを実行
				ResultSet rs = pStmt.executeQuery();
				
				List<ChildElement> innerChildElements = new ArrayList<>();
				
				while(rs.next()) {
					ChildElement childElement = new ChildElement();
					childElement.setUserId(rs.getString("USER_ID"));
					childElement.setRoadmapId(rs.getInt("ROADMAP_ID"));
					childElement.setParentNum(rs.getInt("PARENT_NUM"));
					childElement.setChildNum(rs.getInt("CHILD_NUM"));
					childElement.setChildName(rs.getString("CHILD_NAME"));
					childElement.setChildTagNum(rs.getInt("CHILD_TAG_NUM"));
					childElement.setChildStatusNum(rs.getInt("CHILD_STATUS_NUM"));
					childElement.setChildDescription(rs.getString("CHILD_DESCRIPTION"));
					childElement.setChildCreateAt(rs.getTimestamp("CHILD_CREATE_AT"));
					childElement.setChildUpdateAt(rs.getTimestamp("CHILD_UPDATE_AT"));
					innerChildElements.add(childElement);
				}
				childElements.add(innerChildElements);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return childElements;
	}
	
	public boolean insertByList(List<List<ChildElement>> childElements) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "INSERT INTO CHILD_ELEMENTS (USER_ID, ROADMAP_ID, PARENT_NUM, CHILD_NUM, CHILD_NAME, CHILD_TAG_NUM, CHILD_STATUS_NUM, CHILD_DESCRIPTION, CHILD_CREATE_AT, CHILD_UPDATE_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			List<Boolean> results = new ArrayList<>();
			
			for(List<ChildElement> innerChildElements : childElements) {
				for(ChildElement childElement : innerChildElements) {
					pStmt.setString(1, childElement.getUserId());
					pStmt.setInt(2, childElement.getRoadmapId());
					pStmt.setInt(3, childElement.getParentNum());
					pStmt.setInt(4, childElement.getChildNum());
					pStmt.setString(5, childElement.getChildName());
					pStmt.setInt(6, childElement.getChildTagNum());
					pStmt.setInt(7, childElement.getChildStatusNum());
					pStmt.setString(8, childElement.getChildDescription());
					pStmt.setTimestamp(9, childElement.getChildCreateAt());
					pStmt.setTimestamp(10, childElement.getChildUpdateAt());
					
					// INSERTを実行、結果をリストで保存
					int result = pStmt.executeUpdate();
					if(result < 1) {
						results.add(false);
					} else {
						results.add(true);
					}
				}
			}
			
			// 一つでもfalseならfalseを戻す
			for(Boolean result : results) {
				if(!result) {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
