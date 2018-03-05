package time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPTimeServer {

	private static final int PORT = 5000;
	private static final int BUFFER_SIZE = 1024;
	
	public static void main(String[] args) {

		DatagramSocket datagramSocket = null;
		
		try {
			//1. 소켓 생성
			datagramSocket = new DatagramSocket(PORT);
			
			while(true) {
				//2. 수신 패킷 설정 
				DatagramPacket datagramPacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				
				//3. 수신 대기
				datagramSocket.receive(datagramPacket);
				
				//3. 수신 message에 내용물 없음. "" 그냥 바로 시간 보냄
				String message = new String(datagramPacket.getData(),0, datagramPacket.getLength(),"utf-8");
				
				System.out.println("[UDP Echo Server] received: 메시지 도착");
				
				//4. 시간 처리
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
				String data = sdf.format(new Date());
				System.out.println(data);
				//5. 송신
				byte[] byteData = data.getBytes();
				DatagramPacket sendData = new DatagramPacket(byteData, byteData.length, 
						new InetSocketAddress(datagramPacket.getAddress(), datagramPacket.getPort()));
				
				datagramSocket.send(sendData);
				
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if( datagramSocket != null && datagramSocket.isClosed() == false ) {
				datagramSocket.close();
			}
		}
		
		
		
	}

}
