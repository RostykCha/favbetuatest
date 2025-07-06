package automationframeworkexample.clients.ui.pages;

import automationframeworkexample.clients.ui.BasePage;
import automationframeworkexample.clients.ui.dto.UserDto;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

public class HomePage extends BasePage {

    @FindBy(xpath = "//span[normalize-space()='Live']")
    private WebElement liveLink;

    public LivePage navigateToLivePage() {
        logInfo("Navigate To Live Page");

        liveLink.click();
        return new LivePage();
    }

    public LoginPage loginUser(UserDto userDto) {
        logInfo(String.format("Login user \"%s\"", userDto));

        liveLink.click();
        return new LoginPage();
    }
}
