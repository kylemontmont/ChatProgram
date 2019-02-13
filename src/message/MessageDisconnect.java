package message;


public class MessageDisconnect extends Message{

	public MessageDisconnect(){
		super();
	}
	
	public MessageDisconnect(String username){
		super(username);
	}
	
	public String toString(){
		return username + " disconnected";
	}
	
}
