package com.tests;

import com.restObjects.Game;
import com.restObjects.Player;
import com.restObjects.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class TestGames {
    private static String gameId, gamePassword;
    private static int startingLife;

    @BeforeClass
    public static void setup() {
        gameId = TestPlayers.generateRandomString(new Random().nextInt(10)+5);
        gamePassword = TestPlayers.generateRandomString(new Random().nextInt(10)+5);
        startingLife = new Random().nextInt(20) + 20;
        Response response = Game.createGame(gameId, gamePassword, startingLife);
        assertTrue("Validating response code of createGame during setup", response.getStatusCode() == 200);
        response = Game.hasGameStarted(gameId);
        assertTrue("Validating response code of hasGameStarted inside of setup", response.getStatusCode() == 200);
        boolean result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating that the game has not started", !result);
    }
    @Test
    public void testStartGame() {
        Response response = Game.startGame(gameId);
        assertTrue("Validating the response code of startGame", response.getStatusCode() == 200);
        response = Game.hasGameStarted(gameId);
        assertTrue("Validating response code of hasGameStarted inside of startGame", response.getStatusCode() == 200);
        boolean result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating that the game has started", result);
    }
    @Test
    public void testGetAllCommanders() {
        String email = TestPlayers.generateRandomString(new Random().nextInt(20)+ 7) + "@gmail.com";
        String password = TestPlayers.generateRandomString(new Random().nextInt(10)+6);
        String name = TestPlayers.generateRandomString(new Random().nextInt(10) + 8);
        String commander = TestPlayers.generateRandomString(new Random().nextInt(10) + 8);
        Response response = Player.createPlayer(email, name, password);
        assertTrue("Validating the response code of createPlayer during testGetAllCommanders", response.getStatusCode() == 200);
        response = Player.joinGame(email, password, gameId, gamePassword, new String[] {commander}); //TODO: Also check with partner commanders
        assertTrue("Validating the response code of joinGame during testGetAllCommanders", response.getStatusCode() == 200);
        response = Game.allCommanders(gameId);
        assertTrue("Validating the response code of getAllCommanders", response.getStatusCode() == 200);
        String[][] result = response.mapJSONToObject(String[][].class);
        assertTrue("Validating that the result contains at least one entry", result != null && result.length > 0 && result[0].length == 2);
        boolean found = false;
        for(String[] arr : result) {
            assertTrue("Verifying the size of each result", arr.length == 2);
            if(arr[0].equals(commander) && arr[1].equals(email)) {
                found = true;
            }
        }
        assertTrue("Validating that the commander is in the result", found);
        response = Player.leaveGame(email, gameId);
        assertTrue("Validating response code for leaveGame during testGetAllPlayers", response.getStatusCode() == 200);
        response = Player.deletePlayer(email, password);
        assertTrue("Validating response code for deletePlayer during testGetAllPlayers", response.getStatusCode() == 200);
    }
    @Test
    public void testSearchForGame() {
        String currentId = gameId;
        while(currentId.length() > 0) {
            Response response = Game.search(currentId);
            assertTrue("Validating response code for gameSearch('"+currentId+"')", response.getStatusCode() == 200);
            List<String> result = response.mapJSONToObject(List.class);
            assertTrue("Validating the gameId is in the result", result.contains(gameId));
            currentId = currentId.substring(0, currentId.length() - 1);
        }
    }
    @Test
    public void testUpdateGame() {
        String oldId = gameId;
        gameId = TestPlayers.generateRandomString(new Random().nextInt(10)+5);
        gamePassword = TestPlayers.generateRandomString(new Random().nextInt(10)+5);
        startingLife = new Random().nextInt(20) + 20;
        Response response = Game.updateGame(oldId, gameId, gamePassword, startingLife);
        assertTrue("Validating response code of updateGame", response.getStatusCode() == 200);
        Game game = response.mapJSONToObject(Game.class);
        assertTrue("Validating the gameId of the resulting game", game.getGameId().equals(gameId));
        assertTrue("Validating the starting life of the resulting game", game.getStartingLife() == startingLife);
    }
    @Test
    public void testVerifyGame() {
        Response response = Game.verify(gameId, gamePassword);
        assertTrue("Validating the response code of verifyGame", response.getStatusCode() == 200);
        boolean result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating the result of verifyGame", result);

        //Wrong password
        response = Game.verify(gameId, TestPlayers.generateRandomString(new Random().nextInt(20)));
        assertTrue("Validating the response code of verifyGame", response.getStatusCode() == 200);
        result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating the result of verifyGame", !result);

        //Wrong username
        response = Game.verify(TestPlayers.generateRandomString(new Random().nextInt(20)), gamePassword);
        assertTrue("Validating the response code of verifyGame", response.getStatusCode() == 200);
        result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating the result of verifyGame", !result);
    }
    @AfterClass
    public static void tearDown() {
        Response response = Game.deleteGame(gameId);
        assertTrue("Validating response code of deleteGame during tearDown", response.getStatusCode() == 200);
    }
}
