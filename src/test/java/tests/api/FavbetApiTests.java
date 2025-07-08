package tests.api;

import automationframeworkexample.clients.api.FavbetApiClient;
import automationframeworkexample.clients.api.dto.BonusCounts;
import automationframeworkexample.clients.ui.dto.UserDto;
import automationframeworkexample.clients.ui.pages.HomePage;
import automationframeworkexample.clients.ui.utils.retry.AutomationRetry;
import automationframeworkexample.clients.ui.utils.wrappers.TestCaseDocumentationId;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tests.TestBase;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;
import static automationframeworkexample.clients.ui.utils.wrappers.TestDataRandomizer.getRandomProperEmailFormat;
import static automationframeworkexample.clients.ui.utils.wrappers.TestDataRandomizer.getRandomProperPass;

public class FavbetApiTests extends TestBase {

    private UserDto userDto;


    @BeforeClass
    public void doBeforePreparations() {
        favbetApiClient = ctx.getBean(FavbetApiClient.class);

        logInfo("[Suite setup] Generating random credentials");
        userDto = new UserDto(getRandomProperEmailFormat(), getRandomProperPass());

        logInfo("[Suite setup] Registering user through the UI");
        ctx.getBean(HomePage.class)
                .openHomePage()
                .navigateToRegisterPage()
                .registerUser(userDto)
                .waitUntilUserIsLogged();
        logInfo("[Suite setup] User registered and logged in: " + userDto.getEmail());

        favbetApiClient.bootstrapFromSelenium(dm.getDriver());
    }

    @Test(retryAnalyzer = AutomationRetry.class)
    @TestCaseDocumentationId(testCaseId = "005")
    public void verifyListOfBonusesLogic_T005() {
        logInfo("[T005] Start: verifyListOfBonusesLogic_T005");

        //Will fail, even we copy cookies from UI - Cloudflare’s bot-mitigation cookie is missing
        BonusCounts bonusCounts = favbetApiClient.getListOfBonuses();
        favbetApiClient.assertBonusCountsAreAccurate(bonusCounts);

        logInfo("[T005] Passed: bonus counters are correct");
    }


}
