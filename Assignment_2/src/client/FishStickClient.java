package client;

/*
 * File : FishStickClient
 * Author : John Ferguson
 * Written : March 5th
 * Description : Easy client for writing fishSticks
 * and reading from server
 * 
 * */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.UUID;

import datatransfer.FishStick;
import datatransfer.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FishStickClient {

	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Message message;
	private String serverName = "localhost";
	private int portNum = 8081;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args){
		
		switch(args.length){
		case 2:
			(new FishStickClient(args[1], Integer.parseInt(args[2]))).runClient();
			break;
		case 1:
			(new FishStickClient("localhost", Integer.parseInt(args[1]))).runClient();
			break;
		default:
			(new FishStickClient("localhost", 8081)).runClient();
		}
		
	}
	
	public FishStickClient(String serverName, int portNum){
		this.serverName = serverName;
		this.portNum = portNum;
	}
	
	public void runClient(){
		
		
		try{
			connection = new Socket(InetAddress.getByName(serverName), portNum);
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
			do{
				Message message = new Message();
				FishStick fishStick = new FishStick();
				System.out.println("Enter Data for new FishStick: ");
				System.out.println("Please Enter Record Number: ");
				//Set a random value for the ID here? Unsure if the database does it itself
				fishStick.setRecordNumber(Integer.parseInt(br.readLine()));
				System.out.println("Enter the Omega Value: ");
				fishStick.setOmega(br.readLine()); 
				System.out.println("Enter the Lambda Value: ");
				fishStick.setLambda(br.readLine());
				fishStick.setUUID(UUID.randomUUID().toString());
				message.setFishStick(fishStick);
				message.setCommand("add");
				output.writeObject(message);
				output.flush();
				message = (Message) input.readObject();//TODO Change to Message later
				if(message.getCommand().equalsIgnoreCase("command_worked")){
					System.out.println("Command: " + message.getCommand() + "Returned FishStick: " + message.getFishStick().getId() + ", " + message.getFishStick().getRecordNumber() + ", " + message.getFishStick().getOmega() + ", " + message.getFishStick().getLambda() + ", " + message.getFishStick().getUUID());
					System.out.print("Do you want to insert another FishStick?(y/n):");
					message.setCommand(br.readLine());
					if(message.getCommand().equalsIgnoreCase("y")){
						continue;
					}
					else{
						message.setCommand("disconnect");
						message.setFishStick(null);
						output.writeObject(message);
						break;
					}
				}
				if(message.getCommand().equalsIgnoreCase("command_failed")){
					System.out.println("Insert did not succeed into database. Disconnecting...");
					message.setCommand("disconnect");
					output.writeObject(message);
					continue;
				}
				if(message.getCommand().equalsIgnoreCase("disconnect")){
					System.out.println("Client Program Shutting Down...");
					input.close();
					output.flush();
					output.close();
					connection.close();
					break;
				}
				
			}while(true);
		}catch(IOException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}catch(ClassNotFoundException e2){
			System.out.println(e2.getMessage());
			e2.printStackTrace();
		}
		finally{
			try{
				if(input != null){
					input.close();
				}
			}catch(IOException ex){
				System.out.println(ex.getMessage());
			}
			try{
				if(output != null){
					output.flush();
					output.close();
				}
			}catch(IOException ex){
				System.out.println(ex.getMessage());
			}
			try{
				if(connection != null){
					connection.close();
				}
			}catch(IOException ex){
				System.out.println(ex.getMessage());
			}
		}
		
		
	}
	
}
