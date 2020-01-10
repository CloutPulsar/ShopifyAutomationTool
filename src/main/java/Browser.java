import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Browser implements BasicSiteFunctions
{
	private String url;
	private WebDriver driver;
	
	public Browser(String url) 
	{
		setUrl(url);
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
		driver = new ChromeDriver();
		driver.get(url);
	}
	public void closeBrowser()
	{
		driver.quit();
	}
}