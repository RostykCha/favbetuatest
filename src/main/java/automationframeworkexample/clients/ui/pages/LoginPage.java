package automationframeworkexample.clients.ui.pages;

import automationframeworkexample.clients.ui.BasePage;
import automationframeworkexample.clients.ui.DriverManager;
import automationframeworkexample.clients.ui.dto.UserDto;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
@Scope("prototype")
public class LoginPage extends BasePage {

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    @Autowired
    public LoginPage(DriverManager driverManager) {
        super(driverManager);
        logInfo("Opened Login page");
    }

    public ProfilePage loginUser(UserDto userDto) {
        logInfo(String.format("Login as a user \"%s\"", userDto.getEmail()));

        logInfo(String.format("Type e-mail \"%s\"", userDto.getEmail()));
        emailField.clear();
        emailField.sendKeys(userDto.getEmail());

        logInfo("Type password");
        passField.clear();
        passField.sendKeys(userDto.getPass());

        logInfo("Click Login button");
        loginButton.click();

        waitUntilUserIsLogged();
        return page(ProfilePage.class);
    }
}
