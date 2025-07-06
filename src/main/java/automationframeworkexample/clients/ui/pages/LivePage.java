package automationframeworkexample.clients.ui.pages;

import automationframeworkexample.clients.ui.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

public class LivePage extends BasePage {

    @FindBy(name = "firstname")
    private WebElement nameField;



    public void fillContactForm(String name, String email, String message, String industry, String city) {
        logInfo("Fill Contact Form");
        switchToFrame();

        nameField.sendKeys(name);
        lastname.sendKeys(name);
        emailField.sendKeys(email);
        messageField.sendKeys(message);
        new Select(industryDropdown).selectByVisibleText(industry);
        cityField.sendKeys(city);
    }


}
