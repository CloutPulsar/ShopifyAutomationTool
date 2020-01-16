import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Browser implements BasicSiteFunctions, Checkout
{
	public final static  int productPerPage = 30;
	private final static String extension = "/collections/all/products.json?page=1&limit=250";
	private final static HashMap<String, String[]> webMap = new HashMap<String, String[]>();
	private final static ObjectMapper mapper = new ObjectMapper();
	private final static ChromeOptions options = new ChromeOptions();

	private ArrayList<String> keywords = new ArrayList<String>();
	private String url;
	private String variant;
	private WebDriver driver;
	private int totalPages;
	
	public static String[] sites;
	public Browser()
	{
		// The Following websites contain a products.json file, which allows for
		// easier monitoring
		sites = defaultSites();
		WebDriverManager.chromedriver().version("79.0.3945.36").setup();
		options.setExperimentalOption("excludeSwitches", new String[]
		{"enable-automation"});
		mapDefaultSites();
	}	
	//Maps the total page count with limit = 250 products per page for each supported Shoppify Website. 
	/*Total Pages:(as of 1/12/2020): 
	 * Kith = 270, 
	 * Undefeated = 36
	 * ExtraButterNY = 65
	 * BodegaStore = 414
	 * Cncpts = 51
	 * NotreShop = 28
	 * JuiceStore = 28
	 */
	public void mapDefaultSites()
	{

		webMap.put("kith",          new String[]{sites[0], "270",Integer.toString(calcPage250(270))});
		webMap.put("undefeated",    new String[]{sites[1], "36",Integer.toString(calcPage250(36))});
		webMap.put("extrabutterny", new String[]{sites[2], "65",Integer.toString(calcPage250(65))});
		webMap.put("bodegastore",   new String[]{sites[3], "414",Integer.toString(calcPage250(414))});
		webMap.put("cncpts", new String[]{sites[4], "51",Integer.toString(calcPage250(51))});
		webMap.put("notreshop", new String[]{sites[5], "28",Integer.toString(calcPage250(28))});
		webMap.put("juicestrore", new String[]{sites[6], "28",Integer.toString(calcPage250(28))});

	}
	public String getSites()
	{
		String str = "";
		for (String s : webMap.keySet())
		//removes the characters "[" "]" ","  from the Arrays.toString() method
			str = str + Arrays.toString(webMap.get(s)).replaceAll("[\\[\\],]", "") + "\n";
		return str;
	}
	private int calcPage250(int pages)
	{
		//Math.Ceiling implementation to round to the next highest number. i.e. (x + (n-1))/n
		return ((pages*30)+249)/250;
	}
	public void initiateSearch() throws IOException
	{
		String JSON = fetchJSONData(url);
		//Format the JSON Data
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		JsonNode jsonObject = mapper.readTree(JSON);
		/*------------------For Data Extraction Analysis----------------------*/
		String formattedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
		BufferedWriter writer = new BufferedWriter(new FileWriter("json.json"));
	    writer.write(formattedJson);
	    writer.close();
		/*------------------For Data Extraction Analysis----------------------*/
	    //Must follow this format in order to access variants of each product without creating
	    //more JsonNode instances: jsonObject.get("products").get(i).get("variants").toString()
	    //where i is the i-th product. 
		int tmpProductCounter = jsonObject.get("products").size();
		System.out.println(tmpProductCounter);
		if(tmpProductCounter < 250)
			totalPages = (tmpProductCounter + (productPerPage-1)) / productPerPage;
		productSearch(jsonObject, tmpProductCounter);
	}
	private void productSearch(JsonNode jsonObject, int productNum)
	{
		int productNumber = 0;
		int max = Integer.MIN_VALUE;
		int[] productIndex = new int[productNum];
		String[] handle,tags;
        long start = System.currentTimeMillis(); 
		for(JsonNode product : jsonObject.get("products"))
		{
			int count = 0;
			handle = product.get("handle").toString().toLowerCase().replace("\"","").split("-");
			tags = product.get("tags").toString().toLowerCase().replaceAll("[\\[\\]\\s\"]", "").split(",");
			ArrayList<String> productWords = new ArrayList<String>(Arrays.asList(handle));
			productWords.addAll(Arrays.asList(tags));
			int prd = productWords.size();
			for(String keyword : keywords)
			{
				if((float)count/prd >= .85)
					break;
				if(productWords.contains(keyword) && keyword != "")
				{	
					count++;
					productWords.remove(keyword);
				}
				if(max < count)
					max = count;
			}
			productIndex[productNumber] = count;
			productNumber++;
		}
		Multimap<Integer, Integer> products = ArrayListMultimap.create();
		for(int i =0; i < productIndex.length; i++)
			products.put(productIndex[i], i);
		Collection<Integer> maxMatches = products.get(max);
		Object[] matches = maxMatches.toArray();
		findVariantID(matches, matches.length);
		long end = System.currentTimeMillis(); 
        System.out.println("Page Search Performance Time: "+ (end-start)  +" ms");
	}
	private void findVariantID(Object[] matches, int length)
	{
		
		
	}
	public void setUrl(String url)
	{
		this.url = url + extension;
	}

	public String getUrl()
	{
		return url.replace(extension, "");
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
		String newUrl = url+appendPages+nextPage;
		String emptyProductObject = "{\"products\":[]}";
		String JSON = fetchJSONData(newUrl);
		//Binary Search
		while(JSON != emptyProductObject)
		{
			counter++;
			newUrl = url+appendPages+nextPage;
			JSON = fetchJSONData(newUrl);
		}
        long end = System.currentTimeMillis(); 
        System.out.println("Page Count Performance Time: "+ (end-start));
		return counter;
	}
	private String fetchJSONData(String url)
	{
		String data = "";
		try
		{
			String str;
			URL site = new URL(url);
			BufferedReader br = new BufferedReader(new InputStreamReader(site.openStream()));
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
		return new String[]{"https://kith.com/collections/all/products.json",
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
		String[] str = Arrays.toString(webMap.get(key)).replaceAll("[\\[\\]]", "").split(",");
		str[1] = pages;
		str[2] = Integer.toString(calcPage250(Integer.parseInt(str[1])));
		webMap.replace(key, str);	
	}
	private void loadCheckoutVariantImplementation(clientInfo info) throws InterruptedException
	{
		int delay = 150;
		WebDriverWait wait = new WebDriverWait(driver, 30);
		String checkoutURL = getUrl() + checkoutExtension + variant;
		driver.navigate().to(checkoutURL);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//main[@role='main']")));
		WebElement t = driver.findElement(By.xpath("//main[@role='main']"));
		t.findElement(By.name("checkout")).click();
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
	public void loadCheckoutVariant(clientInfo info) throws InterruptedException
	{
		loadCheckoutVariantImplementation(info);
	}
	public void setVariant(String variant)
	{
		this.variant = variant;	
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
}