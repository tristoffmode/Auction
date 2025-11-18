import java.io.*;
import java.net.*;

public class AuctionClient 
{

	public static void main(String[] args) throws Exception 
	{
		Socket socket = new Socket("localhost", 5000);

		BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter serverOut    = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

		// Thread nhận tin từ server
		new Thread(() -> 
		{
			String msg;
			try {
				while ((msg = serverIn.readLine()) != null) 
				{
					System.out.println(msg);
				}
			} catch (Exception e) {}
		}).start();

		// Thread gửi dữ liệu mình nhập
		String line;
		while ((line = userInput.readLine()) != null) 
		{
			serverOut.println(line);
		}
	}
}
