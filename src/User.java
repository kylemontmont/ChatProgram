import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;

import message.Message;
import message.MessageConnect;
import message.MessageNameChange;


public class User implements Comparable{
	
	//user info
	private String username;
	private int id;
	
	private ArrayList<Object> sentMessages = new ArrayList<Object>();
	
	//connection info
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private boolean connected = true;
	
	//constructors
	public User(){
		sentMessages.add(null);
	}
	
	public User(int id){
		this.id = id;
	}
	
	public User(Socket socket){
		sentMessages.add(null);
		this.socket = socket;
		setupInOut();
	}
	
	public User(Socket socket, int id){
		this(socket);
		this.id = id;
	}
	
	public User(Socket socket, String username, int id){
		this(socket, id);
		this.username = username;
	}
	
	//networking
	public void setupInOut(){
		
		try {
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			error("trying to create object input string");
		}
		
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			error("trying to create object output string");
		}
		
	}
	
	public void disconnect(){
		//System.out.println("disconnecting");
		connected = false;
		
		try {
			socket.close();
		} catch (IOException e) {};
		
	}
	
	public boolean checkConnected(){
		if(!connected)
			System.out.print("connected false");
		return connected;
	}
	
    public Object recive(){
    	Object msg = null;
		try {
			msg = in.readObject();
			System.out.println(in);
		} catch (ClassNotFoundException | IOException e){
			error("reciving message");
			disconnect();
		}
		System.out.println(msg.toString());
		
		//if(msg != null){
			switch(msg.getClass().toString()){//should be ordered from most common to least common
				case "message.MessageConnect" :
					username = ((Message)msg).getUsername();
					break;
				case "message.MessageNameChange" :
					username = ((MessageNameChange) msg).getNewUsername();
					break;
				default : ;
				break;
			}
		
			/*if(msg.getClass().equals(new MessageConnect().getClass()))//get username from connection
				username = ((MessageConnect)msg).getUsername();
			else if(msg.getClass().equals(new MessageNameChange().getClass()))//get username from name change
				username = ((MessageNameChange) msg).getNewUsername();*/
				
		//}
			
		return msg;
	}
    
    public void send(Object msg){
		if(!sentMessages.contains(msg)){
			try {
				out.writeObject(msg);
				sentMessages.add(msg);
			} catch (IOException e) {
				error("sending message");
				disconnect();
			}
		}
	
		try {
			Thread.currentThread().sleep(50);
		} catch (InterruptedException e){}
		
}
	
	//set
	public void setUsername(String username){
		this.username = username;
	}
	
	public void setId(int id){
		this.id = id;
	}
		
	public void setSocket(Socket socket){
		this.socket = socket;
	}
	
	//get
	public String getUsername(){
		return username;
	}

	public int getId(){
		return id;
	}	
	
	public Socket getSocket(){
		return socket;
	}
	
	//other
	public static final Comparator<User> compareId = new Comparator<User>(){

        @Override
        public int compare(User one, User two) {
        	
    		if(one == null && two == null)
    			return 0;
    		else if(one == null)
    			return 1;
    		else if(two == null)
				return -1;
			
    		return one.getId() - two.getId();
        }
      
    };

	public int compareTo(Object obj) {
		User other = (User) obj;
		return getId() - other.getId();
	}
	
	public void error(String errorMsg){
		errorMsg = "Error " + errorMsg;
		System.out.println(errorMsg);
	}
	
	public String toString(){
		//return id + "";
		return username + " " + id;
	}
	
}























