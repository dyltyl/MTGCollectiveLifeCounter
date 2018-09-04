package com.controllers;

import com.Application;
import com.objects.Player;
import com.objects.SmallPlayer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import static com.controllers.GameController.getStartingLife;
import static com.controllers.GameController.verifyGame;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(origins = "*")
@RestController
public class PlayerController {
    @RequestMapping(value="/player", method = POST)
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        String query = "INSERT INTO players (email, password, name) VALUES(?, digest(?, 'sha512'), ?) RETURNING email";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getEmail());
            statement.setString(2, player.getPassword());
            statement.setString(3, player.getName());
            String[][] result = Application.query(statement);
            statement.close();
            connection.close();
            System.out.println(statement.isClosed());
            System.out.println(connection.isClosed());
            String email = result[0][0];
            return new ResponseEntity<>(email,new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/player", method = PUT)
    public ResponseEntity<?> updatePlayer(HttpServletRequest headers, @RequestBody Player player) {
        String query = "UPDATE players SET email = ?, password = text(digest(?, 'sha512')), name = ? WHERE email = ? RETURNING *;";
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
    @RequestMapping(value = "/player", method = DELETE)
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
    @RequestMapping(value = "/players", method = GET) //TODO: errors when there are none
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
    @RequestMapping(value = "/player", method = GET)
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
    @RequestMapping(value = "/gamesPlayerIsIn", method = GET)
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
    @RequestMapping(value = "/joinGame", method = POST)
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
    @RequestMapping(value = "/leaveGame", method = DELETE)
    public ResponseEntity<?> leaveGame(HttpServletRequest headers) {
        String query = "DELETE FROM commander_damage WHERE player = ? AND game = ?; " +
                "DELETE FROM commanders WHERE player = ? AND game = ?; " +
                "DELETE FROM life WHERE email = ? AND game = ?;";
        try {
            Connection connection = Application.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("email"));
            statement.setString(2, headers.getHeader("gameId"));
            statement.setString(3, headers.getHeader("email"));
            statement.setString(4, headers.getHeader("gameId"));
            statement.setString(5, headers.getHeader("email"));
            statement.setString(6, headers.getHeader("gameId"));
            Application.queryNoResults(statement);
            return new ResponseEntity<>("Success", new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>("Something went wrong", new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/login", method = GET)
    public ResponseEntity<?> login(HttpServletRequest headers) {
        return new ResponseEntity<>(verifyUser(headers.getHeader("email"), headers.getHeader("password")), new HttpHeaders(), HttpStatus.OK);
    }
    public static boolean verifyUser(String email, String password) {
        String query = "SELECT email FROM players WHERE email = ? AND password = text(digest(?, 'sha512'));";
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
}
