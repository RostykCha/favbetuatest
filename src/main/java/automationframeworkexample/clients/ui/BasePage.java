package automationframeworkexample.clients.ui;

import automationframeworkexample.clients.ui.pages.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static automationframeworkexample.clients.ui.utils.AppConstants.FAVBET_BASE_URL;
import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BasePage {

    private final WebDriver driver;

    public BasePage() {
        driver = WebDriverWrapper.getChromeDriver();
        PageFactory.initElements(driver, this);
    }

    public HomePage openHomePage() {
        logInfo("Open Home Page");
        driver.get(FAVBET_BASE_URL);
        HomePage homePage = new HomePage();
        return homePage;
    }

    public void closeDrivers() {
        logInfo("Close Drivers");
        driver.quit();
    }

    public WebDriver switchToFrame() {
        driver.switchTo().defaultContent();
        driver.switchTo().frame(0);
        return driver;
    }

}
