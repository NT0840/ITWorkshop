package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import listener.ApplicationListener;
import model.RoadmapId;
import model.UserId;

public class RoadmapIdsDAO {
	
	public List<RoadmapId> findByUserId(UserId userId) {
		List<RoadmapId> roadmapIds = new ArrayList<>();
		
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "SELECT * FROM ROADMAP_IDS WHERE USER_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, userId.getUserId());
			// SELECTを実行
			ResultSet rs = pStmt.executeQuery();
			
			while(rs.next()) {
				RoadmapId roadmapId = new RoadmapId();
				roadmapId.setUserId(rs.getString("USER_ID"));
				roadmapId.setRoadmapId(rs.getInt("ROADMAP_ID"));
				roadmapId.setRoadmapName(rs.getString("ROADMAP_NAME"));
				roadmapId.setRoadmapCreateAt(rs.getTimestamp("ROADMAP_CREATE_AT"));
				roadmapId.setRoadmapUpdateAt(rs.getTimestamp("ROADMAP_UPDATE_AT"));
				roadmapIds.add(roadmapId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return roadmapIds;
	}
}