package com.controllers;

import com.Application;
import com.objects.Game;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "*")
@RestController
public class GameController {
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
            if(result.length > 0 && result[0].length > 0)
                return new ResponseEntity<>(result[0][0].equals("t"), new HttpHeaders(), HttpStatus.OK);
            return new ResponseEntity<>(false, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
    public static boolean verifyGame(String gameId, String gamePassword) {
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
    public static int getStartingLife(String gameId) {
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
}
