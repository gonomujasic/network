package chat;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.Base64.Encoder;

public class ChatWindow {

	private Frame frame;
	private Panel pannel;
	private Button buttonSend;
	private TextField textField;
	private TextArea textArea;
	private PrintWriter pw;
	private BufferedReader br;
	private Socket socket;

	public ChatWindow(String name, Socket socket, BufferedReader br, PrintWriter pw) {
		frame = new Frame(name);
		pannel = new Panel();
		buttonSend = new Button("Send");
		textField = new TextField();
		textArea = new TextArea(30, 80);
		this.socket = socket;
		this.pw = pw;
		this.br = br;
	}

	public void show() {
		// Button
		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				sendMessage(pw);
			}
		});

		// Textfield
		textField.setColumns(80);
		textField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				char keyCode = e.getKeyChar();
				if (keyCode == KeyEvent.VK_ENTER) {
					sendMessage(pw);
				}
			}
		});

		// Pannel
		pannel.setBackground(Color.LIGHT_GRAY);
		pannel.add(textField);
		pannel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, pannel);

		// TextArea
		textArea.setEditable(false);
		frame.add(BorderLayout.CENTER, textArea);

		// Frame
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.pack();

		new ChatClientReceiveThread(br).start();
	}

	private void sendMessage(PrintWriter pw) {
		String message = textField.getText();
		String sendMssg = null;
		/*textArea.append(frame.getName()+ ":" + message);
		textArea.append("\n");*/

		textField.setText("");
		textField.requestFocus();
		
		if("quit".equals(message)) {
			sendMssg = "QUIT";
			pw.println(sendMssg);
			pw.flush();
			System.exit(0);
		} else {
			sendMssg = "CHAT "+ message;
			
			
			pw.println(sendMssg);
			pw.flush();
		}
		
	}

	private class ChatClientReceiveThread extends Thread {
		
		BufferedReader br;

		public ChatClientReceiveThread(BufferedReader br) {
			this.br = br;
		}

		@Override
		public void run() {
			try {
				while (true) {

					String mssg = br.readLine();
					textArea.append(mssg);
					textArea.append("\n");
					System.out.println(mssg);
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
