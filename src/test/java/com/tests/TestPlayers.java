package com.tests;

import com.restObjects.Game;
import com.restObjects.Player;
import com.restObjects.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static com.tests.TestSuite.generateRandomString;
import static junit.framework.TestCase.assertTrue;

public class TestPlayers {
    private static String email, password, name, gameId, gamePassword, gameHost;
    private static int maxSize;
    private static Random rand = new Random();

    @BeforeClass
    public static void setUp() {
        email = generateRandomString(rand.nextInt(20)+ 7) + "@gmail.com";
        password = generateRandomString(rand.nextInt(10)+6);
        name = generateRandomString(rand.nextInt(10) + 8);
        gameId = generateRandomString(rand.nextInt(13) + 10);
        gamePassword = generateRandomString(rand.nextInt(15) + 10);
        gameHost = generateRandomString(rand.nextInt(10)+5);
        maxSize = new Random().nextInt(20) + 20;
        Response response = Player.createPlayer(email, name, password);
        assertTrue("Validating response code for createPlayer during setup", response.getStatusCode() == 200);
        response = Game.createGame(gameId, gamePassword, gameHost, rand.nextInt(20)+20, maxSize);
        assertTrue("Validating response code for createGame during player setup", response.getStatusCode() == 200);
        response = Player.joinGame(email, password, gameId, gamePassword, new String[] {"Narset", "Muldrotha"});
        assertTrue("Validating response code for joinGame during setup", response.getStatusCode() == 200);
    }
    @Test
    public void testCreatePlayer() {
        Response response = Player.createPlayer(email, name, password);
        System.out.println("email = " + email);
        System.out.println("password = " + password);
        System.out.println("name = " + name);
        System.out.println("Put into game:");
        System.out.println("gameId: "+gameId);
        System.out.println("gamePassword: "+gamePassword);
        assertTrue("Validating duplicate players fails", response.getStatusCode() == 400);

    }
    @Test
    public void testUpdatePlayer() {
        String prevEmail = email;
        email = generateRandomString(rand.nextInt(20)+ 7) + "@gmail.com";
        password = generateRandomString(rand.nextInt(10)+6);
        name = generateRandomString(rand.nextInt(10) + 8);
        System.out.println("previous email: "+email);
        System.out.println("email = " + email);
        System.out.println("password = " + password);
        System.out.println("name = " + name);
        Response response = Player.updatePlayer(prevEmail, email, name, password);
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code for updatePlayer", response.getStatusCode() == 200);

    }
    @Test
    public void testGetPlayer() {
        Response response = Player.byEmail(email, gameId);
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code for getPlayer", response.getStatusCode() == 200);
        Player player = response.mapJSONToObject(Player.class);
        assertTrue("Validating resulting player is not null", player != null);
        assertTrue("Validating name of resulting player", player.getName().equals(name));
        assertTrue("Validating email of resulting player", player.getEmail().equals(email));

    }
    @Test
    public void testGetGamesPlayerIsIn() {
        Response response = Player.allGames(email);
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code for getAllGamesPlayerIsIn", response.getStatusCode() == 200);
        String[] games = response.mapJSONToObject(String[].class);
        assertTrue("Validating that there are games returned", games != null && games.length > 0);
        boolean found = false;
        for(String game : games) {
            if(game.equals(gameId))
                found = true;
        }
        assertTrue("Validating the correct game is in the response", found);
    }
    @Test
    public void testGetAllPlayers() {
        Response response = Player.all(gameId);
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code for getAllPlayers", response.getStatusCode() == 200);
        Player[] players = response.mapJSONToObject(Player[].class);
        assertTrue("Validating that there are players returned", players != null && players.length > 0);
        boolean found = false;
        for(Player player : players) {
            if(player.getEmail().equals(email)) {
                found = true;
            }
        }
        assertTrue("Validating the created player is in the response", found);

    }
    @Test
    public void testLogin() {
        System.out.println("Testing the correct credentials");
        Response response = Player.login(email, password);
        System.out.println("email = " + email);
        System.out.println("password = " + password);
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code for login", response.getStatusCode() == 200);
        boolean result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating login", result);

        //Wrong password
        System.out.println("\nTesting incorrect password");
        String value = generateRandomString(rand.nextInt(20));
        response = Player.login(email, value);
        System.out.println("email = "+email);
        System.out.println("password = "+value);
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code for login", response.getStatusCode() == 200);
        result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating login", !result);

        //Wrong username
        System.out.println("\nTesting incorrect username");
        value = generateRandomString(rand.nextInt(20));
        response = Player.login(value, gamePassword);
        System.out.println("email = "+value);
        System.out.println("password = "+gamePassword);
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code for login", response.getStatusCode() == 200);
        result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating login", !result);
    }
    @AfterClass
    public static void tearDown() {
        Response response = Player.leaveGame(email, gameId);
        assertTrue("Validating response code for leaveGame during teardown", response.getStatusCode() == 200);
        response = Player.deletePlayer(email, password);
        assertTrue("Validating response code for deletePlayer during tearDown", response.getStatusCode() == 200);
        response = Game.deleteGame(gameId);
        assertTrue("Validating the response code for deleteGame during tearDown", response.getStatusCode() == 200);

    }
}
