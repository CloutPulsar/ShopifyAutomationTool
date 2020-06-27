import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Proxies
{
	public int threadID = 0;
	public int threads = 2;
	private static volatile int proxCounter = 0;
	private static volatile String proxy;
	private static volatile boolean connected = false;
	private static volatile ArrayList<String> proxyList;
	private static WebDriver driver;
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
	private String testConnection() throws MalformedURLException, IOException, InterruptedException
	{
		String data = null;
		while(connected == false)
		 {
			proxy = Proxies.proxyList.get(proxCounter);
			Proxies.proxyList.remove(proxCounter);
			ChromeOptions options = new ChromeOptions().addArguments("--proxy-server=http://" + proxy);
			options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
			options.addArguments("--disable-extensions");
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);
			driver.manage().timeouts().pageLoadTimeout(80, TimeUnit.SECONDS);
			System.out.println(proxy);
			String[] IPandPort = proxy.split(":");
			try 
			{
				driver.get("https://kith.com");
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				try
				{
					WebElement t = driver.findElement(By.className("neterror"));
					System.out.println("NETERROR: Site could not be reached...");
					System.out.println("Current Proxy: IP Address: "+IPandPort[0]+"\t Port #: "+IPandPort[1]);
					System.out.println("Error could not connect... Retrying with another Proxy...");
					
				}catch(NoSuchElementException e)
				{
					System.out.println("Success!");
					connected = true;
					driver.manage().timeouts().implicitlyWait(Integer.MAX_VALUE, TimeUnit.SECONDS);
					data = proxy;
				}
				
			}catch(TimeoutException e)
			{
				System.out.println("Current Proxy: IP Address: "+IPandPort[0]+"\t Port #: "+IPandPort[1]);
				System.out.println("Error could not connect... Retrying with another Proxy...");
			}
			driver.quit();
		}
		connected = false;   
		return data;
	}
}
