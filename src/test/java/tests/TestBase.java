package tests;

import automationframeworkexample.FrameworkSpringConfiguration;
import automationframeworkexample.clients.api.FavbetApiClient;
import automationframeworkexample.clients.ui.DriverManager;
import automationframeworkexample.clients.ui.dto.UserDto;
import automationframeworkexample.clients.ui.utils.retry.AutomationListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootTest(classes = FrameworkSpringConfiguration.class)
@Listeners(AutomationListenerAdapter.class)
public class TestBase extends AbstractTestNGSpringContextTests {

    public static final List<UserDto> TEST_USERS = new CopyOnWriteArrayList<>();
    @Autowired
    protected ApplicationContext ctx;
    @Autowired
    protected DriverManager dm;
    @Autowired
    protected FavbetApiClient favbetApiClient;

    @DataProvider(name = "userLoginDataProvider", parallel = true)
    public Object[][] userLoginDataProvider() {
        return TEST_USERS.stream()
                .map(u -> new Object[]{u})
                .toArray(Object[][]::new);
    }


}
