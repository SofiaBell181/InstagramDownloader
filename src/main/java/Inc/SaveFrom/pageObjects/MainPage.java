package Inc.SaveFrom.pageObjects;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Inc.SaveFrom.abstractComponents.AbstractComponent;

public class MainPage extends AbstractComponent {

	WebDriver driver;
	public int expectedDownloads;
	ChromeOptions options;
	Actions action;

	public MainPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//button[@aria-label='Consent']")
	private WebElement btnConsent;

	@FindBy(id = "search-form-input")
	private WebElement igramInput;

	@FindBy(css = ".search-form__button")
	private WebElement btnDownload;

	@FindBy(className = "search-form__clipboard-clear")
	private WebElement btnClear;

	@FindBy(css = ".output-component")
	private WebElement searchResult;

	@FindBy(css = ".output-list__list")
	private WebElement outputList;

	@FindBy(css = ".output-profile")
	private WebElement outputProfile;

	@FindBy(className = "profile-media-list")
	private WebElement mediaListProfile;

	@FindBy(className = "user-info__username-text")
	private WebElement userName;

	@FindBy(xpath = "//button[@class='tabs-component__button' and contains(text(), 'stories')]")
	private WebElement btnProfileStories;

	@FindBy(xpath = "//button[@class='tabs-component__button' and contains(text(), 'highlights')]")
	private WebElement btnProfileHighlights;

	@FindBy(className = "highlights-component")
	private WebElement highlightsPosts;

	@FindBy(xpath = "//button[@class='tabs-component__button' and contains(text(), 'reels')]")
	private WebElement btnProfileReels;

	@FindBy(css = ".outputList")
	private WebElement logo;

	@FindBy(xpath = "//span[contains(text(),'Photo')]/parent::a")
	private WebElement menuPhoto;

	@FindBy(xpath = "//span[contains(text(),\"Story\")]/parent::a")
	private WebElement menuStory;

	@FindBy(xpath = "//span[contains(text(),'Reels')]/parent::a")
	private WebElement menuReels;

	@FindBy(xpath = "//span[contains(text(),'IGTV')]/parent::a")
	private WebElement menuIgtv;

	@FindBy(xpath = "//ins")
	private WebElement ads;

	@FindBy(className = "cursor-pointer")
	private WebElement goToMain;

	@FindBy(xpath = "//div[@class='loader-component__circles']")
	private WebElement loader;

    @FindBy(xpath = "//button[@class='fallback-popup__btn']")
    private WebElement btnFallback;

    private By btnhighlightPost = By.className("highlight__button");

    private By btnReadMore = By.xpath("//button[@class='output-list__caption-read-more']");

    private By btnsDownloadPost = By.linkText("Download");

    private By links = By.tagName("a");

    private By fc_overlay = By.className("fc-consent-root");

    private By errorMessage = By.className("error-message");

	public void closeAds() {
		hideAds();
	}

	public void acceptConsentData() {
		List<WebElement> overlays = driver.findElements(fc_overlay);
		if (!overlays.isEmpty()) {
			WebElement overlay = overlays.get(0);
			btnConsent.click();
			waitForElementToDisappear(overlay);
		}
	}

    public void fiilInput(String url) {
    	waitForElementToBeClickable(igramInput);
        if(!igramInput.getAttribute("value").isEmpty()) {
        	btnClear.click();
        }
        igramInput.sendKeys(url);
        btnDownload.click();
    }

    public String checkErrorMessage() {
        waitForElementToAppear(errorMessage);
        String message = driver.findElement(errorMessage).getText();
        return message;
    }
    
    public void goToExtension(){
    	waitForElementToBeClickable(btnFallback);
        btnFallback.click();
    }

	public boolean isUrlInputEmpty(String urlpost) {
		igramInput.sendKeys(urlpost);
		if (btnClear.getText().equalsIgnoreCase("Clear")) {
			btnClear.click();
		} else {
			throw new RuntimeException("Expected button text to be 'Clear' but was: '" + btnClear.getText() + "'");
		}
        return igramInput.getAttribute("value").isEmpty();
	}

