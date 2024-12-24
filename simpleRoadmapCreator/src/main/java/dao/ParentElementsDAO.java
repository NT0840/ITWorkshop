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
import model.UserId;

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
	
	public int findParentNumMax(String userId, int roadmapId) {
		int parentElementMax = 0;
		
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "SELECT MAX(PARENT_NUM) AS MAX_NUM FROM PARENT_ELEMENTS WHERE USER_ID = ? AND ROADMAP_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, userId);
			pStmt.setInt(2, roadmapId);
			// SELECTを実行
			ResultSet rs = pStmt.executeQuery();
			
			if(rs.next()) {
				parentElementMax = rs.getInt("MAX_NUM");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return parentElementMax;
	}

	public boolean updateAfterDelete(ParentElement parentElement, ParentElement currentParentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE PARENT_ELEMENTS SET PARENT_NUM = PARENT_NUM + ? WHERE PARENT_NUM >= ? AND PARENT_NUM < ? AND USER_ID = ? AND ROADMAP_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			// 挿入箇所に応じて他のデータのデータをずらす処理を変える
			if(currentParentElement.getParentNum() < parentElement.getParentNum()) {
				pStmt.setInt(1, -1);
				pStmt.setInt(2, currentParentElement.getParentNum());
				pStmt.setInt(3, parentElement.getParentNum());
			} else {
				pStmt.setInt(1, 1);
				pStmt.setInt(2, parentElement.getParentNum());
				pStmt.setInt(3, currentParentElement.getParentNum() + 1);
			}
			
			pStmt.setString(4, parentElement.getUserId());
			pStmt.setInt(5, parentElement.getRoadmapId());
			
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
	// オーバーロード
	public boolean updateAfterDelete(ParentElement parentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE PARENT_ELEMENTS SET PARENT_NUM = PARENT_NUM - 1 WHERE PARENT_NUM >= ? AND USER_ID = ? AND ROADMAP_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setInt(1, parentElement.getParentNum());
			pStmt.setString(2, parentElement.getUserId());
			pStmt.setInt(3, parentElement.getRoadmapId());
			
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
	
	public boolean updateAfterDeleteByRoadmapId(RoadmapId roadmapId) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE PARENT_ELEMENTS SET ROADMAP_ID = ROADMAP_ID - 1 WHERE ROADMAP_ID >= ? AND USER_ID = ?";
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
	
	public boolean insert(ParentElement parentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "INSERT INTO PARENT_ELEMENTS (USER_ID, ROADMAP_ID, PARENT_NUM, PARENT_NAME, PARENT_STATUS_NUM, PARENT_DESCRIPTION, PARENT_CREATE_AT, PARENT_UPDATE_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, parentElement.getUserId());
			pStmt.setInt(2, parentElement.getRoadmapId());
			pStmt.setInt(3, parentElement.getParentNum());
			pStmt.setString(4, parentElement.getParentName());
			pStmt.setInt(5, parentElement.getParentStatusNum());
			pStmt.setString(6, parentElement.getParentDescription());
			pStmt.setTimestamp(7, parentElement.getParentCreateAt());
			pStmt.setTimestamp(8, parentElement.getParentUpdateAt());
			
			// INSERTを実行、結果をリストで保存
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
	
	public boolean insertByList(List<ParentElement> parentElements) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "INSERT INTO PARENT_ELEMENTS (USER_ID, ROADMAP_ID, PARENT_NUM, PARENT_NAME, PARENT_STATUS_NUM, PARENT_DESCRIPTION, PARENT_CREATE_AT, PARENT_UPDATE_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			List<Boolean> results = new ArrayList<>();
			
			for(ParentElement parentElement : parentElements) {	
				pStmt.setString(1, parentElement.getUserId());
				pStmt.setInt(2, parentElement.getRoadmapId());
				pStmt.setInt(3, parentElement.getParentNum());
				pStmt.setString(4, parentElement.getParentName());
				pStmt.setInt(5, parentElement.getParentStatusNum());
				pStmt.setString(6, parentElement.getParentDescription());
				pStmt.setTimestamp(7, parentElement.getParentCreateAt());
				pStmt.setTimestamp(8, parentElement.getParentUpdateAt());
				
				// INSERTを実行、結果をリストで保存
				int result = pStmt.executeUpdate();
				if(result < 1) {
					results.add(false);
				} else {
					results.add(true);
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
	
	public boolean insertBigger(ParentElement parentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "INSERT INTO PARENT_ELEMENTS (USER_ID, ROADMAP_ID, PARENT_NUM, PARENT_NAME, PARENT_STATUS_NUM, PARENT_DESCRIPTION, PARENT_CREATE_AT, PARENT_UPDATE_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, parentElement.getUserId());
			pStmt.setInt(2, parentElement.getRoadmapId());
			pStmt.setInt(3, parentElement.getParentNum() - 1);
			pStmt.setString(4, parentElement.getParentName());
			pStmt.setInt(5, parentElement.getParentStatusNum());
			pStmt.setString(6, parentElement.getParentDescription());
			pStmt.setTimestamp(7, parentElement.getParentCreateAt());
			pStmt.setTimestamp(8, parentElement.getParentUpdateAt());
			
			// INSERTを実行、結果をリストで保存
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
	
	public boolean updateWithoutParentNum(ParentElement parentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE PARENT_ELEMENTS SET PARENT_NAME = ?, PARENT_STATUS_NUM = ?, PARENT_DESCRIPTION = ?, PARENT_UPDATE_AT = ? WHERE USER_ID = ? AND ROADMAP_ID = ? AND PARENT_NUM = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, parentElement.getParentName());
			pStmt.setInt(2, parentElement.getParentStatusNum());
			pStmt.setString(3, parentElement.getParentDescription());
			pStmt.setTimestamp(4, parentElement.getParentUpdateAt());
			pStmt.setString(5, parentElement.getUserId());
			pStmt.setInt(6, parentElement.getRoadmapId());
			pStmt.setInt(7, parentElement.getParentNum());
			
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
	
	public boolean delete(ParentElement parentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "DELETE FROM PARENT_ELEMENTS WHERE USER_ID = ? AND ROADMAP_ID = ? AND PARENT_NUM = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, parentElement.getUserId());
			pStmt.setInt(2, parentElement.getRoadmapId());
			pStmt.setInt(3, parentElement.getParentNum());
			
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
	
	public boolean deleteByRoadmapId(RoadmapId roadmapId) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "DELETE FROM PARENT_ELEMENTS WHERE USER_ID = ? AND ROADMAP_ID = ?";
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
			
			String sql = "DELETE FROM PARENT_ELEMENTS WHERE USER_ID = ?";
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



