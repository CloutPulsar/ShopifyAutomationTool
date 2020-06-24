import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class Proxies
{
	public int threadID = 0;
	public int threads = 2;
	public static volatile int successcount =0;
	private static volatile int proxCounter = 0;
	private Proxy proxy;
	private final int availableThreads = Runtime.getRuntime().availableProcessors();
	private Thread[] thread = new Thread[availableThreads];
	private ArrayList<String> proxyList;
	public Proxies() throws InterruptedException
	{
		//proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("52.179.18.244", 8080));
		proxyList = fillProxyList();
		multiThreadProxies(threads);
	}
	private ArrayList<String> fillProxyList()
	{
		ArrayList<String> list = new ArrayList<String>();
		try
		{
			Scanner s = new Scanner(new File("proxies.txt"));
			while (s.hasNextLine())
			    list.add(s.nextLine());
			s.close();
		} catch (FileNotFoundException e)
		{
			System.out.println("Error file was not found.");
		}
		return list;
	}
	private void setUpConn() throws MalformedURLException, IOException
	{
		connect(3);
	}
	private void setUpConn(int count) throws MalformedURLException, IOException
	{
		count--;
		if (count <= 0)
		{
			System.out.println("ERROR: Could not connect after 3 attemps. Connection Timeout.");
			return;
		}
		else
			connect(count);
	}
	private void connect(int id) throws MalformedURLException, IOException
	{
		try 
		{
			URLConnection conn = new URL("https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java").openConnection(proxy);
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.setConnectTimeout(5000);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
				 System.out.println(inputLine);
			in.close();
			synchronized(this)
			{
				successcount++;
			}
			System.out.println("Success!"+successcount);

		}catch(ConnectException e)
		{
			System.out.println("Connection Timed out... Retrying");
			setUpConn(id);
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
				while(proxCounter < proxyList.size()) 
				{
					String data;
					synchronized(this)
					{
						 data = proxyList.get(proxCounter);
						 proxCounter++;
					}
					String[] IPandPort = data.split(":");
					proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(IPandPort[0], Integer.parseInt(IPandPort[1])));
					try
					{
						setUpConn();
					} catch (IOException e)
					{
						System.out.println("Error Thread " + threadID +" could not connect to website.");
						System.out.println("IP Address: "+IPandPort[0]+"\t Port #: "+IPandPort[1]);
					}
				}


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
	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException
	{
		Proxies proxyTest = new Proxies();
		System.out.println("TEST BEGAN");
	}
}
