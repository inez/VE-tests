import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
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
		Range range = getSelection();
		Assert.assertTrue(range.isCollapsed());
		Assert.assertEquals(range.from, 33);
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
		Range range = getSelection();
		Assert.assertTrue(range.isCollapsed());
		Assert.assertEquals(range.from, 28);
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
		Range range = getSelection();
		Assert.assertTrue(range.isCollapsed());
		Assert.assertEquals(range.from, 22);
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
		Range range = getSelection();
		Assert.assertTrue(range.isCollapsed());
		Assert.assertEquals(range.from, 21);
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
		Range range = getSelection();
		Assert.assertTrue(range.isCollapsed());
		Assert.assertEquals(range.from, 34);
		assertEqualsJson(
				getHtmlSummaryFromEditor(),
				getHtmlSummaryFromHtml("<html><body><ul><li>item1</li><li>item2</li><li>item3</li></ul><p>Bye!</p></body></html>")
		);
	}

	@Test(groups={"BasicTests"})
	public void preAnnotationsWithTyping() throws Exception {
		WebElement boldButton = driver.findElement(By.className("ve-ui-icon-bold-b"));
		loadDocument("<html><body><p>ipsumsit</p></body></html>");
		showSelection(1);
		boldButton.click();
		documentNode.sendKeys("Lorem ");
		showSelection(12);
		boldButton.click();
		documentNode.sendKeys(" dolor ");
		showSelection(22);
		boldButton.click();
		documentNode.sendKeys(" amet.");

		Range range = getSelection();
		Assert.assertTrue(range.isCollapsed());
		Assert.assertEquals(range.from, 28);
		assertEqualsJson(
				getHtmlSummaryFromEditor(),
				getHtmlSummaryFromHtml("<html><body><p><b>Lorem </b>ipsum<b> dolor </b>sit<b> amet.</b></p></body></html>")
		);
	}

	// https://bugzilla.wikimedia.org/show_bug.cgi?id=43082
	@Test(groups={"BasicTests"})
	public void moveCursorNativelyAndBreak() throws Exception {
		loadDocument("<html><body><p>This!</p></body></html>");
		showSelection(5);
		documentNode.sendKeys(Keys.HOME);
		documentNode.sendKeys(Keys.RETURN);
		assertEqualsJson(
				getHtmlSummaryFromEditor(),
				getHtmlSummaryFromHtml("<html><body><p></p><p>This!</p></body></html>")
		);
	}
	
}