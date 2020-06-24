import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Browser implements BasicSiteFunctions, Checkout
{
	public class PageRefresherFix
	{
		public void run() {}
	}
	public final static int productPerPage = 30;
	public int threadID = 0;
	private final int availableThreads = Runtime.getRuntime()
			.availableProcessors();
	private final static String extension = "/collections/all/products.json?page=1&limit=250";
	private final static HashMap<String, String[]> webMap = new HashMap<String, String[]>();
	private final static ObjectMapper mapper = new ObjectMapper();
	private final static ChromeOptions options = new ChromeOptions();
	private static JsonNode jsonObject;

	private ArrayList<String> keywords = new ArrayList<String>();
	private String url;
	private String variant;
	private String sizeOption = null;
	private WebDriver driver;
	private int totalPages;
	private int PagesLoaded = 1;
	/*----------------Shared Variables between Threads-----------------*/
	// -----------------------------------------------------------------//
	public static volatile int bestPage = Integer.MAX_VALUE;
	public static volatile int globalMax = Integer.MIN_VALUE;
	// ------------------------------------------------------------------//
	/*-----------------------------------------------------------------*/
	private int[] priceRange =
	{Integer.MIN_VALUE, Integer.MAX_VALUE};
	private Object[] bestProduct;
	private boolean validVariant = false;
	private Thread[] thread = new Thread[availableThreads];
	long start, end;

	public static String[] sites;
	public Browser()
	{
		// The Following websites contain a products.json file, which allows for
		// easier monitoring
		sites = defaultSites();
		WebDriverManager.chromedriver().setup();
		options.setExperimentalOption("excludeSwitches", new String[]
		{"enable-automation"});
		mapDefaultSites();
	}
	// Maps the total page count with limit = 250 products per page for each
	// supported Shoppify Website.
	/*
	 * Total Pages:(as of 1/12/2020): Kith = 270, Undefeated = 36 ExtraButterNY
	 * = 65 BodegaStore = 414 Cncpts = 51 NotreShop = 28 JuiceStore = 28
	 */
	public void mapDefaultSites()
	{

		webMap.put("kith", new String[]
		{sites[0], "270", Integer.toString(calcPage250(270))});
		webMap.put("undefeated", new String[]
		{sites[1], "36", Integer.toString(calcPage250(36))});
		webMap.put("extrabutterny", new String[]
		{sites[2], "65", Integer.toString(calcPage250(65))});
		webMap.put("bodegastore", new String[]
		{sites[3], "414", Integer.toString(calcPage250(414))});
		webMap.put("cncpts", new String[]
		{sites[4], "51", Integer.toString(calcPage250(51))});
		webMap.put("notreshop", new String[]
		{sites[5], "28", Integer.toString(calcPage250(28))});
		webMap.put("juicestrore", new String[]
		{sites[6], "28", Integer.toString(calcPage250(28))});

	}
	public String getSites()
	{
		String str = "";
		for (String s : webMap.keySet())
			// removes the characters "[" "]" "," from the Arrays.toString()
			// method
			str = str + Arrays.toString(webMap.get(s)).replaceAll("[\\[\\],]", "") + "\n";
		return str;
	}
	private int calcPage250(int pages)
	{
		// Math.Ceiling implementation to round to the next highest number. i.e.
		// (x + (n-1))/n
		return ((pages * 30) + 249) / 250;
	}
	public void initiateSearch() throws IOException, InterruptedException
	{
		// ---------------------------Create Threads for faster searching------------------------------------------//
		Runnable runnable = new Runnable()
		{
			public void run()
			{
				try
				{
					int threadNum = threadID;
					String data = getUrl() + extension.replace("page=1",
							"page=" + thread[threadNum].getName()
									.replace("Thread ", ""));
					String JSON = fetchJSONData(data);
					jsonObject = mapper.readTree(JSON);
					int tmpProductCounter = jsonObject.get("products").size();
					System.out.println(tmpProductCounter);
					if (tmpProductCounter < 250)
						totalPages = (tmpProductCounter + (productPerPage - 1))
								/ productPerPage;
					productSearch(tmpProductCounter, threadNum);
				} catch (IOException | InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		};
		for (int i = 0; i < ((availableThreads > 5) ? 5: availableThreads); i++)
		{
			thread[i] = new Thread(runnable, ("Thread " + (2 + i)));
			thread[i].start();
			Thread.sleep(1);
			threadID++;
			System.out.println(thread[i].getName() + " has been initiated.");
		}
		String JSON = fetchJSONData(url);
		// Format the JSON Data
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		jsonObject = mapper.readTree(JSON);
		/*------------------For Data Extraction Analysis----------------------*/
		// String formattedJson =
		// mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
		// BufferedWriter writer = new BufferedWriter(new
		// FileWriter("json.json"));
		// writer.write(formattedJson);
		// writer.close();
		/*------------------For Data Extraction Analysis----------------------*/
		// Must follow this format in order to access variants of each product
		// without creating
		// more JsonNode instances:
		// jsonObject.get("products").get(i).get("variants").toString()
		// where i is the i-th product.
		int tmpProductCounter = jsonObject.get("products").size();
		System.out.println(tmpProductCounter);
		if (tmpProductCounter < 250)
			totalPages = (tmpProductCounter + (productPerPage - 1)) / productPerPage;
		productSearch(tmpProductCounter, -1);
	}
	private void productSearch(int productNum, int threadNum)
			throws IOException, InterruptedException
	{
		int productNumber = 0;
		int max = Integer.MIN_VALUE;
		int[] productIndex = new int[productNum];
		String[] handle, tags;
		if (threadNum == -1)
			start = System.currentTimeMillis();
		for (JsonNode product : jsonObject.get("products"))
		{
			int count = 0;
			int price = (int) Double.parseDouble(
					product.get("variants").get(0).get("price").asText());
			if (price >= priceRange[0] && price <= priceRange[1])
			{
				handle = product.get("handle").toString().toLowerCase()
						.replace("\"", "").split("-");
				tags = product.get("tags").toString().toLowerCase()
						.replaceAll("[\\[\\]\\s\"]", "").split(",");
				ArrayList<String> productWords = new ArrayList<String>(
						Arrays.asList(handle));
				productWords.addAll(Arrays.asList(tags));
				for (String keyword : keywords)
				{
					if (productWords.contains(keyword) && keyword != "")
					{
						count++;
						productWords.remove(keyword);
					}
					if (max < count)
						max = count;
				}
			}
			productIndex[productNumber] = count;
			productNumber++;
		}
		Multimap<Integer, Integer> products = ArrayListMultimap.create();
		for (int i = 0; i < productIndex.length; i++)
			products.put(productIndex[i], i);
		System.out.println(products.toString());
		Collection<Integer> maxMatches = products.get(max);
		Object[] matches = maxMatches.toArray();
		synchronized (this)
		{
			int currPage = 1;
			if (threadNum != -1)
				currPage = Integer.parseInt(
						thread[threadNum].getName().replace("Thread ", ""));
			if (globalMax <= max && currPage < bestPage)
			{
				globalMax = max;
				bestProduct = matches;
				bestPage = currPage;
				System.out.println("BestPage is now " + bestPage);
				System.out.println("Total Matches: " + max);
			}
			if (threadNum != -1)
			{
				System.out.println(thread[threadNum].getName() + " has finished processing products.");
				return;
			}
		}
		int i = 2;
		while (thread[i].isAlive())
			thread[i].join();
		String newUrl = getUrl()
				+ extension.replace("page=1", "page=" + bestPage);
		setUrl(newUrl);
		if(bestProduct.length == 0)
			System.out.println("No Product was found. Terminating...");
		else
			findVariantID(bestProduct, bestProduct.length);
		end = System.currentTimeMillis();
		System.out.println("Page Search Performance Time: " + (end - start) + " ms");

	}
	private void findVariantID(Object[] matches, int length)throws JsonMappingException, JsonProcessingException
	{
		if (sizeOption == null)
		{
			String JSON = fetchJSONData(url);
			jsonObject = mapper.readTree(JSON);
			String str = jsonObject.get("products")
					.get(Integer.parseInt(matches[0].toString()))
					.get("variants").get(0).get("id").toString();
			setVariant(str);
		} 
	}
	public void setUrl(String url)
	{
		this.url = url + extension;
	}
	public String getUrl()
	{
		return url.substring(0, url.indexOf("/collections"));
	}
	public void loadBrowser()
	{
		driver = new ChromeDriver(options);
		driver.get(url);
	}
	public void closeBrowser()
	{
		driver.quit();
	}
	private int pageCount()
	{
		long start = System.currentTimeMillis();
		int counter = 9;
		int nextPage = 10;
		String appendPages = "?page=";
		String newUrl = url + appendPages + nextPage;
		String emptyProductObject = "{\"products\":[]}";
		String JSON = fetchJSONData(newUrl);
		// Binary Search
		while (JSON != emptyProductObject)
		{
			counter++;
			newUrl = url + appendPages + nextPage;
			JSON = fetchJSONData(newUrl);
		}
		long end = System.currentTimeMillis();
		System.out.println("Page Count Performance Time: " + (end - start));
		return counter;
	}
	private String fetchJSONData(String url)
	{
		String data = "";
		try
		{
			String str;
			URL site = new URL(url);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(site.openStream()));
			while (null != (str = br.readLine()))
				data += str;
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return data;
	}
	private static String[] defaultSites()
	{
		return new String[]
		{"https://kith.com/collections/all/products.json",
				"https://undefeated.com/collections/all/products.json",
				"https://shop.extrabutterny.com/collections/all/products.json",
				"https://bdgastore.com/collections/all/products.json",
				"https://cncpts.com/collections/all/products.json",
				"https://www.notre-shop.com/collections/all/products.json",
				"https://juicestore.com/collections/all/products.json",
				"https://shop.havenshop.com/collections/all/products.json"};
	}
	public void setTotalPages(String pages, String key)
	{
		setTotalPagesImplementation(pages, key);
	}
	private void setTotalPagesImplementation(String pages, String key)
	{
		String[] str = Arrays.toString(webMap.get(key))
				.replaceAll("[\\[\\]]", "").split(",");
		str[1] = pages;
		str[2] = Integer.toString(calcPage250(Integer.parseInt(str[1])));
		webMap.replace(key, str);
	}
	private void loadCheckoutVariantImplementation(clientInfo info) throws InterruptedException
	{
		try
		{
			int delay = 150;
			WebDriverWait wait = new WebDriverWait(driver, 30);
			String checkoutURL = getUrl() + checkoutExtension + variant;
			driver.navigate().to(checkoutURL);
			wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
			String cart = driver.getTitle();
			WebElement t = driver.findElement(By.xpath("//main[@role='main']"));
			t.findElement(By.name("checkout")).click();
			wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
			PageRefresherFix tryAgain = new PageRefresherFix() {
				@Override
				public void run()
				{
					try 
					{
						WebElement r = driver.findElement(By.xpath("//main[@role='main']"));
						r.findElement(By.name("checkout")).click();
						wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
					}
					catch(StaleElementReferenceException e)
					{
						System.out.println("Stale Element Exception caught... Trying Again");
						driver.navigate().refresh();
						wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
						run();
					}				
				}
			};
			System.out.println(driver.getTitle());
			System.out.println(cart);
			if(driver.getTitle().toLowerCase().contains("cart"))
					tryAgain.run();
			System.out.println(driver.getTitle());
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
			driver.findElement(By.xpath("//input[@type='email']")).sendKeys(info.getEmail());
			Thread.sleep(delay);
			driver.findElement(By.xpath("//input[@name='checkout[shipping_address][first_name]']")).sendKeys(info.getFName());
			Thread.sleep(delay);
			driver.findElement(By.xpath("//input[@name='checkout[shipping_address][last_name]']")).sendKeys(info.getLName());
			Thread.sleep(delay);
			driver.findElement(By.xpath("//input[@name='checkout[shipping_address][address1]']")).sendKeys(info.getAddress());
			Thread.sleep(delay);
			driver.findElement(By.xpath("//input[@name='checkout[shipping_address][address2]']")).sendKeys(info.getAddress2());
			Thread.sleep(delay);
			driver.findElement(By.xpath("//input[@name='checkout[shipping_address][city]']")).sendKeys(info.getCity());
			Thread.sleep(delay);
			driver.findElement(By.xpath("//input[@name='checkout[shipping_address][country]']")).sendKeys(info.getCountry_Region());
			Thread.sleep(delay);
			driver.findElement(By.xpath("//input[@name='checkout[shipping_address][province]']")).sendKeys(info.getState());
			Thread.sleep(delay);
			driver.findElement(By.xpath("//input[@name='checkout[shipping_address][zip]']")).sendKeys(info.getZIP_code());
			Thread.sleep(delay);
			driver.findElement(By.xpath("//input[@name='checkout[shipping_address][phone]']")).sendKeys(info.getPhone());
			driver.findElement(By.xpath("//button[@class='step__footer__continue-btn btn']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='step__footer__continue-btn btn']")));
			driver.findElement(By.xpath("//button[@class='step__footer__continue-btn btn']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@class='card-fields-iframe']")));
			List<WebElement> paymentInfo = driver.findElements(By.xpath("//iframe[@class='card-fields-iframe']"));
			driver.switchTo().frame(paymentInfo.get(0));
			Thread.sleep(600);
			driver.findElement(By.xpath("//input[@autocomplete='cc-number']")).sendKeys(info.getCCNumber());
			Thread.sleep(delay);
			driver.switchTo().parentFrame();
			driver.switchTo().frame(paymentInfo.get(1));
			driver.findElement(By.xpath("//input[@autocomplete='cc-name']")).sendKeys(info.getCCName());
			Thread.sleep(delay);
			driver.switchTo().parentFrame();
			driver.switchTo().frame(paymentInfo.get(2));
			driver.findElement(By.xpath("//input[@autocomplete='cc-exp']")).sendKeys(info.getCCExp());
			Thread.sleep(delay);
			driver.switchTo().parentFrame();
			driver.switchTo().frame(paymentInfo.get(3));
			driver.findElement(By.xpath("//input[@autocomplete='cc-csc']")).sendKeys(info.getCsc());
			Thread.sleep(delay);
			driver.switchTo().parentFrame();
			driver.findElement(By.xpath("//button[@class='step__footer__continue-btn btn']")).click();	
		}
		catch(TimeoutException  | StaleElementReferenceException e)	
		{
			System.out.println("Caught Error: "+e);
			driver.quit();
		}
		
	}
	public void loadCheckoutVariant(clientInfo info) throws InterruptedException
	{
		loadCheckoutVariantImplementation(info);
	}
	public void setVariant(String variant)
	{
		this.variant = variant;
		this.validVariant = true;
	}
	public String getVariant()
	{
		return variant;
	}
	public String getKeywords()
	{
		return keywords.toString();
	}
	public void setKeywords(String[] keywords)
	{
		this.keywords.clear();
		this.keywords.addAll(Arrays.asList(keywords));
	}
	public String getSizeOption()
	{
		return sizeOption;
	}
	public void setSizeOption(String sizeOption)
	{
		this.sizeOption = sizeOption.toLowerCase();
	}
	public boolean isValidVariant()
	{
		return validVariant;
	}
	public int[] getPriceRange()
	{
		return priceRange;
	}
	public void setPriceRange(int[] priceRange)
	{
		this.priceRange = priceRange;
	}
}