package com.tests;

import com.restObjects.Game;
import com.restObjects.Player;
import com.restObjects.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static com.tests.TestSuite.generateRandomString;
import static junit.framework.TestCase.assertTrue;

public class TestGames {
    private static String gameId, gamePassword;
    private static int startingLife;
    private static Random rand = new Random();

    @BeforeClass
    public static void setup() {
        gameId = generateRandomString(rand.nextInt(10)+5);
        gamePassword = generateRandomString(rand.nextInt(10)+5);
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
        System.out.println("Starting game: ");
        System.out.println("gameId = "+gameId);
        System.out.println("gamePassword = "+gamePassword);
        System.out.println("startingLife = "+startingLife);
        Response response = Game.startGame(gameId);
        System.out.println(response.getStringResponse());
        assertTrue("Validating the response code of startGame", response.getStatusCode() == 200);
        response = Game.hasGameStarted(gameId);
        System.out.println("Checking if the game has started: ");
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code of hasGameStarted inside of startGame", response.getStatusCode() == 200);
        boolean result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating that the game has started", result);
    }
    @Test
    public void testGetAllCommanders() {
        String email = generateRandomString(rand.nextInt(20)+ 7) + "@gmail.com";
        String password = generateRandomString(rand.nextInt(10)+6);
        String name = generateRandomString(rand.nextInt(10) + 8);
        String commander = generateRandomString(rand.nextInt(10) + 8);
        String commander2 = generateRandomString(rand.nextInt(10)+8);
        System.out.println("Creating another player:");
        System.out.println("email = " + email);
        System.out.println("password = " + password);
        System.out.println("name = " + name);
        System.out.println("commander1 = "+commander);
        System.out.println("commander2 = "+commander2);
        Response response = Player.createPlayer(email, name, password);
        System.out.println(response.getStringResponse());
        assertTrue("Validating the response code of createPlayer during testGetAllCommanders", response.getStatusCode() == 200);
        System.out.println("Adding the new player to the game");
        response = Player.joinGame(email, password, gameId, gamePassword, new String[] {commander, commander2}); //TODO: Also check with partner commanders
        System.out.println(response.getStringResponse()+"\n");
        assertTrue("Validating the response code of joinGame during testGetAllCommanders", response.getStatusCode() == 200);
        response = Game.allCommanders(gameId);
        System.out.println("\n"+response.getStringResponse()+"\n");
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
        System.out.println("Removing player from the game");
        response = Player.leaveGame(email, gameId);
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code for leaveGame during testGetAllPlayers", response.getStatusCode() == 200);
        System.out.println("Deleting the player");
        response = Player.deletePlayer(email, password);
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code for deletePlayer during testGetAllPlayers", response.getStatusCode() == 200);
    }
    @Test
    public void testSearchForGame() {
        String currentId = gameId;
        while(currentId.length() > 0) {
            Response response = Game.search(currentId);
            System.out.println("Search for: "+currentId);
            System.out.println(response.getStringResponse()+"\n");
            assertTrue("Validating response code for gameSearch('"+currentId+"')", response.getStatusCode() == 200);
            List<Game> result = response.mapJSONToObject(List.class);
            boolean found = false;
            for(Game game : result) {
                if(game.getGameId().equals(currentId)) {
                    found = true;
                    break;
                }
            }
            assertTrue("Validating the gameId is in the result", found);
            currentId = currentId.substring(0, currentId.length() - 1);
        }
    }
    @Test
    public void testUpdateGame() {
        String oldId = gameId;
        gameId = generateRandomString(rand.nextInt(10)+5);
        gamePassword = generateRandomString(rand.nextInt(10)+5);
        startingLife = rand.nextInt(20) + 20;
        System.out.println("oldId: "+oldId);
        System.out.println("gameId: "+gameId);
        System.out.println("gamePassword: "+gamePassword);
        System.out.println("startingLife: "+startingLife);
        Response response = Game.updateGame(oldId, gameId, gamePassword, startingLife);
        System.out.println(response.getStringResponse());
        assertTrue("Validating response code of updateGame", response.getStatusCode() == 200);
        Game game = response.mapJSONToObject(Game.class);
        assertTrue("Validating the gameId of the resulting game", game.getGameId().equals(gameId));
        assertTrue("Validating the starting life of the resulting game", game.getStartingLife() == startingLife);
    }
    @Test
    public void testVerifyGame() {
        System.out.println("Testing correct credentials");
        System.out.println("gameId = "+ gameId);
        System.out.println("gamePassword = "+gamePassword);
        Response response = Game.verify(gameId, gamePassword);
        System.out.println(response.getStringResponse()+"\n");
        assertTrue("Validating the response code of verifyGame", response.getStatusCode() == 200);
        boolean result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating the result of verifyGame", result);

        //Wrong password
        System.out.println("Testing incorrect password");
        String value = generateRandomString(rand.nextInt(20));
        System.out.println("gameId = "+gameId);
        System.out.println("gamePassword = "+value);
        response = Game.verify(gameId, value);
        System.out.println(response.getStringResponse()+"\n");
        assertTrue("Validating the response code of verifyGame", response.getStatusCode() == 200);
        result = response.mapJSONToObject(boolean.class);
        assertTrue("Validating the result of verifyGame", !result);

        //Wrong gameId
        System.out.println("Testing incorrect password");
        value = generateRandomString(rand.nextInt(20));
        System.out.println("gameId = "+value);
        System.out.println("gamePassword = "+gamePassword);
        response = Game.verify(generateRandomString(rand.nextInt(20)), gamePassword);
        System.out.println(response.getStringResponse());
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
