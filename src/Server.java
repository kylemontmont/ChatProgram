import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import message.Message;
import message.MessageDisconnect;
import message.MessageRequest;
import message.Ping;


public class Server extends JFrame{
	
	ServerSocket serverSocket;
	
	int port = 7777;
	int id = 0;
	
	static JTextArea console = null;		
	//static Message distributeMessage = null;
	static Object distributeMessage = null;
	
	User[] users = new User[5];
	
	
	public Server() throws IOException{
		
		setupWindow();
		
		println("server started  ip: " + findIp() + ":" + port + "(this will change periodically)");
     	
    	handleNewConnections();
		
	}
	
	public void setupWindow(){
		
		setTitle("Server");
		setSize(300,300);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		console = new JTextArea();
		console.setEditable(false);
		
		JScrollPane scroll = new JScrollPane(console);
		
		add(scroll);
		
		setVisible(true);
		
	}
	
	public void handleNewConnections(){
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			println("port already in use");
		}
		
		while(true){ //wait for connections and give them a receive thread and a send thread
			
    		try{
				users[findUnusedIndex()] = new User(serverSocket.accept(), ++id);
				println("someone connected");
			}catch (IOException e){
				println("error accepting client");
			}
    		//user get a message sent before they connected
    		new Thread(		//send
            		new Runnable(){
            			public void run(){
            				
            				int myId = id;
            				Thread.currentThread().setName("Sender_" + myId);
            				
            				while(users[findIndex(myId)].checkConnected())
            					users[findIndex(myId)].send(distributeMessage);
            				
            				Thread.currentThread().interrupt();//disconnected
            			}
            		}).start();
        		println("Started sender thread");
    		
    		new Thread(		//Receive
    			new Runnable(){
    				public void run(){
    					
    					int myId = id;
    					Thread.currentThread().setName("Reciever_" + myId);
    					
    					while(users[findIndex(myId)].checkConnected()){
    						
    						Object in = users[findIndex(myId)].recive();
    						
    						if(in.getClass().equals(new MessageRequest().getClass())){//requests
    							if(in.toString().equals("users"))
    								users[findIndex(myId)].send(new MessageRequest(getConnectedUsers()));//
    						}else if(in.getClass().equals(new Ping().getClass())){//ping
    							users[findIndex(myId)].send(in);
    							println("ping");
    						}else{
    							distributeMessage = in;
    							println("distributing message");
    						}
    						
    					}
    					
    					distributeMessage = new MessageDisconnect(users[findIndex(myId)].getUsername()); //disconnected
    				}
    			}).start();
    		println("Started receiver thread");
    		  		
    	}
		
	}
	
	public String findIp(){
		String ip = null;
		
		try {
			ip = String.valueOf(InetAddress.getLocalHost());
			ip = ip.substring(ip.indexOf("/") + 1);
		} catch (UnknownHostException e1) {
			println("error getting ip");
		}
		
		return ip;
	}
	
	public int findUnusedIndex(){
		
		makeUnconnectedNull();
		
		for(int i = 0; i < users.length; i++)
			if(users[i] == null)
				return i;
		return -1;
		
	}
	
	public int findIndex(int id){
		
		int index = Arrays.binarySearch(users, new User(id), User.compareId);
		return index;
		
	}

	public String getConnectedUsers(){
		String s = "Users: ";
		for(User user : users)
			if(user != null)
				s += user.getUsername() + ", ";
		s = s.substring(0, s.length() - 2);
		return s;
	}
	
	public void makeUnconnectedNull(){
		for(int i = 0; i < users.length; i++)
			if(users[i] != null && !users[i].checkConnected()){
				users[i] = null;
			}
		Arrays.sort(users, User.compareId);
	}
	
	public void println(String s){
		System.out.println(s);
		console.append(s + "\n");
		console.setCaretPosition(console.getDocument().getLength());
	}
	
}




































