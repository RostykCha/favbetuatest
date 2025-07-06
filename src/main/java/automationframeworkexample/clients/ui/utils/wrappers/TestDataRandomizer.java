package automationframeworkexample.clients.ui.utils.wrappers;

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class TestDataRandomizer {

    public static Integer getRandomIntData(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than zero");
        }
        Random random = new Random();
        int min = (int) Math.pow(10, length - 1); // Minimum value for the specified length
        int max = (int) Math.pow(10, length) - 1; // Maximum value for the specified length
        return random.nextInt(max - min + 1) + min;
    }

    public static String getRandomStringData(int length) {
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

}
