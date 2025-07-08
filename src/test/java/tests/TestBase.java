package tests;

import automationframeworkexample.FrameworkSpringConfiguration;
import automationframeworkexample.clients.api.FavbetApiClient;
import automationframeworkexample.clients.ui.DriverManager;
import automationframeworkexample.clients.ui.pages.HomePage;
import automationframeworkexample.clients.ui.utils.retry.AutomationListenerAdapter;
import automationframeworkexample.clients.ui.BasePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@SpringBootTest(classes = FrameworkSpringConfiguration.class)
@Listeners(AutomationListenerAdapter.class)
public class TestBase extends AbstractTestNGSpringContextTests {

    @Autowired protected ApplicationContext ctx;
    @Autowired protected DriverManager dm;

    @Autowired protected FavbetApiClient favbetApiClient;

    @DataProvider(name = "userLoginDataProvider", parallel = true)
    public Object[][] userLoginDataProvider() {
        return new Object[][]{{"Hi!!"}};
    }


}
