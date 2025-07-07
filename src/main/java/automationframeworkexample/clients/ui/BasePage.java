package automationframeworkexample.clients.ui;

import automationframeworkexample.clients.ui.pages.FavoritesPage;
import automationframeworkexample.clients.ui.pages.LivePage;
import automationframeworkexample.clients.ui.pages.RegisterPage;
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

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class BasePage {

    public final WebDriver driver;
    private final Duration SHORT_WAIT = Duration.ofSeconds(1);
    private final Duration LONG_WAIT = Duration.ofSeconds(2);
    @FindBy(xpath = "//span[normalize-space()='Live']")
    protected WebElement liveLink;
    @FindBy(xpath = "//*[@data-role='sports-favorites-link' or @data-role='slideItem_favorite']")
    protected WebElement favoritesLink;
    @FindBy(xpath = "//a[@data-role='header-login-button']")
    protected WebElement loginLink;
    @FindBy(xpath = "//a[@data-role='header-register-button']")
    protected WebElement registerLink;
    @FindBy(css = "[data-role='icon-notification-close']")
    private WebElement notificationClose;
    @Autowired
    private ApplicationContext ctx;

    @Autowired
    protected BasePage(DriverManager dm) {
        this.driver = dm.get();
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    protected <T> T page(Class<T> type) {
        return ctx.getBean(type);
    }

    public LivePage navigateToLivePage() {
        liveLink.click();
        return page(LivePage.class);
    }

    public FavoritesPage navigateToFavoritesPage() {
        favoritesLink.click();
        return page(FavoritesPage.class);
    }

    public void closeNotificationIfPresent() {
        try {
            new WebDriverWait(driver, SHORT_WAIT)
                    .until(d -> notificationClose.isDisplayed());

            logInfo("Close notification popup");
            notificationClose.click();

            new WebDriverWait(driver, LONG_WAIT)
                    .until(ExpectedConditions.invisibilityOf(notificationClose));

        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException ignored) {
        }
    }
}
