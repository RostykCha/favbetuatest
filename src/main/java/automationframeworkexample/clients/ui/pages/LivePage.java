package automationframeworkexample.clients.ui.pages;

import automationframeworkexample.clients.ui.DriverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
@Scope("prototype")
public class LivePage extends GeneralTablePage {

    @Autowired
    public LivePage(DriverManager driverManager) {
        super(driverManager);
        logInfo("Opened Live page");
    }
}