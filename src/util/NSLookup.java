package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		try {
			while (true) {
				System.out.println("주소를 입력해주세요.");
				System.out.print(">>");
				String str = scanner.nextLine();

				if ("exit".equals(str)) {
					System.out.println("종료됩니다.");
					break;
				}
				
				InetAddress[] addresses = InetAddress.getAllByName(str);

				for (InetAddress inetAddress : addresses) {
					System.out.println(inetAddress.getHostName()+" : "+inetAddress.getHostAddress());
				}

			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

}
