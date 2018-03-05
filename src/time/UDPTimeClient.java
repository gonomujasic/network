package time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UDPTimeClient {

	private static final String SERVER_IP = "192.168.1.13";
	private static final int SERVER_PORT = 5000;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {

		DatagramSocket socket = null;
		char cha = 34;
		String message = "";
		message += cha;
		message += cha;

		try {
			// 1. 소켓 생성
			socket = new DatagramSocket();

			// 2. 메시지 생성 및 바이트화
			byte[] byteMessage = message.getBytes();
			
			while (true) {
				// 3. 데이터패킷 생성
				DatagramPacket sendData = new DatagramPacket(byteMessage, byteMessage.length, new InetSocketAddress(SERVER_IP, SERVER_PORT));

				// 4. 데이터패킷 전송
				socket.send(sendData);
				System.out.println("[client] 데이터 패킷 전송완료");

				// 5. 데이트패킷 생성 및 수신
				DatagramPacket receiveData = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receiveData);

				// 6. 패킷에서 추출하기 String으로
				String str = new String(receiveData.getData(), 0, receiveData.getLength(), "utf-8");

				System.out.println(str);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( socket != null && socket.isClosed() == false )
				socket.close();
		}

	}

}
