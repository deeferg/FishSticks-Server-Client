package dataaccesslayer;
/*
 * File : FishStickDao
 * Author : John Ferguson
 * Written : March 5th
 * Description : Interface for fishstick data object
 * 
 * */
import java.sql.SQLException;
//import java.util.List; // not needed for now
import datatransfer.FishStick; 
public interface FishStickDao {

	FishStick findByUUID(String uuid) throws SQLException;
	
	void insertFishStick(FishStick fishStick) throws SQLException;
	
}
