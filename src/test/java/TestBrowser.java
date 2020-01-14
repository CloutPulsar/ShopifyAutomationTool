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
		clientInfo client = new clientInfo();
		while(true)
		{
		brows.loadBrowser();
		System.out.println("Dummy Test");
		brows.initiateSearch();
		String[] test = brows.getSites().split("\n");
		for(String s : test)
			System.out.println(s);
		brows.setTotalPages("100", "kith");
		System.out.println(brows.getSites());
		String url = "https://kith.com";
		brows.setUrl(url);
		String var = "19436289851520";
		brows.setVariant(var);
		System.out.println(brows.getVariant());
		System.out.println(brows.getUrl());
		brows.loadCheckoutVariant(client);
		Thread.sleep(1000);
		brows.closeBrowser();
		}
	}
}
