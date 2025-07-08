package automationframeworkexample.clients.ui.pages;

import automationframeworkexample.clients.ui.BasePage;
import automationframeworkexample.clients.ui.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static automationframeworkexample.clients.ui.DriverManager.scrollToCenter;
import static automationframeworkexample.clients.ui.utils.AppConstants.SHORT_WAIT;
import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
@Scope("prototype")
public class SettingsPage extends BasePage {
    private static final String THEME_JS =
            // 1️⃣ newest implementation – extra class on <body>
            "if (document.body.classList.contains('dark'))  return 'dark';" +
                    "if (document.body.classList.contains('light')) return 'light';" +
                    // 2️⃣ fallback – legacy  data-theme  attribute (old builds / SSR flashes)
                    "return document.documentElement.getAttribute('data-theme') || " +
                    "       document.body.getAttribute     ('data-theme');";
    @FindBy(xpath = "//div[@data-role='settings-language-select-trigger']")
    private WebElement languageSettings;
    @FindBy(xpath = "//div[@data-role='option-en']")
    private WebElement englishLanguage;
    @FindBy(xpath = "//div[@data-role='option-uk']")
    private WebElement ukrainianLanguage;
    @FindBy(xpath = "//div[@data-role='settings-color-scheme-switcher-dark']")
    private WebElement darkMode;
    @FindBy(xpath = "//div[@data-role='settings-color-scheme-switcher-light']")
    private WebElement lightMode;

    protected SettingsPage(DriverManager dm) {
        super(dm);
    }

    public SettingsPage cnahgeToUkrainianAndVerify() {
        logInfo("Switch language to Ukrainian");
        languageSettings.click();
        new WebDriverWait(driver, SHORT_WAIT)
                .until(ExpectedConditions.elementToBeClickable(ukrainianLanguage))
                .click();
        new WebDriverWait(driver, SHORT_WAIT)
                .until(d -> languageSettings.getText().toLowerCase().contains("україн"));
        logInfo("Language set to Ukrainian");
        return this;
    }

    public SettingsPage cnahgeToEnglishAndVerify() {
        logInfo("Switch language to English");
        languageSettings.click();
        new WebDriverWait(driver, SHORT_WAIT)
                .until(ExpectedConditions.elementToBeClickable(englishLanguage))
                .click();
        new WebDriverWait(driver, SHORT_WAIT)
                .until(d -> languageSettings.getText().toLowerCase().contains("english"));
        logInfo("Language set to English");
        return this;
    }

    public SettingsPage changeToDarkThemeAndVerify() {
        logInfo("Switch colour scheme to Dark");
        scrollToCenter(driver, darkMode);
        darkMode.click();

        new WebDriverWait(driver, SHORT_WAIT).until(d ->
                "dark".equalsIgnoreCase(
                        String.valueOf(((JavascriptExecutor) d)
                                .executeScript(THEME_JS)))
        );

        logInfo("Dark theme applied");
        return this;
    }

    public SettingsPage changeToLightThemeAndVerify() {
        logInfo("Switch colour scheme to Light");
        scrollToCenter(driver, lightMode);
        lightMode.click();

        new WebDriverWait(driver, SHORT_WAIT).until(d ->
                "light".equalsIgnoreCase(
                        String.valueOf(((JavascriptExecutor) d)
                                .executeScript(THEME_JS)))
        );

        logInfo("Light theme applied");
        return this;
    }
}
