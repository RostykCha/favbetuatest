package automationframeworkexample.clients.ui.pages;

import automationframeworkexample.clients.ui.BasePage;
import automationframeworkexample.clients.ui.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

import static automationframeworkexample.clients.ui.DriverManager.scrollToCenter;
import static automationframeworkexample.clients.ui.DriverManager.switchToFirstAvailableFrame;
import static automationframeworkexample.AppConstants.LONG_WAIT;
import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
@Scope("prototype")
public class YoutubePage extends BasePage {
    @FindBy(xpath = "//button[@aria-label='Accept all' or .//span[normalize-space()='Accept all']]")
    private WebElement acceptAllButton;

    @FindBy(xpath = "//span[@role='text' and text()='Favbet UA']")
    private WebElement channelName;

    @FindBy(xpath = "//input[@id='search' or @name='search_query' or @name='query']")
    private WebElement searchField;

    @FindBy(xpath = "//ytd-video-renderer//a[@id='video-title']")
    private List<WebElement> videoTitleList;

    protected YoutubePage(DriverManager dm) {
        super(dm);
    }

    public YoutubePage acceptCookies() {
        logInfo("Accept Cookies");

        switchToFirstAvailableFrame(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        wait.until(ExpectedConditions.elementToBeClickable(acceptAllButton));
        scrollToCenter(driver, acceptAllButton);
        acceptAllButton.click();
        return this;
    }

    public YoutubePage checkVideoIsPresent(String videoName) {
        logInfo(String.format("Search YouTube for \"%s\"", videoName));
        logInfo("Wait until search field is clickable");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(searchField));
        logInfo("Scroll search field into view");
        scrollToCenter(driver, searchField);
        logInfo("Focus search field via JavaScript click");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchField);
        logInfo("Clear any existing text");
        searchField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        logInfo(String.format("Type query \"%s\" and press ENTER", videoName));
        searchField.sendKeys(videoName + Keys.ENTER);
        logInfo("Wait for search results to appear");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> !videoTitleList.isEmpty());
        logInfo(String.format("Total videos found: %d", videoTitleList.size()));
        boolean found = videoTitleList.stream()
                .peek(el -> logInfo("Video title: " + el.getText()))
                .map(WebElement::getText)
                .anyMatch(t -> t.toLowerCase().contains(videoName.toLowerCase()));
        logInfo(found
                ? String.format("Video \"%s\" FOUND in the results", videoName)
                : String.format("Video \"%s\" NOT found in the results", videoName));
        org.junit.jupiter.api.Assertions.assertTrue(
                found,
                String.format("Video \"%s\" not present in the results", videoName));
        return this;
    }

    public YoutubePage verifyYoutubeChannelName() {
        logInfo("Verify YouTube channel name");
        new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT.getSeconds()))
                .until(ExpectedConditions.visibilityOf(channelName));
        logInfo("YouTube channel name is Favbet UA");
        return this;
    }

}
