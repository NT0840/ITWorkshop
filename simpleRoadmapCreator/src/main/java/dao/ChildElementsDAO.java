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

}
