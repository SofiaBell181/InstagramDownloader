package Inc.SaveFrom.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Inc.SaveFrom.pageObjects.MainPage;
import Inc.SaveFrom.testComponents.BaseTest;

public class DownloadFiles extends BaseTest {

	public String urlpost = "https://www.instagram.com/p/DUOEv2MgOZu/";
	public String profilename = "natgeo";
	public String highlightsName = "Penguins";
	public SoftAssert soft;
	public MainPage main;

	@Test
	public void checkInputField() {
		main = new MainPage(driver);
		main.acceptConsentData();
		Assert.assertTrue(main.isUrlInputEmpty(urlpost));
	}

	@Test(dataProvider = "getData")
	public void downloadVideoandPhoto(List<HashMap<String, String>> data) throws InterruptedException, IOException {
		List<HashMap<String, String>> videoPhotoData = data.stream().filter(row -> {
			String name = row.get("name");
			return name.contains("video") || name.contains("photo");
		}).toList();
		Assert.assertFalse(videoPhotoData.isEmpty(), "No video or photo data found in JSON");

		main = new MainPage(driver);
//		main.acceptConsentData();

		for (HashMap<String, String> row : videoPhotoData) {
			main.searchPostFile(row.get("url"));
			main.closeAds();
			main.downloadPostFile();
			List<File> downloadedFile = main.checkFolder(downloadPath);
			Assert.assertFalse(downloadedFile.isEmpty(), "No files downloaded");

			if (downloadedFile.size() == 1) {
				System.out.println(downloadedFile.get(0).getName());
			}
			FileUtils.cleanDirectory(new File(downloadPath));
			main.clearInput();
			if (!driver.getCurrentUrl().contains("photo")) {
				main.gotoPhoto();
			}

		}

	}

	@Test(dataProvider = "getData")
	public void downloadStory(List<HashMap<String, String>> data) throws InterruptedException, IOException {
		List<HashMap<String, String>> storiesData = data.stream().filter(row -> row.get("name").contains("stories"))
				.toList();
		Assert.assertFalse(storiesData.isEmpty(), "No stories data found in JSON");

		main = new MainPage(driver);
		main.acceptConsentData();
		main.gotoStory();

		for (HashMap<String, String> row : storiesData) {
			main.searchPostFile(row.get("url"));
			main.downloadPostFile();
			List<File> downloadedFile = main.checkFolder(downloadPath);
			Assert.assertFalse(downloadedFile.isEmpty(), "No files downloaded");

			if (downloadedFile.size() == 1) {
				System.out.println(downloadedFile.get(0).getName());
			}
			FileUtils.cleanDirectory(new File(downloadPath));
			main.clearInput();
		}
	}

	@Test(dataProvider = "getData")
	public void downloadReelsAndIgtv(List<HashMap<String, String>> data) throws InterruptedException, IOException {
		List<HashMap<String, String>> reelsIgtvData = data.stream().filter(row -> {
			String name = row.get("name");
			return name.contains("reels") || name.contains("igtv");
		}).toList();
		Assert.assertFalse(reelsIgtvData.isEmpty(), "No reels or igtv data found in JSON");

		main = new MainPage(driver);
		main.acceptConsentData();

		main.goToReels();

		for (HashMap<String, String> row : reelsIgtvData) {
			main.searchPostFile(row.get("url"));
			main.downloadPostFile();
			List<File> downloadedFile = main.checkFolder(downloadPath);
			Assert.assertFalse(downloadedFile.isEmpty(), "No files downloaded");

			if (downloadedFile.size() == 1) {
				System.out.println(downloadedFile.get(0).getName());
			}
			FileUtils.cleanDirectory(new File(downloadPath));
			main.clearInput();
			if (!driver.getCurrentUrl().contains("igtv")) {
				main.goToIgtv();
			}

		}
	}

	@Test
	public void downloadByProfile() throws IOException {
		soft = new SoftAssert();
		main = new MainPage(driver);
		main.acceptConsentData();
		String userNameText = main.searchByProfileName(profilename);
		Assert.assertTrue(userNameText.contains(profilename), "Username doesn't contain expected name");
		main.closeAds();
		main.downloadPostFile();
		List<File> downloadedFile = main.checkFolder(downloadPath);
		Assert.assertFalse(downloadedFile.isEmpty(), "No files downloaded");

		if (downloadedFile.size() == 1) {
			System.out.println(downloadedFile.get(0).getName());
		}
		FileUtils.cleanDirectory(new File(downloadPath));

		String errorMessage = main.goToStoryInProfile();
		if (errorMessage != null) {
			Assert.assertTrue(errorMessage
					.contains("It seems that there are no stories for the last 24 hours. Please try again later."));
		} else {
			main.downloadPostFile();
		}

		Assert.assertFalse(downloadedFile.isEmpty(), "No files downloaded");

		if (downloadedFile.size() == 1) {
			System.out.println(downloadedFile.get(0).getName());
		}
		FileUtils.cleanDirectory(new File(downloadPath));

		main.goTobProfileHighlights();
		main.openSpecificHighlight(highlightsName);

		main.goToProfileReels();
	}

	@DataProvider
	public Object[][] getData() throws IOException {
		List<HashMap<String, String>> data = getJsonDataToMap();
		return new Object[][] { { data } };
	}

}
