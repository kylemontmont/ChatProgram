package message;

public class MessageRequest extends Message{

	String request;
	
	public MessageRequest(){
		super();
	}
	
	public MessageRequest(String request){
		super();
		this.request = request;
	}
	
	public String toString(){
		return request;
	}
	
}
