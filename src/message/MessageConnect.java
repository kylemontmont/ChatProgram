package message;

public class MessageConnect extends Message{

	public MessageConnect(){
		super();
	}
	
	public MessageConnect(String username){
		super(username);
	}
	
	public String toString(){
		return username + " connected";
	}
	
}
