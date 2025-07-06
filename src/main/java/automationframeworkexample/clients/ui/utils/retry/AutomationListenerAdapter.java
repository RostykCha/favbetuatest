package automationframeworkexample.clients.ui.utils.retry;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

public class AutomationListenerAdapter extends TestListenerAdapter {


    @Override
    public void onTestFailure(ITestResult result) {
        if (result.getMethod().getRetryAnalyzer() != null) {
            AutomationRetry automationRetryAnalyser = (AutomationRetry) result.getMethod().getRetryAnalyzer();

            if (automationRetryAnalyser.isRetryAvailable()) {
                result.setStatus(ITestResult.SKIP);
            } else {
                result.setStatus(ITestResult.FAILURE);
            }
            Reporter.setCurrentTestResult(result);
        }

    }

}
