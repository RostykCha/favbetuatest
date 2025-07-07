package automationframeworkexample.clients.ui.utils.wrappers;

import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

public class TestDataRandomizer {
    private static final RandomGenerator RNG = RandomGenerator.getDefault();
    private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] ALPHANUM = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final char[] SPECIAL_CHARS = "!@#$%^&*_-+=?".toCharArray();

    private TestDataRandomizer() {
    }

    public static int getRandomIntData(int length) {
        if (length <= 0) throw new IllegalArgumentException("Length must be greater than zero");
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;
        return RNG.nextInt(min, max + 1);
    }

    public static String getRandomStringData(int length) {
        return randomChars(length, LETTERS);
    }

    public static String getRandomProperEmailFormat() {
        String local = randomChars(1, LETTERS) + randomChars(7, ALPHANUM);
        String domain = randomChars(5, LETTERS);
        List<String> tlds = List.of("com", "net", "org", "io", "co");
        String tld = tlds.get(RNG.nextInt(tlds.size()));
        return "%s@%s.%s".formatted(local, domain, tld);
    }

    public static String getRandomProperPass() {
        String upper = randomChars(2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());
        String lower = randomChars(2, LETTERS);
        String digit = randomChars(2, "0123456789".toCharArray());
        String fill = randomChars(4, ALPHANUM);
        String raw = upper + lower + digit  + fill;
        List<Character> chars = raw.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        for (int i = chars.size() - 1; i > 0; i--) {
            int j = RNG.nextInt(i + 1);
            Collections.swap(chars, i, j);
        }
        StringBuilder pwd = new StringBuilder();
        chars.forEach(pwd::append);
        return pwd.toString();
    }

    private static String randomChars(int length, char[] pool) {
        return RNG.ints(length, 0, pool.length)
                .mapToObj(i -> String.valueOf(pool[i]))
                .collect(Collectors.joining());
    }
}
