package automationframeworkexample.clients.ui.utils.wrappers;

public class WaiterWrapper {

    public static void implicitWait(Integer secondsToWait){
        try {
            Thread.sleep(secondsToWait*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
