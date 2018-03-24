package datatransfer;

/*
 * File : Message
 * Author : John Ferguson
 * Written : March 5th
 * Description : A Serializable container for FishSticks
 * and their commands
 * 
 * */

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private String command;
	private FishStick fishStick;
	
	
	public Message(){
		
	}
	
	public Message(String command){
		this.command = command;
	}
	
	public Message(String command, FishStick fishStick){
		this.command = command;
		this.fishStick = fishStick;
	}
	
	public String getCommand(){
		return command;
	}
	
	public void setCommand(String command){
		this.command = command;
	}
	
	public FishStick getFishStick(){
		return fishStick;
	}
	
	public void setFishStick(FishStick fishStick){
		this.fishStick = fishStick;
	}
	
	
}
