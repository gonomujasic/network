package echo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;
	
	public EchoServerReceiveThread( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		//4. 연결 성공
		InetSocketAddress remoteSocketAddress = 
				(InetSocketAddress)socket.getRemoteSocketAddress();
		int remoteHostPort = remoteSocketAddress.getPort();
		String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();
		consoleLog( "connected from " + remoteHostAddress + ":" + remoteHostPort );
		
		try {
			//5. I/O Stream 받아오기
			/*InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();*/
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			while( true ) {
/*				//6. 데이터 읽기(read)
				byte[] buffer = new byte[256];
				int readByteCount = is.read( buffer ); //Blocking
				
				if( readByteCount == -1 ) { //정상종료
					consoleLog( "disconnected by client" );
					break;
				}
				
				String data = new String( buffer, 0, readByteCount, "utf-8" );
				consoleLog( "received:" + data );
			
				//7. 데이터 쓰기
				os.write( data.getBytes( "utf-8" ) );*/
				String data = br.readLine();
				consoleLog("received: "+data);
				bw.write(data+"\n");
				bw.flush();
				
			}
		} catch(SocketException e) {
			// 상대편이 정상적으로 소켁을 닫지 않고 종료 한 경우
			consoleLog( "sudden closed by client" );
		} catch( IOException e ) {
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
	
	private void consoleLog( String log ) {
		System.out.println( "[server:" + getId() + "] " + log );
	}
}
