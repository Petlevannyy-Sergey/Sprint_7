package utils;

import org.apache.commons.text.RandomStringGenerator;

public class RandomUtils {
    public static String generateLogin() {
        return new RandomStringGenerator.Builder().withinRange('a', 'z').get().generate(10);
    }

    public static String generatePassword() {
        return new RandomStringGenerator
                .Builder()
                .selectFrom("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray())
                .get()
                .generate(8);
    }

    public static String generateFirstName() {
        String generated = new RandomStringGenerator.Builder().withinRange('a', 'z').get().generate(10);
        return generated.substring(0, 1).toUpperCase() + generated.substring(1);
    }
}
