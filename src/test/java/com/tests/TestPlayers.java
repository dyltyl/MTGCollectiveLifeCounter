package com.tests;

import com.Application;
import com.restObjects.Player;
import com.restObjects.Response;
import com.restObjects.RestObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class TestPlayers {
    private static String email, password, name;
    private static final String ALPHA = "1234567890qwertyuiopasdfghjklzxcvbnm`!@#$%^&*()-_=+[]{}|\\;:'\"/?.>,<";
    public static String generateRandomString(int length) {
        Random rand = new Random();
        StringBuilder builder = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            builder.append(ALPHA.charAt(rand.nextInt(ALPHA.length())));
        return builder.toString();
    }
    @BeforeClass
    public static void setUp() {
        email = generateRandomString(new Random().nextInt(20)+ 7) + "@gmail.com";
        password = generateRandomString(new Random().nextInt(10)+6);
        name = generateRandomString(new Random().nextInt(10) + 8);
        System.out.println("email = " + email);
        System.out.println("password = " + password);
        System.out.println("name = " + name);
        Response response = Player.createPlayer(email, name, password);
        assertTrue("Validating response code for createPlayer during setup", response.getStatusCode() == 200);
    }
    @Test
    public void testCreatePlayer() {
        Response response = Player.createPlayer(email, name, password);
        assertTrue("Validating duplicate players fails", response.getStatusCode() == 400);
    }
    @Test
    public void testUpdatePlayer() {
        String prevEmail = email;
        email = generateRandomString(new Random().nextInt(20)+ 7) + "@gmail.com";
        password = generateRandomString(new Random().nextInt(10)+6);
        name = generateRandomString(new Random().nextInt(10) + 8);
        System.out.println("email = " + email);
        System.out.println("password = " + password);
        System.out.println("name = " + name);
        Response response = Player.updatePlayer(prevEmail, email, name, password);
        assertTrue("Validating response code for updatePlayer", response.getStatusCode() == 200);
    }
}
