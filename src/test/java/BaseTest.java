import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

	protected WebDriver driver;
	
	protected WebElement documentNode;
	
	protected ObjectMapper mapper = new ObjectMapper();

	@BeforeClass
	public void setUp() {
		driver = new ChromeDriver();
		//driver = new FirefoxDriver();
		driver.get("http://192.168.142.128/mw/extensions/VisualEditor/demos/ve/?page=simple");
		documentNode = driver.findElement(By.className("ve-ce-documentNode"));
	}
	
	@AfterClass
	public void tearDown() {
		driver.close();
	}
	
	protected void showSelection(int at) {
		showSelection(at, at);
	}
	
	protected void showSelection(int from, int to) {
		String showSelectiontJS = "ve.instances[0].model.change( null, new ve.Range( " + from + ", " + to + " ) );";
		((JavascriptExecutor) driver).executeScript(showSelectiontJS);
	}
	
	protected void emptyDocument() {
		String emptyDocumentJS =
				"ve.instances[0].model.change(" + 
					"ve.dm.Transaction.newFromRemoval(" +
						"ve.instances[0].model.documentModel," +
							"new ve.Range( 0, ve.instances[0].model.documentModel.data.getLength() )" + 
					")" +
				");";
		((JavascriptExecutor) driver).executeScript(emptyDocumentJS);
	}
	
	protected void loadDocument(String html) {
		emptyDocument();
		String loadDocumentJS =
				"var store = new ve.dm.IndexValueStore();" + 
				"var internalList = new ve.dm.InternalList();" +
				"var data = ve.dm.converter.getDataFromDom( ve.createDocumentFromHTML( '" + html + "' ), store, internalList ).getData();" +
				"ve.instances[0].model.change( ve.dm.Transaction.newFromInsertion( ve.instances[0].model.documentModel, 0, data ) );";
		((JavascriptExecutor) driver).executeScript(loadDocumentJS);
	}
	
	protected String getHtmlSummaryFromHtml(String html) {
		String js =
				"return JSON.stringify(" +
						"ve.getDomElementSummary( ve.createDocumentFromHTML( '" + html + "' ) )" +
				");";
		return (String) ((JavascriptExecutor) driver).executeScript(js);
		
	}
	
	protected String getHtmlSummaryFromEditor() {
		String js =
				"return JSON.stringify(" +
					"ve.getDomElementSummary(" +
						"ve.dm.converter.getDomFromData(" +
							"ve.instances[0].model.documentModel.getFullData()," +
							"ve.instances[0].model.documentModel.getStore()," +
							"ve.instances[0].model.documentModel.getInternalList()" +
						")" +
					")"+
				");";
		return (String) ((JavascriptExecutor) driver).executeScript(js);
	}
	
	protected Range getSelection() throws Exception {
		String selectionString = (String) ((JavascriptExecutor) driver).executeScript("return JSON.stringify(ve.instances[0].model.selection);");
		return mapper.readValue(selectionString, Range.class);		
	}
	
	protected void assertEqualsJson(String actualJson, String expectedJson) throws Exception {
		/*
		System.out.println("------------------");
		System.out.println("actualJson" + actualJson);
		System.out.println("------------------");
		System.out.println("expectedJson" + expectedJson);
		System.out.println("------------------");
		*/
		Object actual = mapper.readValue(actualJson, Object.class);
		Object expected = mapper.readValue(expectedJson, Object.class);
		Assert.assertEquals(actual, expected);
	}

}