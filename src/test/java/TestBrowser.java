import java.io.IOException;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TestBrowser
{
	String url = "https://bdgastore.com";
	Browser brows = new Browser();
	@BeforeTest //annotation
	public void initialUrlTest() throws InterruptedException
	{
		System.out.println("Initial Test Commencing: Opening Browswer and Loading Webpage...");
		//Assert that the URL was set.
		brows.setUrl(url);
		Assert.assertEquals(brows.getUrl(), url);
	}
	@Test
	public void loadBrowserTest() throws IOException, InterruptedException
	{
		String[] info = {"edgargranados18@yahoo.com","Edgar","Steyermorrisontom","5700 Mack Road","",
				"Sacramento","United States","California","95823","9168556514"};
		clientInfo client = new clientInfo();
		client.setCCNumber("4342568595887414");
		client.setCCName("Reese Numss");
		client.setCCExp("0618");
		client.setCsc("159");
		brows.loadBrowser();
		System.out.println("Dummy Test");
		String[] t = new String[10];
		t[0] = "sandals";
		t[1] = "kaw";
		t[2] = "red";
		t[3] = "suicoke";
		t[t.length-6] ="apparel";
		t[t.length-5] ="gray";
		t[t.length-4] ="light gray";
		t[t.length-3] ="mens";
		t[t.length-2] = "new-arrivals";
		brows.setKeywords(info);
		//System.out.println(Arrays.toString(brows.getKeywords()));
		brows.setKeywords(t);
		String url = "https://shop.extrabutterny.com";
		brows.setUrl(url);
		int[] range = {80, 250};
		brows.setPriceRange(range);
		client.setFullinfo(info);
		brows.initiateSearch();
		if(brows.isValidVariant())
			brows.loadCheckoutVariant(client);
		//System.out.println(Arrays.toString(brows.getKeywords()));
		brows.closeBrowser();
	}
}
