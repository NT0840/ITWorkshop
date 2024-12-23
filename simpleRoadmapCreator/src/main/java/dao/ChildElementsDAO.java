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
import model.RoadmapId;

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
	
	public int findChildNumMax(String userId, int roadmapId, int parentNum) {
		int childElementMax = 0;
		
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "SELECT MAX(CHILD_NUM) AS MAX_NUM FROM CHILD_ELEMENTS WHERE USER_ID = ? AND ROADMAP_ID = ? AND PARENT_NUM = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, userId);
			pStmt.setInt(2, roadmapId);
			pStmt.setInt(3, parentNum);
			// SELECTを実行
			ResultSet rs = pStmt.executeQuery();
			
			if(rs.next()) {
				childElementMax = rs.getInt("MAX_NUM");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return childElementMax;
	}
	
	public List<ChildElement> findByParentElement(ParentElement parentElement) {
		List<ChildElement> childElements = new ArrayList<>();
		
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "SELECT * FROM CHILD_ELEMENTS WHERE USER_ID = ? AND ROADMAP_ID = ? AND PARENT_NUM = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, parentElement.getUserId());
			pStmt.setInt(2, parentElement.getRoadmapId());
			pStmt.setInt(3, parentElement.getParentNum());

			ResultSet rs = pStmt.executeQuery();
				
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
				
				childElements.add(childElement);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return childElements;
	}
	
	public boolean updateAfterDelete(ChildElement childElement, ChildElement currentChildElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE CHILD_ELEMENTS SET CHILD_NUM = CHILD_NUM + ? WHERE CHILD_NUM >= ? AND CHILD_NUM < ? AND USER_ID = ? AND ROADMAP_ID = ? AND PARENT_NUM = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			// 挿入箇所に応じて他のデータのデータをずらす処理を変える
			if(currentChildElement.getChildNum() < childElement.getChildNum()) {
				pStmt.setInt(1, -1);
				pStmt.setInt(2, currentChildElement.getChildNum());
				pStmt.setInt(3, childElement.getChildNum());
			} else {
				pStmt.setInt(1, 1);
				pStmt.setInt(2, childElement.getChildNum());
				pStmt.setInt(3, currentChildElement.getChildNum() + 1);
			}
			
			pStmt.setString(4, childElement.getUserId());
			pStmt.setInt(5, childElement.getRoadmapId());
			pStmt.setInt(6, childElement.getParentNum());
			
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
	public boolean updateAfterDelete(ChildElement childElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE CHILD_ELEMENTS SET CHILD_NUM = CHILD_NUM - 1 WHERE CHILD_NUM >= ? AND USER_ID = ? AND ROADMAP_ID = ? AND PARENT_NUM = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setInt(1, childElement.getChildNum());
			pStmt.setString(2, childElement.getUserId());
			pStmt.setInt(3, childElement.getRoadmapId());
			pStmt.setInt(4, childElement.getParentNum());
			
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
	
	public boolean updateAfterDeleteByParentElement(ParentElement parentElement, ParentElement currentParentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE CHILD_ELEMENTS SET PARENT_NUM = PARENT_NUM + ? WHERE PARENT_NUM >= ? AND PARENT_NUM < ? AND USER_ID = ? AND ROADMAP_ID = ?";
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
	public boolean updateAfterDeleteByParentElement(ParentElement parentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE CHILD_ELEMENTS SET PARENT_NUM = PARENT_NUM - 1 WHERE PARENT_NUM >= ? AND USER_ID = ? AND ROADMAP_ID = ?";
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
			
			String sql = "UPDATE CHILD_ELEMENTS SET ROADMAP_ID = ROADMAP_ID - 1 WHERE ROADMAP_ID >= ? AND USER_ID = ?";
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
	
	public boolean updateWithoutChildNum(ChildElement childElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "UPDATE CHILD_ELEMENTS SET CHILD_NAME = ?, CHILD_TAG_NUM = ?, CHILD_STATUS_NUM = ?, CHILD_DESCRIPTION = ?, CHILD_UPDATE_AT = ? WHERE USER_ID = ? AND ROADMAP_ID = ? AND PARENT_NUM = ? AND CHILD_NUM = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, childElement.getChildName());
			pStmt.setInt(2, childElement.getChildTagNum());
			pStmt.setInt(3, childElement.getChildStatusNum());
			pStmt.setString(4, childElement.getChildDescription());
			pStmt.setTimestamp(5, childElement.getChildUpdateAt());
			pStmt.setString(6, childElement.getUserId());
			pStmt.setInt(7, childElement.getRoadmapId());
			pStmt.setInt(8, childElement.getParentNum());
			pStmt.setInt(9, childElement.getChildNum());
			
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
	
	public boolean insert(ChildElement childElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "INSERT INTO CHILD_ELEMENTS (USER_ID, ROADMAP_ID, PARENT_NUM, CHILD_NUM, CHILD_NAME, CHILD_TAG_NUM, CHILD_STATUS_NUM, CHILD_DESCRIPTION, CHILD_CREATE_AT, CHILD_UPDATE_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
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
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
	
	public boolean insertBigger(ChildElement childElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "INSERT INTO CHILD_ELEMENTS (USER_ID, ROADMAP_ID, PARENT_NUM, CHILD_NUM, CHILD_NAME, CHILD_TAG_NUM, CHILD_STATUS_NUM, CHILD_DESCRIPTION, CHILD_CREATE_AT, CHILD_UPDATE_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, childElement.getUserId());
			pStmt.setInt(2, childElement.getRoadmapId());
			pStmt.setInt(3, childElement.getParentNum());
			pStmt.setInt(4, childElement.getChildNum() - 1);
			pStmt.setString(5, childElement.getChildName());
			pStmt.setInt(6, childElement.getChildTagNum());
			pStmt.setInt(7, childElement.getChildStatusNum());
			pStmt.setString(8, childElement.getChildDescription());
			pStmt.setTimestamp(9, childElement.getChildCreateAt());
			pStmt.setTimestamp(10, childElement.getChildUpdateAt());
			
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
	
	public boolean insertBiggerByParentElement(List<ChildElement> childElements, ParentElement parentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "INSERT INTO CHILD_ELEMENTS (USER_ID, ROADMAP_ID, PARENT_NUM, CHILD_NUM, CHILD_NAME, CHILD_TAG_NUM, CHILD_STATUS_NUM, CHILD_DESCRIPTION, CHILD_CREATE_AT, CHILD_UPDATE_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			List<Boolean> results = new ArrayList<>();
			
			for(ChildElement childElement : childElements) {
				pStmt.setString(1, childElement.getUserId());
				pStmt.setInt(2, childElement.getRoadmapId());
				pStmt.setInt(3, parentElement.getParentNum() - 1);
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
	
	public boolean insertSmallerByParentElement(List<ChildElement> childElements, ParentElement parentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "INSERT INTO CHILD_ELEMENTS (USER_ID, ROADMAP_ID, PARENT_NUM, CHILD_NUM, CHILD_NAME, CHILD_TAG_NUM, CHILD_STATUS_NUM, CHILD_DESCRIPTION, CHILD_CREATE_AT, CHILD_UPDATE_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			List<Boolean> results = new ArrayList<>();
			
			for(ChildElement childElement : childElements) {
				pStmt.setString(1, childElement.getUserId());
				pStmt.setInt(2, childElement.getRoadmapId());
				pStmt.setInt(3, parentElement.getParentNum());
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
	
	public boolean delete(ChildElement childElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "DELETE FROM CHILD_ELEMENTS WHERE USER_ID = ? AND ROADMAP_ID = ? AND PARENT_NUM = ? AND CHILD_NUM = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, childElement.getUserId());
			pStmt.setInt(2, childElement.getRoadmapId());
			pStmt.setInt(3, childElement.getParentNum());
			pStmt.setInt(4, childElement.getChildNum());
			
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
	
	public boolean deleteByParentElement(ParentElement parentElement) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "DELETE FROM CHILD_ELEMENTS WHERE USER_ID = ? AND ROADMAP_ID = ? AND PARENT_NUM = ?";
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
			
			String sql = "DELETE FROM CHILD_ELEMENTS WHERE USER_ID = ? AND ROADMAP_ID = ?";
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

}
