package utils;

import com.github.javafaker.Faker;
import org.apache.commons.text.RandomStringGenerator;


public class RandomUtils {
    public static String generateLogin() {
        return new Faker().name().username();
    }

    public static String generatePassword() {
        return new Faker().internet().password();
    }

    public static String generateFirstName() {
        return new Faker().name().firstName();
    }
}
