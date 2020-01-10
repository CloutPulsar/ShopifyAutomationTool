import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestBrowser
{
	@BeforeTest //annotation
	public void initialBrowserTest()
	{
		System.out.println("Initial Test Commencing: Opening Browswer and Loading Webpage...");
		String url = "https://www.youtube.com/watch?v=Jdkrj2lDAEY";
		Browser brows = new Browser(url);
		//Assert that the URL was set.
		Assert.assertEquals(brows.getUrl(), url);
		
	}
	@Test
	public void sampleTest()
	{
		System.out.println("Dummy Test");
	}
}
