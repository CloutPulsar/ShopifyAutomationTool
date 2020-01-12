import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonObject;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Browser implements BasicSiteFunctions
{
	public String[] sites;
	private String url;
	private final String extension = "/collections/all/products.json?page=1&limit=250";
	private WebDriver driver;
	private int totalPages;
	private HashMap<String, String> webMap = new HashMap();
	private ObjectMapper mapper = new ObjectMapper();
	private static ChromeOptions options = new ChromeOptions();
	public Browser()
	{
		// The Following websites contain a products.json file, which allows for
		// easier monitoring
		sites = defaultSites();
		WebDriverManager.chromedriver().version("79.0.3945.36").setup();
		options.setExperimentalOption("excludeSwitches", new String[]
		{"enable-automation"});
	}	
	public void initiateSearch() throws IOException
	{
		String JSON = fetchJSONData(url);
		//Format the JSON Data
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		JsonNode jsonObject = mapper.readTree(JSON);
		
		/*------------------For Data Extraction Analysis----------------------*/
		//String formattedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
		//BufferedWriter writer = new BufferedWriter(new FileWriter("json.json"));
	    //writer.write(formattedJson);
	    //writer.close();
		/*------------------For Data Extraction Analysis----------------------*/
	    //Must follow this format in order to access variants of each product without creating
	    //more JsonNode instances: jsonObject.get("products").get(i).get("variants").toString()
	    //where i is the i-th product. 
		
		System.out.println(jsonObject.get("products").size());
		int tmpProductCounter = jsonObject.get("products").size();
		if(tmpProductCounter < 250)
			totalPages = (tmpProductCounter + (30-1)) / 30;
		else 
			totalPages = pageCount();		
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
		//Binary Search
		
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
	private String[] defaultSites()
	{
		return new String[]{"https://kith.com/collections/all/products.json",
				"https://undefeated.com/collections/all/products.json",
				"https://shop.extrabutterny.com/collections/all/products.json",
				"https://bdgastore.com/collections/all/products.json",
				"https://cncpts.com/collections/all/products.json",
				"https://www.notre-shop.com/collections/all/products.json",
				"https://juicestore.com/collections/all/products.json"};
	}
}