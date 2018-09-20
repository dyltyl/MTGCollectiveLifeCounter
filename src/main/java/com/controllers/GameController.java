package com.controllers;

import com.Application;
import com.objects.Game;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(origins = "*")
@RestController
public class GameController {
    @RequestMapping(value= "/status", method = GET)
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Server is online", new HttpHeaders(), HttpStatus.OK);
    }
    @RequestMapping(value="/game", method = POST)
    public ResponseEntity<?> createGame(@RequestBody Game game) {
        System.out.println(Application.getJson(game, true));
        String query = "INSERT INTO games (id, password, starting_life, host, max_size) VALUES (?, digest(?, 'sha512'), ?, ?, ?);";
        Connection connection = null;
        PreparedStatement statement = null;
        ResponseEntity<?> response = null;
        try {
            connection = Application.getDataSource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, game.getGameId());
            statement.setString(2, game.getGamePassword());
            statement.setInt(3, game.getStartingLife());
            statement.setString(4, game.getHost());
            statement.setInt(5, game.getMaxSize());
            Application.queryNoResults(statement);
            response = new ResponseEntity<>("Success",new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        finally {
            try {
                if(statement != null && !statement.isClosed())
                    statement.close();
                if(connection != null && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e1) {
            }
        }
        return response;
    }
    @RequestMapping(value = "/game", method = PUT)
    public ResponseEntity<?> updateGame(HttpServletRequest headers, @RequestBody Game game) {
        String query = "UPDATE games SET id = ?, password = text(digest(?, 'sha512')), starting_life = ?, host = ?, current_size = ?, max_size = ? WHERE id = ? RETURNING *;";
        Connection connection = null;
        PreparedStatement statement = null;
        ResponseEntity<?> response = null;
        try {
            connection = Application.getDataSource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, game.getGameId());
            statement.setString(2, game.getGamePassword());
            statement.setInt(3, game.getStartingLife());
            statement.setString(4, game.getHost());
            statement.setInt(5, game.getCurrentSize());
            statement.setInt(6, game.getMaxSize());
            statement.setString(7, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            if(result.length < 1)
                response = new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
            else {
                Game newGame = game;
                newGame.setGameId(result[0][0]);
                newGame.setGamePassword("*********");
                newGame.setStartingLife(Integer.parseInt(result[0][2]));
                response = new ResponseEntity<>(newGame, new HttpHeaders(), HttpStatus.OK);
            }
        }
        catch (SQLException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        finally {
            try {
                if(statement != null && !statement.isClosed())
                    statement.close();
                if(connection != null && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e1) {
            }
        }
        return response;
    }
    @RequestMapping(value = "/game", method = DELETE)
    public ResponseEntity<?> deleteGame(HttpServletRequest headers) {
        String query = "DELETE FROM games WHERE id = ? RETURNING id;";
        Connection connection = null;
        PreparedStatement statement = null;
        ResponseEntity<?> response = null;
        try {
            connection = Application.getDataSource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            if(result.length == 0) {
                response = new ResponseEntity<>("Not Found", new HttpHeaders(), HttpStatus.NOT_FOUND);
            }
            else
                response = new ResponseEntity<>("Deleted game: " + result[0][0],new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        finally {
            try {
                if(statement != null && !statement.isClosed())
                    statement.close();
                if(connection != null && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e1) {
            }
        }
        return response;
    }
    @RequestMapping(value = "/commanders", method = GET)
    public ResponseEntity<?> getAllCommanders(HttpServletRequest headers) {
        String query = "SELECT commander, player FROM commanders WHERE game = ?;";
        Connection connection = null;
        PreparedStatement statement = null;
        ResponseEntity<?> response = null;
        try {
            connection = Application.getDataSource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            System.out.println(Application.getJson(result, true));
            response = new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        finally {
            try {
                if(statement != null && !statement.isClosed())
                    statement.close();
                if(connection != null && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e1) {
            }
        }
        return response;
    }
    @RequestMapping(value = "/game", method = GET)
    public ResponseEntity<?> searchForGame(HttpServletRequest headers) {
        String query = "SELECT * FROM games WHERE UPPER(id) LIKE UPPER(?);";
        Connection connection = null;
        PreparedStatement statement = null;
        ResponseEntity<?> response = null;
        try {
            connection = Application.getDataSource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, "%"+headers.getHeader("gameId")+"%");
            String[][] result = Application.query(statement);
            Game[] games = new Game[result.length];
            for(int i = 0; i < result.length; i++) {
                games[i] = new Game();
                games[i].setGameId(result[i][0]);
                games[i].setGamePassword("*******");
                games[i].setStartingLife(Integer.parseInt(result[i][2]));
                System.out.println(result[i][3]);
                games[i].setStarted(Boolean.parseBoolean(result[i][3]));
                games[i].setHost(result[i][4]);
                games[i].setCurrentSize(Integer.parseInt(result[i][5]));
                games[i].setMaxSize(Integer.parseInt(result[i][6]));
                System.out.println("Game started: "+games[i].getStarted());
            }
            response = new ResponseEntity<>(games, new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        finally {
            try {
                if(statement != null && !statement.isClosed())
                    statement.close();
                if(connection != null && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e1) {
            }
        }
        return response;
    }
    @RequestMapping(value = "/startGame", method = GET)
    public ResponseEntity<?> startGame(HttpServletRequest headers) {
        String query = "UPDATE games SET started = true WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResponseEntity<?> response = null;
        try {
            connection = Application.getDataSource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("gameId"));
            Application.queryNoResults(statement);
            response = new ResponseEntity<>("Success", new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        finally {
            try {
                if(statement != null && !statement.isClosed())
                    statement.close();
                if(connection != null && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e1) {
            }
        }
        return response;
    }
    @RequestMapping(value = "/hasGameStarted", method = GET)
    public ResponseEntity<Boolean> hasGameStarted(HttpServletRequest headers) {
        String query = "SELECT started FROM games WHERE id = ?;";
        Connection connection = null;
        PreparedStatement statement = null;
        ResponseEntity<Boolean> response = null;
        try {
            connection = Application.getDataSource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            if(result.length > 0 && result[0].length > 0)
                response = new ResponseEntity<>(result[0][0].equals("t"), new HttpHeaders(), HttpStatus.OK);
            else
                response = new ResponseEntity<>(false, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        catch (SQLException e) {
            response = new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        finally {
            try {
                if(statement != null && !statement.isClosed())
                    statement.close();
                if(connection != null && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e1) {
            }
        }
        return response;
    }
    @RequestMapping(value = "/host", method = PUT)
    public ResponseEntity<?> updateHost(HttpServletRequest headers) {
        String query = "UPDATE games SET host = ? WHERE id = ? RETURNING host;";
        Connection connection = null;
        PreparedStatement statement = null;
        ResponseEntity<?> response;
        try {
            connection = Application.getDataSource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, headers.getHeader("hostEmail"));
            statement.setString(2, headers.getHeader("gameId"));
            String[][] result = Application.query(statement);
            if(result.length > 0)
                response = new ResponseEntity<>(result[0][0], new HttpHeaders(), HttpStatus.OK);
            else
                response = new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        catch (SQLException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        finally {
            try {
                if(statement != null && !statement.isClosed())
                    statement.close();
                if(connection != null && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e1) {
            }
        }
        return response;
    }
    @RequestMapping(value = "/verifyGame", method = GET)
    public ResponseEntity<?> loginToGame(HttpServletRequest headers) {
        return new ResponseEntity<>(verifyGame(headers.getHeader("gameId"), headers.getHeader("gamePassword")), new HttpHeaders(), HttpStatus.OK);
    }
    public static boolean verifyGame(String gameId, String gamePassword) {
        String query = "SELECT id FROM games WHERE id = ? AND password = text(digest(?, 'sha512'));";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean response = false;
        try {
            connection = Application.getDataSource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, gameId);
            statement.setString(2, gamePassword);
            String[][] result = Application.query(statement);
            response = result.length > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            response = false;
        }
        finally {
            try {
                if(statement != null && !statement.isClosed())
                    statement.close();
                if(connection != null && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e1) {
            }
        }
        return response;
    }
    public static int getStartingLife(String gameId) {
        String query = "SELECT starting_life FROM games WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        int response = -1;
        try {
            connection = Application.getDataSource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, gameId);
            String[][] result = Application.query(statement);
            if(result.length > 0) {
                response = Integer.parseInt(result[0][0]);
            }
            else
                response = -1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            response = -1;
        }
        finally {
            try {
                if(statement != null && !statement.isClosed())
                    statement.close();
                if(connection != null && !connection.isClosed())
                    connection.close();
            }
            catch (SQLException e1) {
            }
        }
        return response;
    }
}
