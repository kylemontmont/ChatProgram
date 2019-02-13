import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Launcher extends JFrame implements ActionListener{

	String[] buttonNames = new String[]{"Client", "Server"};
	JButton[] buttons = new JButton[3];
	
	public static void main(String[] args) {
		new Launcher();
	}
	
	public Launcher(){
		
		setTitle("Chat Launcher");
		setSize(300,300);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		
		for(int i = 0; i < 2; i++){
			buttons[i] = new JButton(buttonNames[i]);
			buttons[i].addActionListener(this);
			panel.add(buttons[i]);
		}
		
		add(panel);
		
	}
	
	public void actionPerformed(ActionEvent e) {

		if(e.getActionCommand().equalsIgnoreCase("client")){
			new Client(true);
			setVisible(false);
		}else if(e.getActionCommand().equalsIgnoreCase("server")){
			try {
				new Server();
				setVisible(false);
			} catch (IOException e1) {}
			
		}
		
	}

}










