package com.controllers;

import com.Application;
import com.objects.CommanderDamage;
import com.objects.Game;
import com.objects.Player;
import com.objects.SmallPlayer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(origins = "*")
@RestController
public class MagicController {

    @RequestMapping(value= {"/status"}, method = GET)
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Server is online", new HttpHeaders(), HttpStatus.OK);
    }
    @RequestMapping(value={"/createGame"}, method = POST)
    public ResponseEntity<?> createGame(@RequestBody Game game) {
        System.out.println(Application.getJson(game, true));
        String query = "INSERT INTO games (id, password, starting_life) VALUES (?, ?, ?);";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, game.getGameId());
            statement.setString(2, game.getGamePassword());
            statement.setInt(3, game.getStartingLife());
            Application.queryNoResults(statement);
            connection.close();
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Success",new HttpHeaders(), HttpStatus.OK);
    }
    @RequestMapping(value={"/createPlayer"}, method = POST)
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        String query = "INSERT INTO players (email, password, name) VALUES(?, ?, ?) RETURNING email";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getEmail());
            statement.setString(2, player.getPassword());
            statement.setString(3, player.getName());
            String[][] result = Application.query(statement);
            connection.close();
            String email = result[0][0];
            return new ResponseEntity<>(email,new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/joinGame"}, method = POST)
    public ResponseEntity<?> joinGame(HttpServletRequest headers, @RequestBody String[] commanders) {
        if(!verifyGame(headers.getHeader("gameId"), headers.getHeader("gamePassword"))) {
            return new ResponseEntity<>("Incorrect game credentials", HttpStatus.BAD_REQUEST);
        }
        if(!verifyUser(headers.getHeader("email"), headers.getHeader("password"))) {
            return new ResponseEntity<>("Incorrect user credentials", HttpStatus.BAD_REQUEST);
        }
        int startingLife = getStartingLife(headers.getHeader("gameId"));
        if(startingLife < 1) {
            return new ResponseEntity<>("Starting life must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        for(String commander : commanders) {
            String query = "INSERT INTO commanders (game, player, commander) VALUES (?, ?, ?);";
            try {
                Connection connection = Application.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, headers.getHeader("gameId"));
                statement.setString(2, headers.getHeader("email"));
                statement.setString(3, commander);
                Application.queryNoResults(statement);
                connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String query = "INSERT INTO life (email, game, life) VALUES (?, ?, ?) RETURNING email;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("email"));
            statement.setString(2, headers.getHeader("gameId"));
            statement.setInt(3, startingLife);
            String[][] result = Application.query(statement);
            connection.close();
            if(result.length > 0) {
                String email = result[0][0];
                return new ResponseEntity<>(email, new HttpHeaders(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    public boolean verifyGame(String gameId, String gamePassword) {
        String query = "SELECT id FROM games WHERE id = ? AND password = ?;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gameId);
            statement.setString(2, gamePassword);
            String[][] result = Application.query(statement);
            connection.close();
            return result.length > 0;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getStartingLife(String gameId) {
        String query = "SELECT starting_life FROM games WHERE id = ?";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gameId);
            String[][] result = Application.query(statement);
            connection.close();
            if(result.length > 0) {
                return Integer.parseInt(result[0][0]);
            }
            return -1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public boolean verifyUser(String email, String password) {
        String query = "SELECT email FROM players WHERE email = ? AND password = ?;"; //TODO: encrypt password
       try {
           Connection connection = Application.getDataSource().getConnection();
           PreparedStatement statement = connection.prepareStatement(query);
           statement.setString(1, email);
           statement.setString(2, password);
           String[][] result = Application.query(statement);
           connection.close();
           if(result.length == 1) {
               return true;
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return false;
   }
    @RequestMapping(value = {"/setLife/{life}"}, method = PUT)
    public ResponseEntity<?> setLife(HttpServletRequest headers, @PathVariable int life) {
       if(!verifyGame(headers.getHeader("gameId"), headers.getHeader("gamePassword"))) {
           return new ResponseEntity<>("Incorrect game credentials", HttpStatus.BAD_REQUEST);
       }
       if(!verifyUser(headers.getHeader("email"), headers.getHeader("password"))) {
           return new ResponseEntity<>("Incorrect user credentials", HttpStatus.BAD_REQUEST);
       }
        String query = "UPDATE life SET life = ? WHERE email = ? AND game = ? RETURNING life";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, life);
            statement.setString(2, headers.getHeader("email"));
            statement.setString(3, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            connection.close();
            if(result.length > 0) {
                return new ResponseEntity<>(result[0][0], new HttpHeaders(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
   }
    @RequestMapping(value = {"/setPoison/{poison}"}, method = POST)
    public ResponseEntity<?> setPoison(HttpServletRequest headers, @PathVariable int poison) {
        if(!verifyGame(headers.getHeader("gameId"), headers.getHeader("gamePassword"))) {
            return new ResponseEntity<>("Incorrect game credentials", HttpStatus.BAD_REQUEST);
        }
        if(!verifyUser(headers.getHeader("email"), headers.getHeader("password"))) {
            return new ResponseEntity<>("Incorrect user credentials", HttpStatus.BAD_REQUEST);
        }
        String query = "UPDATE life SET poison = ? WHERE email = ? AND game = ? RETURNING poison";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, poison);
            statement.setString(2, headers.getHeader("email"));
            statement.setString(3, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            connection.close();
            if(result.length > 0) {
                return new ResponseEntity<>(result[0][0], new HttpHeaders(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/setExperience/{experience}"}, method = POST)
    public ResponseEntity<?> setExperience(HttpServletRequest headers, @PathVariable int experience) {
        if(!verifyGame(headers.getHeader("gameId"), headers.getHeader("gamePassword"))) {
            return new ResponseEntity<>("Incorrect game credentials", HttpStatus.BAD_REQUEST);
        }
        if(!verifyUser(headers.getHeader("email"), headers.getHeader("password"))) {
            return new ResponseEntity<>("Incorrect user credentials", HttpStatus.BAD_REQUEST);
        }
        String query = "UPDATE life SET experience = ? WHERE email = ? AND game = ? RETURNING experience";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, experience);
            statement.setString(2, headers.getHeader("email"));
            statement.setString(3, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            connection.close();
            if(result.length > 0) {
                return new ResponseEntity<>(result[0][0], new HttpHeaders(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/setCommanderDamage"}, method = POST)
    public ResponseEntity<?> setCommanderDamage(HttpServletRequest headers, @RequestBody CommanderDamage damage) {
        if(!verifyGame(headers.getHeader("gameId"), headers.getHeader("gamePassword"))) {
            return new ResponseEntity<>("Incorrect game credentials", HttpStatus.BAD_REQUEST);
        }
        if(!verifyUser(headers.getHeader("email"), headers.getHeader("password"))) {
            return new ResponseEntity<>("Incorrect user credentials", HttpStatus.BAD_REQUEST);
        }
        String query = "INSERT INTO commander_damage (player, enemy_player, game, commander, damage) Values (?, ?, ?, ?, ?) " +
                "ON CONFLICT ON CONSTRAINT commander_damage_pkey DO UPDATE SET damage = excluded.damage RETURNING damage;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, damage.getPlayer());
            statement.setString(2, damage.getEnemyPlayer());
            statement.setString(3, headers.getHeader("gameId"));
            statement.setString(4, damage.getCommander());
            statement.setInt(5, damage.getDamage());
            String[][] result = Application.query(statement);
            connection.close();
            if(result.length > 0) {
                return new ResponseEntity<>(result[0][0], new HttpHeaders(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        catch(SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/getAllCommanders"}, method = GET)
    public ResponseEntity<?> getAllCommanders(HttpServletRequest headers) {
        String query = "SELECT commander, player FROM commanders WHERE game = ?;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            connection.close();
            System.out.println(Application.getJson(result, true));
            return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
        }
        catch(SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/getAllPlayers"}, method = GET) //TODO: errors when there are none
    public ResponseEntity<?> getAllPlayers(HttpServletRequest headers) {
        String query = "SELECT players.email, life, poison, experience, name FROM life JOIN players ON players.email = life.email WHERE game = ?;";
        Player[] players;
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            connection.close();
            players = new Player[result.length];
            for(int i = 0; i < result.length; i++) {
                players[i] = new Player();
                players[i].setEmail(result[i][0]);
                players[i].setPassword("**********");
                players[i].setLife(Integer.parseInt(result[i][1]));
                players[i].setPoison(Integer.parseInt(result[i][2]));
                players[i].setExperience(Integer.parseInt(result[i][3]));
                players[i].setName(result[i][4]);
                HashMap<String, HashMap<String, Integer>> map = new HashMap<>();
                query = "SELECT enemy_player, commander, damage FROM commander_damage WHERE game = ? AND player = ?;";
                try {
                    connection = Application.getDataSource().getConnection();
                    statement = connection.prepareStatement(query);
                    statement.setString(1, headers.getHeader("gameId"));
                    statement.setString(2, players[i].getEmail());
                    String[][] result2 = Application.query(statement);
                    connection.close();
                    for(String[] arr : result2) {
                        if(!map.containsKey(arr[0])) {
                            HashMap<String, Integer> commanderDamage = new HashMap<>();
                            map.put(arr[0], commanderDamage);
                        }
                        map.get(arr[0]).put(arr[1], Integer.parseInt(arr[2]));
                    }
                }
                catch(SQLException e) {
                    e.printStackTrace();
                }

                players[i].setCommanderDamage(map);

            }
            return new ResponseEntity<>(players, new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/player"}, method = GET)
    public ResponseEntity<?> getPlayer(HttpServletRequest headers) {
        String query = "SELECT players.email, life, poison, experience, name FROM life JOIN players ON players.email = life.email WHERE game = ? AND life.email = ?;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("gameId"));
            statement.setString(2, headers.getHeader("email"));
            String[][] result = Application.query(statement);
            connection.close();
            Player player = new Player();
            player.setEmail(result[0][0]);
            player.setLife(Integer.parseInt(result[0][1]));
            player.setPoison(Integer.parseInt(result[0][2]));
            player.setExperience(Integer.parseInt(result[0][3]));
            player.setName(result[0][4]);
            return new ResponseEntity<>(player, new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/commanderDamage/{commander}"}, method = GET)
    public ResponseEntity<?> getCommanderDamage(HttpServletRequest headers, @PathVariable String commander) {
        String query = "SELECT damage FROM commander_damage WHERE player = ? AND enemy_player = ? AND commander = ? AND game = ?;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("email"));
            statement.setString(2, headers.getHeader("enemyPlayer"));
            statement.setString(3, commander);
            statement.setString(4, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            connection.close();
            return new ResponseEntity<>(result[0][0], new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/game/{gameId}"}, method = DELETE)
    public ResponseEntity<?> deleteGame(@PathVariable String gameId) {
        String query = "DELETE FROM games WHERE id = ? RETURNING id;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gameId);
            String[][] result = Application.query(statement);
            connection.close();
            if(result.length == 0) {
                return new ResponseEntity<>("Not Found", new HttpHeaders(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Deleted game: " + result[0][0],new HttpHeaders(), HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/player"}, method = DELETE)
    public ResponseEntity<?> deletePlayer(HttpServletRequest headers) {
        if(!verifyUser(headers.getHeader("email"), headers.getHeader("password"))) {
            return new ResponseEntity<>("Incorrect user credentials", HttpStatus.BAD_REQUEST);
        }
        String query = "DELETE FROM players WHERE email = ? RETURNING email;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("email"));
            String[][] result = Application.query(statement);
            connection.close();
            if(result.length == 0) {
                return new ResponseEntity<>("Not Found", new HttpHeaders(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Deleted player: " + result[0][0],new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/player"}, method = POST)
    public ResponseEntity<?> updatePlayer(HttpServletRequest headers, @RequestBody Player player) {
        String query = "UPDATE players SET email = ?, password = ?, name = ? WHERE email = ? RETURNING *;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getEmail());
            statement.setString(2, player.getPassword());
            statement.setString(3, player.getName());
            statement.setString(4, headers.getHeader("email"));
            String[][] result = Application.query(statement);
            connection.close();
            if(result.length < 1)
                return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
            SmallPlayer newPlayer = new SmallPlayer();
            newPlayer.setEmail(result[0][0]);
            newPlayer.setPassword("**********");
            newPlayer.setName(result[0][2]);
            return new ResponseEntity<>(newPlayer, new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/game"}, method = POST)
    public ResponseEntity<?> updateGame(HttpServletRequest headers, @RequestBody Game game) {
        String query = "UPDATE games SET id = ?, password = ?, starting_life = ? WHERE id = ? RETURNING *;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, game.getGameId());
            statement.setString(2, game.getGamePassword());
            statement.setInt(3, game.getStartingLife());
            statement.setString(4, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            connection.close();
            if(result.length < 1)
                return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
            Game newGame = game;
            newGame.setGameId(result[0][0]);
            newGame.setGamePassword(result[0][1]);
            newGame.setStartingLife(Integer.parseInt(result[0][2]));
            return new ResponseEntity<>(newGame, new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/life"}, method = POST)
    public ResponseEntity<?> updateLife(HttpServletRequest headers, @RequestBody Player player) {
        String query = "UPDATE life SET life = ?, poison = ?, experience = ? WHERE email = ? AND game = ?;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, player.getLife());
            statement.setInt(2, player.getPoison());
            statement.setInt(3, player.getExperience());
            statement.setString(4, headers.getHeader("email"));
            statement.setString(5, headers.getHeader("gameId"));
            Application.queryNoResults(statement);
            connection.close();
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        for(String enemyPlayer : player.getCommanderDamage().keySet()) {
            for(String commander : player.getCommanderDamage().get(enemyPlayer).keySet()) {
                query = "INSERT INTO commander_damage (player, enemy_player, game, commander, damage) VALUES (?, ?, ?, ?, ?) " +
                        "ON CONFLICT ON CONSTRAINT commander_damage_pkey DO UPDATE SET damage = excluded.damage;";
                try {
                    Connection connection = Application.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, player.getEmail());
                    statement.setString(2, enemyPlayer);
                    statement.setString(3, headers.getHeader("gameId"));
                    statement.setString(4, commander);
                    statement.setInt(5, player.getCommanderDamage().get(enemyPlayer).get(commander));
                    Application.queryNoResults(statement);
                    connection.close();
                }
                catch (SQLException e) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<>(player, new HttpHeaders(), HttpStatus.OK);
    }
    @RequestMapping(value = {"/gamesPlayerIsIn"}, method = GET)
    public ResponseEntity<?> getGamesPlayerIsIn(HttpServletRequest headers) {
        String query = "SELECT game FROM life WHERE email = ?;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("email"));
            String[][] result = Application.query(statement);
            connection.close();
            String[] games = new String[result.length];
            for(int i = 0; i < result.length; i++) {
                games[i] = result[i][0];
            }
            return new ResponseEntity<>(games, new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/gameSearch"}, method = GET)
    public ResponseEntity<?> searchForGame(HttpServletRequest headers) {
        String query = "SELECT * FROM games WHERE UPPER(id) LIKE UPPER(?);";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%"+headers.getHeader("gameId")+"%");
            String[][] result = Application.query(statement);
            connection.close();
            String[] games = new String[result.length];
            for(int i = 0; i < result.length; i++) {
                games[i] = result[i][0];
            }
            return new ResponseEntity<>(games, new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/startGame"}, method = GET)
    public ResponseEntity<?> startGame(HttpServletRequest headers) {
        String query = "UPDATE games SET started = true WHERE id = ?";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("gameId"));
            Application.queryNoResults(statement);
            return new ResponseEntity<>("Success", new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/hasGameStarted"}, method = GET)
    public ResponseEntity<Boolean> hasGameStarted(HttpServletRequest headers) {
        String query = "SELECT started FROM games WHERE id = ?;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            connection.close();
            System.out.println(Boolean.parseBoolean(result[0][0]));
            if(result.length > 0 && result[0].length > 0)
                return new ResponseEntity<>(Boolean.parseBoolean(result[0][0]), new HttpHeaders(), HttpStatus.OK);
            return new ResponseEntity<>(false, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
}
