import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;

@Test
public class BasicTests {

	private WebDriver driver;

	@BeforeClass
	public void setUp() {
		driver = new FirefoxDriver();
		// TODO: Make url part of the configuration
		driver.get("http://192.168.142.128/mw/extensions/VisualEditor/demos/ve/?page=empty");
	}

	@Test(groups={"BasicTests"})
	public void testSeriousness() throws Exception {
		Thread.sleep(2000);
	}

	@AfterClass
	public void tearDown() {
		driver.close();
	}
}