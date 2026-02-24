package Inc.SaveFrom.abstractComponents;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractComponent {
	WebDriver driver;
	WebDriverWait wait;
	JavascriptExecutor js;
	
	public AbstractComponent(WebDriver driver) {
		this.driver = driver;
	}


	public void waitForElementsToAppear(By FindBy) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(7));
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(FindBy));
	}
	
	public void waitForWebElementToAppear(WebElement element) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(7));
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	public void waitForElementToDisappear(WebElement element) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOf(element));
	}
	
	public void waitForWebElementToBeClickable(WebElement element) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	public void hideAds() {
		js = (JavascriptExecutor) driver;
		js.executeScript("""
				    document.querySelectorAll('iframe, ins').forEach(e => {
				        e.style.display='none';
				        e.style.visibility='hidden';
				        e.style.pointerEvents='none';
				    });
				""");
	}
	
	public void scrollToElement() {
		js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, 600)");
	}
	
	public void scrollToElementShortly() {
		js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, 150)");
	}
	
}
