import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Browser implements BasicSiteFunctions
{
	private String url;
	private WebDriver driver;
	public String[] sites;
	private HashMap<String, String> webMap = new HashMap();
	private static ChromeOptions options = new ChromeOptions();
	public Browser(String url)
	{
		// The Following websites contain a products.json file, which allows for
		// easier monitoring
		sites = defaultSites();
		WebDriverManager.chromedriver().version("79.0.3945.36").setup();
		options.setExperimentalOption("excludeSwitches", new String[]
		{"enable-automation"});
		setUrl(url);
	}
	private String fetchJSONData()
	{
		String data = "";
		try
		{
			String str;
			URL url = new URL(this.url);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			while (null != (str = br.readLine()))
				data += str;
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return data;
	}
	public void initiateSearch() throws IOException
	{
		String JSON = fetchJSONData();
		ObjectMapper mapper = new ObjectMapper();
		// pretty print
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		JsonNode jsonObject = mapper.readTree(JSON);
		//String formattedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
		String Handles = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject.findValues("handle"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("json.txt"));
		System.out.println(jsonObject.findValues("handle"));
	    writer.write(Handles.replaceAll(", ", "\n"));
	     
	    writer.close();
	}
	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getUrl()
	{
		return url;
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
	private String[] defaultSites()
	{
		return new String[]
		{"https://kith.com/products.json",
				"https://undefeated.com/products.json",
				"https://shop.extrabutterny.com/products.json",
				"https://bdgastore.com/products.json",
				"https://cncpts.com/products.json",
				"https://www.notre-shop.com/products.json",
				"https://juicestore.com/products.json"};
	}
}