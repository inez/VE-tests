import org.openqa.selenium.Keys;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

@Test
public class BasicTests extends BaseTest {

	/*
	@AfterMethod
	public void aaa() throws Exception {
		Thread.sleep(2500);
	}
	*/
	
	@Test(groups={"BasicTests"})
	public void insertTextByTypingIntoEmptyDocument() throws Exception {
		emptyDocument();
		showSelection(0);
		documentNode.sendKeys("Hello world! What are you up to?");
		documentNode.sendKeys(Keys.ESCAPE);
		assertEqualsJson(
				getHtmlSummaryFromEditor(),
				getHtmlSummaryFromHtml("<html><body><p>Hello world! What are you up to?</p></body></html>")
		);
	}
	
	@Test(groups={"BasicTests"})
	public void insertTextByTypingIntoNotEmptyDocument() throws Exception {
		loadDocument("<html><body><p>Lorem</p></body></html>");
		showSelection(6);
		documentNode.sendKeys(" ipsum dolor sit amet.");
		documentNode.sendKeys(Keys.ESCAPE);
		assertEqualsJson(
				getHtmlSummaryFromEditor(),
				getHtmlSummaryFromHtml("<html><body><p>Lorem ipsum dolor sit amet.</p></body></html>")
		);
	}

	@Test(groups={"BasicTests"})
	public void breakParagraphWithEnter() throws Exception {
		loadDocument("<html><body><p>Lorem ipsum dolor sit amet.</p></body></html>");
		showSelection(12);
		documentNode.sendKeys(Keys.RETURN); // break after "Lorem ipsum"
		documentNode.sendKeys(Keys.ESCAPE);
		showSelection(20);
		documentNode.sendKeys(Keys.RETURN);// break after "dolor"
		documentNode.sendKeys(Keys.ESCAPE);
		assertEqualsJson(
				getHtmlSummaryFromEditor(),
				getHtmlSummaryFromHtml("<html><body><p>Lorem ipsum</p><p> dolor</p><p> sit amet.</p></body></html>")
		);
	}

	@Test(groups={"BasicTests"})
	public void breakListItemWithEnter() throws Exception {
		loadDocument("<html><body><ul><li>item1item2item3</li></ul></body></html>");
		showSelection(8);
		documentNode.sendKeys(Keys.RETURN); // break after "item1"
		documentNode.sendKeys(Keys.ESCAPE);
		showSelection(17);
		documentNode.sendKeys(Keys.RETURN); // break after "item2"
		documentNode.sendKeys(Keys.ESCAPE);
		assertEqualsJson(
				getHtmlSummaryFromEditor(),
				getHtmlSummaryFromHtml("<html><body><ul><li>item1</li><li><p>item2</p></li><li><p>item3</p></li></ul></body></html>")
		);
	}

	@Test(groups={"BasicTests"})
	public void exitListWithDoubleReturn() throws Exception {
		loadDocument("<html><body><ul><li>item1</li><li>item2</li><li>item3</li></ul></body></html>");
		showSelection(26);
		documentNode.sendKeys(Keys.RETURN);
		documentNode.sendKeys(Keys.ESCAPE);
		documentNode.sendKeys(Keys.RETURN);
		documentNode.sendKeys(Keys.ESCAPE);
		documentNode.sendKeys("Bye!");
		documentNode.sendKeys(Keys.ESCAPE);
		assertEqualsJson(
				getHtmlSummaryFromEditor(),
				getHtmlSummaryFromHtml("<html><body><ul><li>item1</li><li>item2</li><li>item3</li></ul><p>Bye!</p></body></html>")
		);
	}

}