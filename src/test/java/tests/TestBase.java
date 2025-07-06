package tests;

import automationframeworkexample.FrameworkSpringConfiguration;
import automationframeworkexample.clients.ui.utils.retry.AutomationListenerAdapter;
import automationframeworkexample.clients.ui.BasePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@SpringBootTest(classes = FrameworkSpringConfiguration.class)
@Listeners(AutomationListenerAdapter.class)
public class TestBase extends AbstractTestNGSpringContextTests {
    public static final Integer BOOK_ID_LENGTH = 3;

    @Autowired
    public BasePage basePage;

    @DataProvider(name = "userLoginDataProvider", parallel = true)
    public Object[][] userLoginDataProvider() {
        return new Object[][]{{"Hi!!"}};
    }


}
