package automationframeworkexample.clients.ui.utils.wrappers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestCaseDocumentationId {
    /**
     * Provides possibility to link all our Tests to Tests Cases
     */
    String testCaseId();
}
