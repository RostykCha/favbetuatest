package automationframeworkexample.clients.ui.pages;

import automationframeworkexample.clients.ui.BasePage;
import automationframeworkexample.clients.ui.DriverManager;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static automationframeworkexample.clients.ui.utils.AppConstants.FAVBET_BASE_URL;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HomePage extends BasePage {

    public HomePage(DriverManager dm) {
        super(dm);
    }

    public LoginPage navigateToLoginPage() {
        closeNotificationIfPresent();
        loginLink.click();
        return page(LoginPage.class);
    }

    public RegisterPage navigateToRegisterPage() {
        closeNotificationIfPresent();
        registerLink.click();
        return page(RegisterPage.class);
    }

    public HomePage openHomePage() {
        driver.get(FAVBET_BASE_URL);
        return this;
    }
}
