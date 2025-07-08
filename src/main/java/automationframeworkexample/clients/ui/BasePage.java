package automationframeworkexample.clients.ui;

import automationframeworkexample.clients.ui.pages.FavoritesPage;
import automationframeworkexample.clients.ui.pages.LivePage;
import automationframeworkexample.clients.ui.pages.SettingsPage;
import automationframeworkexample.clients.ui.pages.YoutubePage;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.NoSuchElementException;

import static automationframeworkexample.clients.ui.DriverManager.scrollToCenter;
import static automationframeworkexample.AppConstants.LONG_WAIT;
import static automationframeworkexample.AppConstants.SHORT_WAIT;
import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class BasePage {

    public final WebDriver driver;

    @FindBy(xpath = "//span[normalize-space()='Live']")
    protected WebElement liveLink;
    @FindBy(xpath = "//*[@data-role='sports-favorites-link' or @data-role='slideItem_favorite']")
    protected WebElement favoritesLink;
    @FindBy(xpath = "//a[@data-role='header-login-button']")
    protected WebElement loginLink;
    @FindBy(xpath = "//a[@data-role='header-register-button']")
    protected WebElement registerLink;
    @FindBy(xpath = "//a[@data-role='user-menu-item-settings-toggle']")
    protected WebElement settingsLink;
    @FindBy(css = "[data-role='user-logo-header']")
    private WebElement profileIcon;
    @FindBy(xpath = "//a[contains(@href,'https://www.youtube.com/@favbetua')]")
    private WebElement youtubeIcon;
    @FindBy(css = "[data-role='icon-notification-close']")
    private WebElement notificationClose;


    @Autowired
    private ApplicationContext ctx;

    @Autowired
    protected BasePage(DriverManager dm) {
        this.driver = dm.getDriver();
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    protected <T> T page(Class<T> type) {
        return ctx.getBean(type);
    }

    public LivePage navigateToLivePage() {
        closeNotificationIfPresent();
        logInfo("Navigate To Live Page");
        liveLink.click();
        return page(LivePage.class);
    }

    public YoutubePage openYoutubeFromFooter() {
        closeNotificationIfPresent();
        scrollToCenter(driver, youtubeIcon);

        String parent = driver.getWindowHandle();
        int handlesBefore = driver.getWindowHandles().size();

        youtubeIcon.click();                                       // opens new tab

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.getWindowHandles().size() > handlesBefore);

        String child = driver.getWindowHandles().stream()
                .filter(h -> !h.equals(parent))
                .findFirst()
                .orElseThrow();

        driver.switchTo().window(child);
        return page(YoutubePage.class);      // Spring-wired YouTube page object
    }

    public SettingsPage navigateToSettingsPage() {
        logInfo("Navigate To Settings Page");
        profileIcon.click();
        settingsLink.click();
        return page(SettingsPage.class);
    }

    public FavoritesPage navigateToFavoritesPage() {
        logInfo("Navigate To Favorites Page");
        favoritesLink.click();
        return page(FavoritesPage.class);
    }

    public void closeNotificationIfPresent() {
        try {
            new WebDriverWait(driver, SHORT_WAIT)
                    .until(d -> notificationClose.isDisplayed());

            logInfo("Close notification popup");
            notificationClose.click();

            new WebDriverWait(driver, SHORT_WAIT)
                    .until(ExpectedConditions.invisibilityOf(notificationClose));

        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException ignored) {
        }
    }

    public BasePage waitUntilUserIsLogged() {
        logInfo("Wait until login is confirmed");
        closeNotificationIfPresent();
        new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT.getSeconds())).pollingEvery(Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOf(profileIcon));
        logInfo("Login confirmed – profile link visible");
        return this;
    }
}