	public void searchPostFile(String url) {
		igramInput.sendKeys(url);
		btnDownload.click();
		waitForElementToAppear(searchResult);
		waitForElementToAppear(outputList);

		scrollToElement();
		List<WebElement> btnsReadMore = driver.findElements(btnReadMore);
		if (!btnsReadMore.isEmpty()) {
			btnsReadMore.get(0).click();
		}
	}

	public int downloadPostFile() {
		List<WebElement> btnsDownload = driver.findElements(btnsDownloadPost);
		expectedDownloads = btnsDownload.size();

		if (expectedDownloads == 0) {
			throw new AssertionError("'Download' buttons not found");
		}
		for (WebElement button : btnsDownload) {
			button.click();
			if (driver.getCurrentUrl().contains("google_vignette")) {
				driver.navigate().back();
				button.click();
			}
		}
		return expectedDownloads;
	}

	public List<File> checkFolder(String downloadPath) {
		File dir = new File(downloadPath);
		long end = System.currentTimeMillis() + 30_000;

		while (System.currentTimeMillis() < end) {

			if (dir.exists()) {
				// get only finished files
				File[] files = dir.listFiles(f -> {
					String name = f.getName().toLowerCase();
					return !name.endsWith(".crdownload") && !name.endsWith(".tmp");
				});

				if (files != null && files.length == expectedDownloads) {
					System.out.println(files.length);
					return Arrays.asList(files);
				}
			}

			try {
				// wait before next check
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}
		throw new AssertionError(
				"Download failed. No file found in: " + downloadPath + ". Expected " + expectedDownloads + " files");
	}

	public String searchByProfileName(String profilename) {
		for (int i = 0; i < profilename.length(); i++) {
			igramInput.sendKeys(String.valueOf(profilename.charAt(i)));
		}
		btnDownload.click();
		waitForElementToAppear(searchResult);
		waitForElementToAppear(outputProfile);
		waitForElementsToAppear(btnsDownloadPost);

        return userName.getText();
	}

	public String goToStoryInProfile() {
		btnProfileStories.click();
		waitForElementToDisappear(loader);
		String error = null;
		List<WebElement> message = driver.findElements(errorMessage);
		if (!message.isEmpty()) {
			error = message.get(0).getText();
			System.out.println(error);
		} else {
			waitForElementToAppear(mediaListProfile);
		}

		return error;

	}

	public void goTobProfileHighlights() {
		btnProfileHighlights.click();
		waitForElementToAppear(highlightsPosts);
	}

	public void openSpecificHighlight(String highlightsName) {
		List<WebElement> highlightButtons = driver.findElements(btnhighlightPost);
		WebElement btn = highlightButtons.stream().filter(button -> button.getText().equalsIgnoreCase(highlightsName))
				.findFirst().orElse(null);
		btn.click();

		waitForElementToAppear(mediaListProfile);
	}

	public void goToProfileReels() {
		btnProfileReels.click();
		waitForElementToAppear(mediaListProfile);

	}

	public void clearInput() {
		igramInput.clear();
	}

	public void goToMainPage() {
		goToMain.click();
		waitForElementToAppear(ads);
		hideAds();
	}

	public void gotoPhoto() {
		menuPhoto.click();
		if (driver.getCurrentUrl().contains("google_vignette")) {
			driver.navigate().back();
			menuPhoto.click();
		}
		driver.navigate().refresh();
		scrollToElementShortly();
		waitForElementToAppear(ads);
		hideAds();
	}

	public void gotoStory() {
		menuStory.click();
		if (driver.getCurrentUrl().contains("google_vignette")) {
			driver.navigate().back();
			menuStory.click();
		}
		driver.navigate().refresh();
		scrollToElementShortly();
		waitForElementToAppear(ads);
		hideAds();
	}

	public void goToReels() {
		menuReels.click();
		if (driver.getCurrentUrl().contains("google_vignette")) {
			driver.navigate().back();
			menuReels.click();
		}
		driver.navigate().refresh();
		scrollToElementShortly();
		waitForElementToAppear(ads);
		hideAds();
	}

	public void goToIgtv() {
		menuIgtv.click();
		if (driver.getCurrentUrl().contains("google_vignette")) {
			driver.navigate().back();
			menuIgtv.click();
		}
		driver.navigate().refresh();
		scrollToElementShortly();
		waitForElementToAppear(ads);
		hideAds();
	}

}
