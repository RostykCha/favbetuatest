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
public class RegisterPage extends BasePage {

    @FindBy(xpath = "//input[@data-role='register-email-input']")
    private WebElement emailField;
    @FindBy(xpath = "//input[@data-role='register-password-input']")
    private WebElement passField;
    @FindBy(xpath = "//button[@data-role='register-submit-button']")
    private WebElement registerButton;

    @Autowired
    public RegisterPage(DriverManager driverManager) {
        super(driverManager);
        logInfo("Opened Register page");
    }

    public RegisterPage registerUser(UserDto userDto) {
        logInfo(String.format("Type e-mail \"%s\"", userDto.getEmail()));
        emailField.clear();
        emailField.sendKeys(userDto.getEmail());

        logInfo("Type password");
        passField.clear();
        passField.sendKeys(userDto.getPass());

        logInfo("Click Register button");
        registerButton.click();

        return this;
    }
}
