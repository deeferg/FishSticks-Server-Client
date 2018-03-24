package server;

import java.io.EOFException;

/*
 * File : FishStickServer
 * Author : John Ferguson
 * Written : March 5th
 * Description : A MultiThreaded Server for FishSticks
 * 
 * */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dataaccesslayer.FishStickDaoImpl;
import datatransfer.FishStick;
import datatransfer.Message;

public class FishStickServer {

	private ServerSocket server;
	private Socket conn;
	private int portNum = 8081;
	public static ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	public static void main(String[] args){
		
		if(args.length > 0){
			(new FishStickServer(Integer.parseInt(args[0]))).runServer();
		}
		else{
			(new FishStickServer(8081)).runServer();
		}
	}
	
	public FishStickServer(int portNum){
		this.portNum = portNum;
	}
	
	public void talkToClient(final Socket connection){
		threadExecutor.execute(new Runnable(){
			public void run(){
				ObjectOutputStream output = null;
				ObjectInputStream input = null;
				Message message;//TODO : make a fishstick
				FishStickDaoImpl fishStickDao = new FishStickDaoImpl();
				FishStick fishStick;
				System.out.println("Got a connection");
				try{
					SocketAddress remoteAddress = connection.getRemoteSocketAddress();
					String remote = remoteAddress.toString();
					output = new ObjectOutputStream(connection.getOutputStream());
					input = new ObjectInputStream(connection.getInputStream());
					do{
						
						message = (Message) input.readObject();
							
						if(message.getCommand().equalsIgnoreCase("disconnect") || message.getFishStick() == null){
							message.setFishStick(null);
							break;
						}
						else{
							System.out.println("From: " + remote + "Command: " + message.getCommand() + " FishStick: "  + message.getFishStick().getRecordNumber() + ", " + message.getFishStick().getId() + ", " + message.getFishStick().getOmega() + ", " + message.getFishStick().getUUID());
							
			
							if(message.getCommand().equalsIgnoreCase("add")){
								
								try {
									fishStickDao.insertFishStick(message.getFishStick());
									fishStick = fishStickDao.findByUUID(message.getFishStick().getUUID());
									message.setCommand("command_worked");
									message.setFishStick(fishStick);
									
								} catch (SQLException e) {
									System.out.println(e.getMessage());
									e.printStackTrace();
									message.setCommand("command_failed");
									message.setFishStick(null);
								}
							}
						}
						output.writeObject(message);
						output.flush();
					}while(message.getFishStick() != null || message.getCommand().equalsIgnoreCase("disconnect"));
					System.out.println(remote + " disconnected via request");
				}catch(IOException exception){
					System.err.println(exception.getMessage());
					exception.printStackTrace();
				}catch (ClassNotFoundException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				finally{
					try{
						if(input != null){
							input.close();
						}
					}catch(IOException e){
							System.out.println(e.getMessage());	
					}
					try{
						if(output != null){
							output.flush();
							output.close();
						}
					}catch(IOException e){
							System.out.println(e.getMessage());	
					}
					try{
						if(connection != null){
							connection.close();
						}
					}catch(IOException e){
						System.out.println(e.getMessage());
					}
				}
			
			}
		});
	}
	public void runServer(){
		try {
			server = new ServerSocket(portNum);
		}catch (IOException e){
			e.printStackTrace();
		}
		System.out.println("Listenting for connections...(By John Ferguson)");
		while(true){
			try{
				conn = server.accept();
				talkToClient(conn);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
	
}
