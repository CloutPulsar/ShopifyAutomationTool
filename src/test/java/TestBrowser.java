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
		brows.loadBrowser();
		System.out.println("Dummy Test");
		String[] t = new String[1000009];
		for (int i =0; i < 1000000; i++)
			t[i] = "shirt";
		t[t.length-6] ="apparel";
		t[t.length-5] ="gray";
		t[t.length-4] ="light gray";
		t[t.length-3] ="mens";
		t[t.length-2] = "new-arrivals";
		brows.setKeywords(info);
		//System.out.println(Arrays.toString(brows.getKeywords()));
		brows.setKeywords(t);
		brows.initiateSearch();
		//System.out.println(Arrays.toString(brows.getKeywords()));
		Thread.sleep(1000);
		brows.closeBrowser();
	}
}
