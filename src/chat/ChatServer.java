package chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ChatServer {

	private static final int SERVER_PORT = 6000;
	
	private static class SingletonHolder {
		
		private static LinkedList<PrintWriter> printWriterList = new LinkedList<>();
	}
	
	public static LinkedList<PrintWriter> getPrintWriterList() {
		return SingletonHolder.printWriterList;
	}
	

	public static void main(String[] args) {
		try {
			// 1. 소켓생성
			ServerSocket socketServer = new ServerSocket();

			// 2. 바인딩
			String address = InetAddress.getLocalHost().getHostAddress();
			socketServer.bind(new InetSocketAddress(address, SERVER_PORT));
			log("[server] 바인딩 완료");
			
			// 3. waiting
			while (true) {
				Socket socket = socketServer.accept();
				
				new ChatServerProcessThread(socket, getPrintWriterList()).start();
				log("[server] 연결");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void log(String str) {
		System.out.println(str);
	}
}
