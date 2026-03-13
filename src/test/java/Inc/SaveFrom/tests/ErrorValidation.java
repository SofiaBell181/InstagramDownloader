package Inc.SaveFrom.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Inc.SaveFrom.pageObjects.MainPage;
import Inc.SaveFrom.testComponents.BaseTest;

public class ErrorValidation extends BaseTest {
	private String urlExtension = "https://en1.savefrom.net/1/userjs-for-google-chrome.php?vid=796&from=errorFallback&utm_source=i010104";
	private String urlDesktop = "https://televzr.com/en/clickunder-landing-v5?utm_source=i010101";

	private String textError = "You have entered the link to a private account. Please, try to use the link to a public account.";
	private String textErrorDesktop = "Sorry, we don’t support this site";
	private String textErrorExtension = "Link is not supported We couldn't download this content.";
	private String textErrorEmpty = "@username or link is required";

	@Test(dataProvider = "getInvalidData")
	public void IncompleteLink(List<HashMap<String, String>> data) {
		List<HashMap<String, String>> getRows = data.stream().filter(row -> {
			return row.containsKey("link");
		}).toList();
		MainPage main = new MainPage(driver);
		main.acceptConsentData();

		for (HashMap<String, String> row : getRows) {
			String link = row.get("link");
			System.out.println(link);
			main.fiilInput(link);
			String errorMes = main.checkErrorMessage();
			main.goToExtension();
			Set<String> links = driver.getWindowHandles();
			Iterator<String> item = links.iterator();
			String mainLink = item.next();
			String childLink = item.next();
			driver.switchTo().window(childLink);
			if(errorMes.contains(textErrorExtension)) {
				Assert.assertTrue(driver.getCurrentUrl().contains(urlExtension),
					"Expected URL to be " + urlExtension + "actual link is " + driver.getCurrentUrl() + "");
			}
			else if (errorMes.contains(textErrorDesktop)) {
				Assert.assertTrue(driver.getCurrentUrl().contains(urlDesktop),
						"Expected URL to be " + urlDesktop + "actual link is " + driver.getCurrentUrl() + "");
			}
			
			driver.close();
			driver.switchTo().window(mainLink);

			if (driver.getCurrentUrl().contains("google_vignette")) {
				driver.navigate().back();
			}
		}

	}

	@Test(dataProvider = "getInvalidData")
	public void privateLink(List<HashMap<String, String>> data) {
		MainPage main = new MainPage(driver);
		main.acceptConsentData();
		String getUrl = data.stream().filter(row -> row.containsKey("privatelink")).findFirst().orElse(null)
				.get("privatelink");
		main.fiilInput(getUrl);
		String textMessage = main.checkErrorMessage();
		Assert.assertTrue(textMessage.contains(textError), "Error message doesn't contain text" + textError);
	}
	
	@Test
	public void emptyRequest() {
		MainPage main = new MainPage(driver);
		main.acceptConsentData();
		main.fiilInput("");
		String textMessage = main.checkErrorMessage();
		Assert.assertTrue(textMessage.contains(textErrorEmpty), "Error message doesn't contain text " + textErrorEmpty);
		String errorMes = main.checkErrorMessage();
		main.goToExtension();
		Set<String> links = driver.getWindowHandles();
		Iterator<String> item = links.iterator();
		String mainLink = item.next();
		String childLink = item.next();
		driver.switchTo().window(childLink);
		if(errorMes.contains(textErrorExtension)) {
			Assert.assertTrue(driver.getCurrentUrl().contains(urlExtension),
				"Expected URL to be " + urlExtension + "actual link is " + driver.getCurrentUrl() + "");
		}
		driver.close();
		driver.switchTo().window(mainLink);

		if (driver.getCurrentUrl().contains("google_vignette")) {
			driver.navigate().back();
		}
	}

	@DataProvider
	public Object[][] getInvalidData() throws IOException {
		List<HashMap<String, String>> data = getJsonDataToMap(
				"\\src\\test\\java\\Inc\\SaveFrom\\data\\invalidLinks.json");
		return new Object[][] { { data } };
	}
}
