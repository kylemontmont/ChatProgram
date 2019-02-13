import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import message.*;

/*to do
save a config
auto find or launch server
test pinging
port to android
*/
public class Client extends JFrame implements ActionListener, Runnable{
	
	static Socket socket;
	static ObjectInputStream in;
	static ObjectOutputStream out;
	static boolean connected = false;
	
	static JTextArea chatHistory;
	static JTextField textInput;
	
	static String ip = "10.23.112.102";
	static int port = 7777;
	
	static String username = "test";
	
	
	public Client(){}
	
	public Client(boolean gui){
		super();
		setupWindow();
		new Thread(new Client()).start();
	}

	public void setupWindow(){
		
		setTitle("Client");
		setSize(300,300);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - getSize().width / 2, dim.height/2 - getSize().height / 2);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		chatHistory = new JTextArea();
		chatHistory.setEditable(false);
		
		JScrollPane chatHistoryScroll = new JScrollPane(chatHistory);
		
		textInput = new JTextField();
		textInput.addActionListener(this);
		
		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chatHistoryScroll, textInput);
		pane.setDividerSize(2);
		pane.setDividerLocation(getHeight() - 70);
		
		add(pane);
		
		setVisible(true);
		
	}
	
	public void println(String s){
		System.out.println(s);
		chatHistory.append(s + "\n");
		chatHistory.setCaretPosition(chatHistory.getDocument().getLength());
	}
	
 	public boolean connect(){
		
		if(!connected)
			try {
				println("Trying to connect...");
				socket = new Socket(ip, port);
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
				//new Thread(new Client()).start();
				System.out.println("connected");
			} catch (IOException e) {
				System.out.println(e);
				return false;
			}
		
		sendObject(new MessageConnect(username));
        return true;
        
	}
	
	public void disconnect(){
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		connected = false;
		println("disconnected");
	}
	
	@Override
	public void run() { //Receiver

		Thread.currentThread().setName("Client Reciver");
		Object incomingMessage = null;
		
		while(true){
			if(connected){
				try {
					incomingMessage = in.readObject();
				} catch (IOException | ClassNotFoundException e) {
					System.out.println(e);
					disconnect();
					return;
				}

				println(incomingMessage.toString());
				
			}
			try {
				Thread.currentThread().sleep(50);
			} catch (InterruptedException e){}
		}
		
	}
	
	public void sendObject(Object obj){
		try {
			out.writeObject(obj);
		} catch (IOException e) {
			System.out.println(e);
			disconnect();
		}
	}
	
	public void actionPerformed(ActionEvent e) { //text field
		
		String input = textInput.getText();
		textInput.setText("");
		
		if(input.indexOf("/") == 0){
			command(input);
		}else if(connected){
			sendObject(new Message(username, input));
		}else{
			println("command not reconized");
		}
		
	}

	public void command(String s){
		 
		String param, command;
		
		if(s.indexOf(" ") != -1){
			command = s.substring(1, s.indexOf(" "));
			param = s.substring(s.indexOf(" ")).trim();
		}else{
			command = s.substring(1);
			param = "noparam";
		}
		
		
		if(command.equalsIgnoreCase("help")){							//help
			println("/connect");
			println("/connect IP:PORT");
			println("/disconnect");
			println("/ip NEWIP");
			println("/port NEWPORT");
			println("/name NEWNAME");
			println("/users");
			println("/ping");
		}else if(command.equalsIgnoreCase("connect") && !connected){    //connect
			if(param.indexOf(":") != -1){
				ip = param.substring(0, param.indexOf(":"));
				port = Integer.valueOf((param.substring(param.indexOf(":") + 1)));
				println("ip set to " + ip + " and port set to " + port);
			}
			connected = connect();
		}else if(command.equalsIgnoreCase("disconnect")){				//disconnect
			disconnect();
		}else if(command.equalsIgnoreCase("ip")){						//ip
			ip = param;
		}else if(command.equalsIgnoreCase("port")){ 					//port
			port = Integer.valueOf(param);
		}else if(command.equalsIgnoreCase("name")){						//name
			if(connected)
				sendObject(new MessageNameChange(username, param));
			username = param;
		}else if(command.equalsIgnoreCase("users")){					//users
			if(connected)
				sendObject(new MessageRequest("users"));
			else
				println("not connected");
		}else if(command.equalsIgnoreCase("ping")){						//ping
			if(connected)
				sendObject(new Ping());
			else
				println("not connected");
		}
		
	}
	
}











































