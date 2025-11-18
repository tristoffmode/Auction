import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class AuctionServer 
{

	private static int currentBid = 0;               // Giá hiện tại
	private static String currentWinner = "Personne"; // Người đang dẫn
	private static long lastBidTime = System.currentTimeMillis();
	private static final List<ClientHandler> clients = new ArrayList<>();

	private static final Object lock = new Object();  // Đồng bộ hóa

	public static void main(String[] args) throws Exception 
	{
		ServerSocket serverSocket = new ServerSocket(5000);
		System.out.println("[SERVER] Server started on port 5000...");

		// Thread countdown riêng
		new Thread(() -> manageCountdown()).start();

		// Chấp nhận client liên tục
		while (true) 
			{
			Socket socket = serverSocket.accept();
			ClientHandler ch = new ClientHandler(socket);
			synchronized (clients) 
			{
				clients.add(ch);
			}
			ch.start();
		}
	}

	// Quản lý “une fois / deux fois / adjudgé”
	private static void manageCountdown() 
	{
		while (true) {
			long now = System.currentTimeMillis();
			long inactive = now - lastBidTime;

			try 
			{
				if (inactive >= 10_000 && inactive < 20_000) 
				{
					broadcast("[SERVER] Une fois !");
				} 
				else if (inactive >= 20_000 && inactive < 30_000) 
				{
					broadcast("[SERVER] Deux fois !");
				}
				else if (inactive >= 30_000)
				{
					broadcast("[SERVER] Adjugé ! Winner: " + currentWinner);
					System.out.println("[SERVER] Enchère terminée.");
					System.exit(0);
				}

				Thread.sleep(1000);

			} catch (Exception e) {}
		}
	}

	// Gửi thông báo cho tất cả client
	public static void broadcast(String msg) 
	{
		synchronized (clients) {
			for (ClientHandler ch : clients) 
			{
				ch.sendMessage(msg);
			}
		}
	}

	// Khi có đấu giá mới
	public static void newBid(int amount, String bidder) 
	{
		synchronized (lock) 
		{
			if (amount > currentBid) 
			{
				currentBid = amount;
				currentWinner = bidder;
				lastBidTime = System.currentTimeMillis();

				broadcast("[SERVER] Nouvelle enchère: " + amount + " par " + bidder);
			}
		}
	}

	// Xử lý từng client
	static class ClientHandler extends Thread 
	{
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;
		private String name;

		public ClientHandler(Socket socket) 
		{
			this.socket = socket;
		}

		public void sendMessage(String msg) 
		{
			out.println(msg);
		}

		@Override
		public void run() 
		{
			try 
			{
				out = new PrintWriter(socket.getOutputStream(), true);
				in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				out.println("Entrez votre nom : ");
				name = in.readLine();

				out.println("Bienvenue " + name + ". Enchère actuelle = " + currentBid);

				String line;
				while ((line = in.readLine()) != null) 
				{
					try 
					{
						int offer = Integer.parseInt(line);
						AuctionServer.newBid(offer, name);
					} catch (NumberFormatException e) 
					{
						out.println("[ERREUR] Merci d'envoyer un nombre entier.");
					}
				}

			} catch (Exception e) {
				System.out.println("[SERVER] Client déconnecté.");
			}
		}
	}
}
