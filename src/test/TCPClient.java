package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {
	private static final String SERVER_IP = "192.168.73.1";
	private static final int SERVER_PORT = 5000;
	
	public static void main(String[] args) {
		Socket socket = null;
		
		try {
			//1. 소켓 생성
			socket = new Socket();
			
			//1.1 버퍼 사이즈 참조 및 조절. 기본 64KB	
			System.out.println(socket.getReceiveBufferSize());
			System.out.println(socket.getSendBufferSize());
			socket.setReceiveBufferSize(1024*10);
			socket.setSendBufferSize(1024*10);
			System.out.println(socket.getReceiveBufferSize());
			System.out.println(socket.getSendBufferSize());
			
			//1.2 SO_TIMEOUT 
			socket.setSoTimeout(1);

			//1.3 SO_NODELAY(네이글 알고리즘 off)
			socket.setTcpNoDelay( true );
			//2. 서버연결
			socket.connect( new InetSocketAddress(SERVER_IP, SERVER_PORT ) );
		
			//3. I/O Stream 받아 오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			//4. 쓰기/읽기
			String data = "hello";
			os.write( data.getBytes( "utf-8" ) );
			
			byte[] buffer = new byte[256];
			int readByteCount = is.read( buffer );
			
			if( readByteCount == -1 ) {
				System.out.println( "[client] disconnected by Server" );
				return;
			}
			
			data = new String( buffer, 0, readByteCount, "utf-8" );
			System.out.println( "[client] received:" + data );
			
		} catch(ConnectException e ) {
			System.out.println( "[client] Not Connected");
		} catch (SocketTimeoutException e) {
			System.out.println("[client] Read Time Out");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}
		
	}

}
