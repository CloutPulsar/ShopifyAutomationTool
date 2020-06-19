import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class Proxies
{
	public int threadID = 0;
	private Proxy proxy;
	private final int availableThreads = Runtime.getRuntime().availableProcessors();
	private Thread[] thread = new Thread[availableThreads];
	private void setUpConn() throws MalformedURLException, IOException
	{
		proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("174.138.74.211", 8080));
		try 
		{
			URLConnection conn = new URL("https://www.foxnews.com/").openConnection(proxy);
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.setConnectTimeout(5000);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
				 System.out.println(inputLine);
			in.close();
		}catch(IOException e)
		{
			System.out.println("Connection Timedout... Retrying");
			setUpConn(3);
		}
	}
	private void setUpConn(int count) throws MalformedURLException, IOException
	{
		count--;
		if (count == 0)
		{
			System.out.println("ERROR: Could not connect after 3 attemps. Connection Timeout.");
			return;
		}
		else
		{
			try 
			{
				URLConnection conn = new URL("https://www.foxnews.com/").openConnection(proxy);
				conn.addRequestProperty("User-Agent", "Mozilla");
				conn.setConnectTimeout(5000);
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) 
					 System.out.println(inputLine);
				in.close();
			}catch(ConnectException e)
			{
				System.out.println("Connection Timedout... Retrying");
				setUpConn(count);
			}
		}	
	}
	private void multiThreadProxies(int threadCount) throws InterruptedException
	{
		//---------------------------Create Threads for faster searching------------------------------------------//
		Runnable runnable = new Runnable()
		{
			public void run()
			{
				int threadNum = threadID;
				
			}
		};
		for(int i = 0; i < threadCount; i++)
		{
			thread[i] = new Thread(runnable, ("Thread "+ (2+i)));
			thread[i].start();
			Thread.sleep(1);
			threadID++;
			System.out.println(thread[i].getName() + " has been initiated.");
		}
	}
	public static void main(String[] args) throws MalformedURLException, IOException
	{
		Proxies proxyTest = new Proxies();
		System.out.println("TEST BEGAN");
		proxyTest.setUpConn();
	}
}
