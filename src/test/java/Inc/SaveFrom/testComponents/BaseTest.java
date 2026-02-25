package Inc.SaveFrom.testComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class BaseTest {
	private Properties prop;
	public WebDriver driver;
	public ChromeOptions options;
	public String downloadPath; 

	public WebDriver initializeDriver() {
		String browser = prop.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {
			downloadPath = System.getProperty("user.dir") + "\\files";
			options = new ChromeOptions();
			Map<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("download.default_directory", downloadPath);
			options.setExperimentalOption("prefs", chromePrefs);

			driver = new ChromeDriver(options);
			
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		}

		else if (browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		return driver;
	}

	public String getUrl() {
		String url = prop.getProperty("url");
		if (url == null)
			throw new RuntimeException("url property missing in config.properties");
		return url;
	}

	public String makeScreenshoot(String testCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot)driver;
		File screenshotFile = ts.getScreenshotAs(OutputType.FILE);
//		File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destFile = new File(System.getProperty("user.dir") + "\\screenshots\\" + testCaseName + timestamp() + ".png");
		FileUtils.copyFile(screenshotFile, destFile);

		return System.getProperty("user.dir") + "\\screenshots\\" +  testCaseName + timestamp() + ".png";
	}

	public String timestamp() {
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	}

	public List<HashMap<String, String>> getJsonDataToMap() throws IOException {
		String jsonContent = FileUtils.readFileToString(
				new File(System.getProperty("user.dir") + "\\src\\test\\java\\Inc\\SaveFrom\\Data\\data.json"),
				StandardCharsets.UTF_8);
		JsonMapper mapper = new JsonMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {
				});
		return data;
	}
	
	public List<WebElement> getLinks() {
		List<WebElement> listOfLinks = driver.findElements(By.xpath("//body//a[@href]"));
		return listOfLinks;
	}
	
	public void goToMainPage() {
		driver.get(getUrl());
	}

	@BeforeClass (alwaysRun = true)
	public void setUp() throws IOException, InterruptedException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\Inc\\SaveFrom\\resources\\GlobalData.properties");
		prop.load(fis);

		driver = initializeDriver();
		driver.get(getUrl());
	}

	
	@AfterClass (alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
