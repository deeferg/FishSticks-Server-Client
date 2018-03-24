package dataaccesslayer;
/*
 * File : FishStickDaoImpl
 * Author : John Ferguson
 * Written : March 5th
 * Description : Complete implementation from FishStickDAO
 * 
 * */
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import datatransfer.FishStick;

public class FishStickDaoImpl  implements FishStickDao{
	
		public FishStick findByUUID(String uuid) throws SQLException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FishStick fishStick = null;
		try{
			DataSource source = new DataSource();
			conn = source.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM FishSticks WHERE uuid = ?");
			pstmt.setString(1, uuid);
			
			rs = pstmt.executeQuery();
			rs.next();
			fishStick = new FishStick();
			fishStick.setId(rs.getInt("id"));
			fishStick.setRecordNumber(rs.getInt("recordnumber"));
			fishStick.setOmega(rs.getString("omega"));
			fishStick.setLambda(rs.getString("lambda"));
			fishStick.setUUID(rs.getString("uuid"));
		}catch(SQLException e){
			System.out.println(e.getMessage());
			throw e;
		}finally{
			try{
				if(rs != null){
					rs.close();
				}
			}catch(SQLException ex){
				System.out.println(ex.getMessage());
			}
			try{
				if(pstmt != null){
					pstmt.close();
				}
			}catch(SQLException ex){
				System.out.println(ex.getMessage());
			}
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException ex){
				System.out.println(ex.getMessage());
			}
		}
		return fishStick;
		
		
	}

	public void insertFishStick(FishStick fishStick)throws SQLException{
		DataSource source = new DataSource();
		Connection conn = source.getConnection();
		PreparedStatement pstmt = null;
		try{
			pstmt = conn.prepareStatement(
					"INSERT INTO FishSticks (recordnumber, omega, lambda, uuid) " +
					"VALUES(?, ?, ?, ?)"
					);
					pstmt.setInt(1, fishStick.getRecordNumber());
					pstmt.setString(2, fishStick.getOmega());
					pstmt.setString(3,  fishStick.getLambda());
					pstmt.setString(4, fishStick.getUUID());
					pstmt.executeUpdate();
		}catch(SQLException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}finally{
			try{
				if(pstmt != null){
					pstmt.close();
				}
			}catch(SQLException ex){
				System.out.println(ex.getMessage());
			}
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException ex){
				System.out.println(ex.getMessage());
			}
		}
	}
}
