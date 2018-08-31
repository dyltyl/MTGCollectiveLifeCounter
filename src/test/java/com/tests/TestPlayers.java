package com.tests;

import com.Application;
import com.restObjects.Player;
import com.restObjects.Response;
import com.restObjects.RestObject;
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
        /*email = generateRandomString(new Random().nextInt(10)+ 4) + "@gmail.com";
        password = generateRandomString(new Random().nextInt(10)+6);
        name = generateRandomString(new Random().nextInt(10) + 8);*/
        email = "help@gmail.com";
        name = "testing";
        password = "password";
        Response response = Player.createPlayer(email, name, password);
        System.out.println(response.getStatusCode());
        System.out.println(response.getRequestBody());
        System.out.println(response.getMethod());
        System.out.println(response.getResponse().getStatusLine().getReasonPhrase());
        assertTrue("Validating response code for createPlayer", response.getStatusCode() == 200);
    }
    @Test
    public void testCreatePlayer() {
        RestObject help = new RestObject();
        Response response = help.sendGetRequest(RestObject.BASE_URL+"/status");
        assertTrue(response.getStatusCode() == 200);
    }
}
