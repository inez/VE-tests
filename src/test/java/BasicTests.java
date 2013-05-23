import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;

@Test
public class BasicTests {

	private WebDriver driver;

	@BeforeClass
	public void setUp() {
		// TODO: Browser should be part of the configuration or maven parameter
		if ( false ) {
			driver = new FirefoxDriver();
		} else {
			driver = new ChromeDriver();			
		}
		// TODO: Make url part of the configuration
		driver.get("http://192.168.142.128/mw/extensions/VisualEditor/demos/ve/?page=simple");
	}

	@Test(groups={"BasicTests"})
	public void simpleTyping() throws Exception {
		emptyDocument();
		showSelection(0);
		
		//
		// Interaction
		//
		WebElement documentNode = driver.findElement(By.className("ve-ce-documentNode"));
		documentNode.sendKeys("123");
		documentNode.sendKeys(Keys.RETURN);
		documentNode.sendKeys("456");
		
		//
		// Actual
		//
		String getActualJS =
				"return JSON.stringify(" +
					"ve.getDomElementSummary(" +
						"ve.dm.converter.getDomFromData(" +
							"ve.instances[0].model.documentModel.getFullData()," +
							"ve.instances[0].model.documentModel.getStore()," +
							"ve.instances[0].model.documentModel.getInternalList()" +
						")" +
					")"+
				");";
		String actualJson = (String) ((JavascriptExecutor) driver).executeScript(getActualJS);

		//
		// Expected
		//
		String getExpectedJs =
				"return JSON.stringify(" +
						"ve.getDomElementSummary( ve.createDocumentFromHTML( '<html><body><p>123</p><p>456</p></body></html>' ) )" +
				");";
		String expectedJson = (String) ((JavascriptExecutor) driver).executeScript(getExpectedJs);
		
		//
		// Compare + Assert
		//
		ObjectMapper mapper = new ObjectMapper();
		Object actual = mapper.readValue(actualJson, Object.class);
		Object expected = mapper.readValue(expectedJson, Object.class);
		//Assert.assertEquals(actual.equals(expected), true);
		Assert.assertEquals(actual, expected);
	}
	
	private void showSelection(int at) {
		showSelection(at, at);
	}
	
	private void showSelection(int from, int to) {
		String showSelectiontJS = "ve.instances[0].view.showSelection( new ve.Range( " + from + ", " + to + " ) );";
		((JavascriptExecutor) driver).executeScript(showSelectiontJS);
	}
	
	private void emptyDocument() {
		String emptyDocumentJS =
				"ve.instances[0].model.change(" + 
					"ve.dm.Transaction.newFromRemoval(" +
						"ve.instances[0].model.documentModel," +
							"new ve.Range( 0, ve.instances[0].model.documentModel.data.getLength() )" + 
					")" +
				");";
		((JavascriptExecutor) driver).executeScript(emptyDocumentJS);
	}

	@AfterClass
	public void tearDown() {
		driver.close();
	}
}