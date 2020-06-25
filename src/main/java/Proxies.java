import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Proxies
{
	public static class PageRefresherFix
	{
		public void run() {}
	}
	public int threadID = 0;
	public int threads = 2;
	public static volatile int successcount =0;
	private static volatile int proxCounter = 0;
	private final int availableThreads = Runtime.getRuntime().availableProcessors();
	private Thread[] thread = new Thread[availableThreads];
	private ArrayList<String> proxyList;
	public Proxies() throws InterruptedException
	{
		//proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("52.179.18.244", 8080));
		proxyList = fillProxyList();
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
	public WebDriver createDriver() throws InterruptedException, MalformedURLException, IOException
	{
		return createDriverImplementation();
	}
	private WebDriver createDriverImplementation() throws InterruptedException, MalformedURLException, IOException
	{
		String proxy = testConnection();
		ChromeOptions options = new ChromeOptions().addArguments("--proxy-server=http://" + proxy);
		options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
		options.addArguments("--disable-extensions");
		WebDriverManager.chromedriver().setup();
		return new ChromeDriver(options);
	}
	private String testConnection() throws MalformedURLException, IOException
	{
		String data = null;
		while(proxCounter < proxyList.size()) 
		{
			String tmp = proxyList.get(proxCounter);
			String[] IPandPort = tmp.split(":");
			proxyList.remove(proxCounter);
			proxCounter++;
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(IPandPort[0], Integer.parseInt(IPandPort[1])));
			try
			{
				URLConnection conn = new URL("https://google.com").openConnection(proxy);
				conn.addRequestProperty("User-Agent", "Mozilla");
				conn.setConnectTimeout(3000);
				data = tmp;
				System.out.println("Successfully Connected.");
				break;
			}catch(ConnectException e)
			{
				System.out.println("Current Proxy: IP Address: "+IPandPort[0]+"\t Port #: "+IPandPort[1]);
				System.out.println("Error could not connect... Retrying with another Proxy...");
			}
		}
		return data;
	}
	public static void main(String[] args) throws InterruptedException
	{
		Proxies prox = new Proxies();
		String proxy = "35.192.37.211:3128";
		System.out.println(proxy);
		ChromeOptions options = new ChromeOptions().addArguments("--proxy-server=http://" + proxy);
		options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
		options.addArguments("--disable-extensions");
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, 5);
		PageRefresherFix tryAgain = new PageRefresherFix() 
		{
			
			@Override
			public void run() 
			{
				try
				{
					driver.get("https://kith.com");
					System.out.println(driver.getTitle());
					System.out.println("Success!");
				}catch(TimeoutException e)
				{
					System.out.println("Error could not connect with proxy... Retrying");
					System.out.println("FAILED PROXY: "+proxy);
					driver.close();
					run();
				}
			}
		};
		tryAgain.run();
		
	}
}
