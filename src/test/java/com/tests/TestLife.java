package com.tests;

import com.Application;
import com.objects.CommanderDamage;
import com.restObjects.Game;
import com.restObjects.Player;
import com.restObjects.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class TestLife {
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Player[] players;
    private static String[] passwords;
    private static String[][] commanders;
    private static Game game;

    private static String generateRandomString(int length) {
        Random rand = new Random();
        StringBuilder builder = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            builder.append(ALPHA.charAt(rand.nextInt(ALPHA.length())));
        return builder.toString();
    }
    @BeforeClass
    public static void setup() {
        players = new Player[5];
        commanders = new String[5][];
        passwords = new String[5];
        //Game Creation
        game = new Game();
        game.setGameId(TestPlayers.generateRandomString(new Random().nextInt(10)+5));
        game.setGamePassword(TestPlayers.generateRandomString(new Random().nextInt(10)+5));
        game.setStartingLife(new Random().nextInt(20) + 20);
        Response response = Game.createGame(game);
        assertTrue("Verifying response code for createGame in setup", response.getStatusCode() == 200);
        //Player Creation
        for(int i = 0; i < players.length; i++) {
            players[i] = new Player();
            players[i].setEmail(TestPlayers.generateRandomString(new Random().nextInt(20)+ 7) + "@gmail.com");
            players[i].setPassword(TestPlayers.generateRandomString(new Random().nextInt(10)+6));
            passwords[i] = players[i].getPassword();
            players[i].setName(TestPlayers.generateRandomString(new Random().nextInt(10) + 8));
            response = Player.createPlayer(players[i]);
            assertTrue("Verifying response code for createPlayer in setup", response.getStatusCode() == 200);
            String[] playersCommanders;
            if(new Random().nextBoolean()) {
                playersCommanders = new String[2];
                playersCommanders[0] = generateRandomString(new Random().nextInt(10) + 4);
                playersCommanders[1] = generateRandomString(new Random().nextInt(10) + 4);
            }
            else {
                playersCommanders = new String[1];
                playersCommanders[0] = generateRandomString(new Random().nextInt(10) + 4);
            }
            commanders[i] = playersCommanders;
            response = Player.joinGame(players[i].getEmail(),players[i].getPassword(),game.getGameId(), game.getGamePassword(), playersCommanders);
            assertTrue("Verifying response code for joinGame in setup", response.getStatusCode() == 200);
            response = Player.all(game.getGameId());
            assertTrue("Verifying response code for getAllPlayers in setup", response.getStatusCode() == 200);
        }
        players = response.mapJSONToObject(Player[].class);
        //Damage
        for(int i = 0; i < 20; i++) {
            int amount = new Random().nextInt(10);
            int player = new Random().nextInt(players.length);
            int life = players[player].getLife() - amount;
            int enemyPlayer = new Random().nextInt(players.length);
            while(enemyPlayer == player)
                enemyPlayer = new Random().nextInt(players.length);
            CommanderDamage damage = new CommanderDamage();
            damage.setPlayer(players[player].getEmail());
            damage.setEnemyPlayer(players[enemyPlayer].getEmail());
            damage.setCommander(commanders[enemyPlayer][0]);
            if(players[player].getCommanderDamage().containsKey(players[enemyPlayer]) && players[player].getCommanderDamage().get(players[enemyPlayer]).containsKey(commanders[i][0]))
                amount += players[player].getCommanderDamage().get(players[enemyPlayer].getEmail()).get(commanders[enemyPlayer][0]);
            damage.setDamage(amount);
            response = Player.putCommanderDamage(game.getGameId(), game.getGamePassword(), passwords[player], damage);
            System.out.println(Application.getJson(damage, true));
            assertTrue("Validating response code for setCommanderDamage", response.getStatusCode() == 200);
            if(!players[player].getCommanderDamage().containsKey(players[enemyPlayer].getEmail())) {
                HashMap<String, Integer> map = new HashMap<>();
                players[player].getCommanderDamage().put(players[enemyPlayer].getEmail(), map);
            }
            players[player].getCommanderDamage().get(players[enemyPlayer].getEmail()).put(commanders[enemyPlayer][0], amount);
            //Setting life in relation to commander damage
            response = Player.putLife(game.getGameId(), game.getGamePassword(), players[player].getEmail(), passwords[player], life);
            assertTrue("Validating response code for setLife during setup", response.getStatusCode() == 200);
            players[player].setLife(life);

            //Poison
            int poison = new Random().nextInt(11);
            response = Player.putPoison(game.getGameId(), game.getGamePassword(), players[player].getEmail(), passwords[player], poison);
            System.out.println(response.getStringResponse());
            assertTrue("Validating response code for setPoison during setup", response.getStatusCode() == 200);
            players[player].setPoison(poison);

            //Experience
            int experience = new Random().nextInt(15);
            response = Player.putExperience(game.getGameId(), game.getGamePassword(), players[player].getEmail(), passwords[player], poison);
            assertTrue("Validating response code for setExperience during setup", response.getStatusCode() == 200);
            players[player].setExperience(poison);
        }
    }
    @Test
    public void testGetCommanderDamage() {
        for(Player player : players) {
            for(String enemyPlayer : player.getCommanderDamage().keySet()) {
                for(String commander : player.getCommanderDamage().get(enemyPlayer).keySet()) {
                    Response response = Player.commanderDamage(commander, game.getGameId(), player.getEmail(), enemyPlayer);
                    System.out.println(response.getStringResponse());
                    assertTrue("Validating response code for getCommanderDamage", response.getStatusCode() == 200);
                    int amount = response.mapJSONToObject(int.class);
                    assertTrue("Validating the amount of damage", amount == player.getCommanderDamage().get(enemyPlayer).get(commander));
                }
            }
        }
    }
    @Test
    public void testGetLifeStats() {
        for(Player myPlayer : players) {
            Response response = Player.player(game.getGameId(), myPlayer.getEmail());
            assertTrue("Validating the response code for getPlayer", response.getStatusCode() == 200);
            System.out.println(response.getStringResponse());
            Player player = response.mapJSONToObject(Player.class);
            assertTrue("Validating the life total of the resulting player", player.getLife() == myPlayer.getLife());
            assertTrue("Validating the poison of the resulting player", player.getPoison() == myPlayer.getPoison());
            assertTrue("Validating the experience of the resulting player", player.getExperience() == myPlayer.getExperience());
        }
    }
    @Test
    public void testGetPlayer() {
        for(Player player : players) {
            Response response = Player.byEmail(player.getEmail(), game.getGameId());
            assertTrue("Validating the response code of getPlayer", response.getStatusCode() == 200);
            Player dbPlayer = response.mapJSONToObject(Player.class);
            assertTrue("Validating the name of the player", player.getName().equals(dbPlayer.getName()));
            assertTrue("Validating the email of the player", player.getEmail().equals(dbPlayer.getEmail()));
            assertTrue("Validating the life of the player", player.getLife() == dbPlayer.getLife());
            assertTrue("Validating the poison of the player", player.getPoison() == dbPlayer.getPoison());
            assertTrue("Validating the experience of the player", player.getExperience() == dbPlayer.getExperience());
        }
    }
    @AfterClass
    public static void tearDown() {
        Response response = Game.deleteGame(game.getGameId());
        assertTrue("Verifying response code for deleteGame in tearDown", response.getStatusCode() == 200);
        int i = 0;
        for(Player player : players) {
            response = Player.deletePlayer(player.getEmail(), passwords[i]);
            System.out.println(response.getStringResponse());
            assertTrue("Verifying response code for deletePlayer in tearDown", response.getStatusCode() == 200);
            i++;
        }
    }
}
