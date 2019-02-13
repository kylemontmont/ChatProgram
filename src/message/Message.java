package message;

import java.io.Serializable;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;

public class Message implements Serializable{
	
	//Calendar timeSent;
	
	String username, msg;
	
	
	public Message(){
		//timeSent = Calendar.getInstance();
	}
	
	public Message(String username){
		this();
		this.username = username;
	}
	
	public Message(String username, String msg){
		this();
		
		this.username = username;
		this.msg = msg;	
	}
		
	public String getUsername(){
		return username;
	}
	
	public String getMsg(){
		return msg;
	}
	
	/*public String getTime(){
		return timeSent.get(Calendar.HOUR) + ":" + timeSent.get(Calendar.MINUTE);
	}
	
	public String getDate(){
		return (timeSent.get(Calendar.MONTH) + 1) + "/" + timeSent.get(Calendar.DAY_OF_MONTH) + "/" + timeSent.get(Calendar.YEAR);
	}*/
	
	public String toString(){
		return username + ": " + msg;
	}
	
}
