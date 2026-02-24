package Inc.SaveFrom.tests;

import java.util.List;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Inc.SaveFrom.pageObjects.MainPage;
import Inc.SaveFrom.testComponents.BaseTest;
import Inc.SaveFrom.utils.LinkValidator;

public class LinkHealthTest extends BaseTest {
	public SoftAssert soft;
	public MainPage main;
	public LinkValidator validator;

	@Test
	public void mainPageLinks() {
		soft = new SoftAssert();
		main = new MainPage(driver);
		validator = new LinkValidator();
		goToMainPage();
		main.acceptConsentData();
		
		List<String> brokenlinksMainPage = validator.findBrokenLinks(getLinks());
		soft.assertTrue(brokenlinksMainPage.isEmpty(),
				"Main page broken links: " + String.join("\n", brokenlinksMainPage));
		soft.assertAll();

	}

	@Test
	public void photoPageLinks() {
		soft = new SoftAssert();
		main = new MainPage(driver);
		validator = new LinkValidator();
		main.acceptConsentData();

		main.gotoPhoto();
		List<String> brokenlinksPhotoPage = validator.findBrokenLinks(getLinks());
		soft.assertTrue(brokenlinksPhotoPage.isEmpty(),
				"Broken links found:" + String.join("\n", brokenlinksPhotoPage));
		soft.assertAll();
	}

	@Test
	public void storyPageLinks() {
		soft = new SoftAssert();
		main = new MainPage(driver);
		validator = new LinkValidator();
		main.acceptConsentData();

		main.gotoStory();
		List<String> brokenlinksStoryPage = validator.findBrokenLinks(getLinks());
		soft.assertTrue(brokenlinksStoryPage.isEmpty(),
				"Broken links found:" + String.join("\n", brokenlinksStoryPage));
		soft.assertAll();
	}

	@Test
	public void reelsPageLinks() {
		soft = new SoftAssert();
		main = new MainPage(driver);
		validator = new LinkValidator();
		main.acceptConsentData();

		main.goToReels();
		List<String> brokenlinksReelsPage = validator.findBrokenLinks(getLinks());
		soft.assertTrue(brokenlinksReelsPage.isEmpty(),
				"Broken links found:" + String.join("\n", brokenlinksReelsPage));
		soft.assertAll();
	}

	@Test
	public void igtvPageLinks() {
		soft = new SoftAssert();
		main = new MainPage(driver);
		validator = new LinkValidator();
		main.acceptConsentData();

		main.goToIgtv();
		List<String> brokenlinksIgtvPage = validator.findBrokenLinks(getLinks());
		soft.assertTrue(brokenlinksIgtvPage.isEmpty(), "Broken links found:" + String.join("\n", brokenlinksIgtvPage));
		soft.assertAll();

	}
}
