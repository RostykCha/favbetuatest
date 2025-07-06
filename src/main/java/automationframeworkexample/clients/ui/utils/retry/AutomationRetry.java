package automationframeworkexample.clients.ui.utils.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

public class AutomationRetry implements IRetryAnalyzer {
    /**
     * Retry should be avoided, but sometimes due to environmental issues,
     * it may help us distinguish product issues from environmental.
     */

    private static final Integer MAX_RETRY_COUNT = 0;

    AtomicInteger count = new AtomicInteger(MAX_RETRY_COUNT);

    public boolean isRetryAvailable() {
        return count.intValue() > 0;
    }

    @Override
    public boolean retry(ITestResult result) {
        boolean retry = false;
        if (isRetryAvailable()) {
            retry = true;
            count.decrementAndGet();
        }
        return retry;
    }
}
