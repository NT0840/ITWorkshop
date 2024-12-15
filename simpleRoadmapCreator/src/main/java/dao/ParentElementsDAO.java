package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import listener.ApplicationListener;
import model.ParentElement;
import model.RoadmapId;

public class ParentElementsDAO {
	
	public List<ParentElement> findByRoadmapId(RoadmapId roadmapId) {
		List<ParentElement> parentElements = new ArrayList<>();
		
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "SELECT * FROM PARENT_ELEMENTS WHERE USER_ID = ? AND ROADMAP_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, roadmapId.getUserId());
			pStmt.setInt(2, roadmapId.getRoadmapId());
			// SELECTを実行
			ResultSet rs = pStmt.executeQuery();
			
			while(rs.next()) {
				ParentElement parentElement = new ParentElement();
				parentElement.setUserId(rs.getString("USER_ID"));
				parentElement.setRoadmapId(rs.getInt("ROADMAP_ID"));
				parentElement.setParentNum(rs.getInt("PARENT_NUM"));
				parentElement.setParentName(rs.getString("PARENT_NAME"));
				parentElement.setParentStatusNum(rs.getInt("PARENT_STATUS_NUM"));
				parentElement.setParentDescription(rs.getString("PARENT_DESCRIPTION"));
				parentElement.setParentCreateAt(rs.getTimestamp("PARENT_CREATE_AT"));
				parentElement.setParentUpdateAt(rs.getTimestamp("PARENT_UPDATE_AT"));
				parentElements.add(parentElement);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return parentElements;
	}
}