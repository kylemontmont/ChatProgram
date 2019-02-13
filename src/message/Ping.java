package message;

import java.io.Serializable;

public class Ping implements Serializable{
	
	int ping;
	long creationTime;
	
	public Ping(){
		creationTime = System.currentTimeMillis();
	}
	
	public Ping(long ms){
		creationTime = ms;
	}
	
	public void setCreationTime(long ms){
		creationTime = ms;
	}
	
	public long getCreationTime(){
		return creationTime;
	}
	
	public int getPing(){
		return ping;
	}
	
	public int calcPing(){
		ping = (int) (System.currentTimeMillis() - creationTime);
		return ping;
	}
	
	public String toString(){
		return "Ping: " + calcPing();
	}
	
}
