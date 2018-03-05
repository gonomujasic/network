package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

public class ChatServerProcessThread extends Thread{
	
	private String nickname;
	private Socket socket;
	private LinkedList<PrintWriter> printWriterList;

	public ChatServerProcessThread(Socket socket, LinkedList<PrintWriter> printWriterList) {
		this.socket = socket;
		this.printWriterList = printWriterList;
	}

	@Override
	public void run() {
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"), true);
			String mssg;
			String[] splitedMssg;
			String sendMssg;
			
			while(true) {
				
				mssg = br.readLine();
				
				if(mssg == null) {
					log("클라이언트로부터 연결이 끊김");
					doQuit(pw);
					break;
				}
				
				//프로토콜 분석
				splitedMssg = mssg.split(" ");
				if("JOIN".equals(splitedMssg[0])) {
					doJoin(splitedMssg[1], pw);
					
				} else if("CHAT".equals(splitedMssg[0])) {
					doChat(splitedMssg[1]);
				} else if("QUIT".equals(splitedMssg[0])) {
					sendMssg = splitedMssg[1]+"님이 퇴장하셨습니다.";
					//퇴장 시 조치 사항
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void log(String mssg) {
		System.out.println(mssg);
	}
	
	private void doJoin(String nick, PrintWriter pw) {
		
		//입장 시 조치 사항
		
		synchronized (printWriterList) {
			printWriterList.add(pw);
		}
		
		nickname = nick;
		String greeting = nickname+"님이 입장하셨습니다.\r\n";
		broadcast(greeting);
		
	}

	private void broadcast(String mssg) {
		
		synchronized (printWriterList) {
		
			Iterator<PrintWriter> it = printWriterList.iterator();
			while(it.hasNext()) {
				PrintWriter pw = it.next();
				pw.print(mssg);
				pw.flush();
			}
		}
	}
	
	private void doChat(String contents) {
		String mssg = nickname +": "+ contents+"\r\n";
		
		broadcast(mssg);
	}

	private void doQuit(PrintWriter pw) {
		
		synchronized (printWriterList) {
			printWriterList.remove(printWriterList.indexOf(pw));
		}
		
		String bye = nickname +"님이 퇴장하셨습니다.\r\n";
		broadcast(bye);
		
	}
	
}
