package com.tests;

import com.restObjects.Player;
import com.restObjects.Response;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class TestPlayers {
    private String email, password, name;
    public String generateRandomString(int length) {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
    @Before
    public void setUp() {
        email = generateRandomString(new Random().nextInt(10)+ 4) + "@gmail.com";
        password = generateRandomString(new Random().nextInt(10)+6);
        name = generateRandomString(new Random().nextInt(10) + 8);
        Response response = Player.createPlayer(email, name, password);
        assertTrue("Validating response code for createPlayer", response.getStatusCode() == 200);
    }
    @Test
    public void testCreatePlayer() {
        assertTrue(true);
    }
}
