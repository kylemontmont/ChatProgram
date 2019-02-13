package message;

public class MessageNameChange extends Message{

	String newUsername;
	
	public MessageNameChange(){
		super();
	}
	
	public MessageNameChange(String username, String newUsername){
		super(username);
		this.newUsername = newUsername;
	}
	
	public String getNewUsername(){
		return newUsername;
	}
	
	public String toString(){
		return username + " changed their name to " + newUsername;
	}
	
}
