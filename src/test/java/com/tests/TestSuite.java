package com.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Random;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestPlayers.class,
        TestGames.class,
        TestLife.class
})
public class TestSuite {
    private static final String ALPHA = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ`!@#$%^&*()-_=+[]{}|\\;:'\"/?.>,<";
    private static final String ALPHA_NO_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random rand = new Random();

    public static String generateRandomString(int length) {
        StringBuilder builder = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            builder.append(ALPHA.charAt(rand.nextInt(ALPHA.length())));
        return builder.toString();
    }
    public static String generateRandomString(int length, boolean noSymbols) {
        if(noSymbols) {
            StringBuilder builder = new StringBuilder(length);
            for (int i = 0; i < length; i++)
                builder.append(ALPHA_NO_SYMBOLS.charAt(rand.nextInt(ALPHA_NO_SYMBOLS.length())));
            return builder.toString();
        }
        return generateRandomString(length);
    }
}
