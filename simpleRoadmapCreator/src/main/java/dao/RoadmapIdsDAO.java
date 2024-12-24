package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
	
	public int findRoadmapIdMaxByUserId(UserId userId) {
		int roadmapIdMax = 0;
		
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "SELECT MAX(ROADMAP_ID) AS MAX_ID FROM ROADMAP_IDS WHERE USER_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, userId.getUserId());
			// SELECTを実行
			ResultSet rs = pStmt.executeQuery();
			
			if(rs.next()) {
				roadmapIdMax = rs.getInt("MAX_ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return roadmapIdMax;
	}
	
	public boolean insert(RoadmapId roadmapId) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "INSERT INTO ROADMAP_IDS (USER_ID, ROADMAP_ID, ROADMAP_NAME, ROADMAP_CREATE_AT, ROADMAP_UPDATE_AT) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, roadmapId.getUserId());
			pStmt.setInt(2, roadmapId.getRoadmapId());
			pStmt.setString(3, roadmapId.getRoadmapName());
			pStmt.setTimestamp(4, roadmapId.getRoadmapCreateAt());
			pStmt.setTimestamp(5, roadmapId.getRoadmapUpdateAt());
			
			// INSERTを実行
			int result = pStmt.executeUpdate();
			
			if(result < 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateAfterDelete(RoadmapId roadmapId) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE ROADMAP_IDS SET ROADMAP_ID = ROADMAP_ID - 1 WHERE ROADMAP_ID >= ? AND USER_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setInt(1, roadmapId.getRoadmapId());
			pStmt.setString(2, roadmapId.getUserId());
			
			// UPDATEを実行、結果を保存
			int result = pStmt.executeUpdate();
			if(result < 1) {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean setUpdateAt(String userId, int roadmapId) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE ROADMAP_IDS SET ROADMAP_UPDATE_AT = ? WHERE USER_ID = ? AND ROADMAP_ID = ? ";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			// 更新日時セット用の現在時刻
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			
			pStmt.setTimestamp(1, now);
			pStmt.setString(2, userId);
			pStmt.setInt(3, roadmapId);
			
			// UPDATEを実行
			int result = pStmt.executeUpdate();
			
			if(result < 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public boolean delete(RoadmapId roadmapId) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "DELETE FROM ROADMAP_IDS WHERE USER_ID = ? AND ROADMAP_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, roadmapId.getUserId());
			pStmt.setInt(2, roadmapId.getRoadmapId());
			
			// DELETEを実行、結果を保存
			int result = pStmt.executeUpdate();
			if(result < 1) {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteByUserId(UserId userId) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "DELETE FROM ROADMAP_IDS WHERE USER_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, userId.getUserId());
			
			// DELETEを実行、結果を保存
			int result = pStmt.executeUpdate();
			if(result < 1) {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
