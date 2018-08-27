package com.controllers;

import com.Application;
import com.objects.Game;
import com.objects.Player;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "*")
@RestController
public class MagicController {

    @RequestMapping(value= {"/status"}, method = GET)
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Server is online", new HttpHeaders(), HttpStatus.OK);
    }
    @RequestMapping(value={"/createGame"}, method = POST)
    public ResponseEntity<?> createGame(@RequestBody Game game) {
        StringBuilder builder = new StringBuilder("INSERT INTO games (id, password) VALUES ('");
        builder.append(game.getGameId());
        builder.append("', '");
        builder.append(game.getGamePassword());
        builder.append("');");
        try {
            Application.queryNoResults(builder.toString());
        }
        catch(Exception e) {
            return new ResponseEntity<>("Cannot create game", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Success",new HttpHeaders(), HttpStatus.OK);
    }
    @RequestMapping(value={"/createPlayer"}, method = POST)
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        StringBuilder builder = new StringBuilder("INSERT INTO players (email, password, name) VALUES('");
        builder.append(player.getEmail());
        builder.append("', '");
        builder.append(player.getPassword());
        builder.append("', '");
        builder.append(player.getName());
        builder.append("') RETURNING email;");
        ResultSet result = Application.query(builder.toString());
        try {
            result.next();
            String email = result.getString(1);
            return new ResponseEntity<>(email,new HttpHeaders(), HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/joinGame"}, method = POST)
    public ResponseEntity<?> joinGame(HttpServletRequest headers) {
        Enumeration<String> keys = headers.getHeaderNames();
        while(keys.hasMoreElements()) {
            System.out.println(keys.nextElement());
        }
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
        StringBuilder builder = new StringBuilder("INSERT INTO life (email, game, life) VALUES ('");
        builder.append(headers.getHeader("email"));
        builder.append("', '");
        builder.append(headers.getHeader("gameId"));
        builder.append("', ");
        builder.append(startingLife);
        builder.append(") RETURNING email;");
        ResultSet result = Application.query(builder.toString());
        try {
            result.next();
            String email = result.getString(1);
            return new ResponseEntity<>(email, new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }
    public boolean verifyGame(String gameId, String gamePassword) {
        StringBuilder builder = new StringBuilder("SELECT password FROM games WHERE id = '");
        builder.append(gameId);
        builder.append("';");
        ResultSet result = Application.query(builder.toString());
        try {
            result.next();
            if(result.getRow() > 0) {
                if(gamePassword.equals(result.getString(1))) {
                    return true;
                }
            }
            return false;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getStartingLife(String gameId) {
        StringBuilder builder = new StringBuilder("SELECT starting_life FROM games WHERE id = '");
        builder.append(gameId);
        builder.append("';");
        ResultSet result = Application.query(builder.toString());
        try {
            result.next();
            if(result.getRow() > 0) {
                return result.getInt(1);
            }
            return -1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
   public boolean verifyUser(String email, String password) {
        StringBuilder builder = new StringBuilder("SELECT email FROM players WHERE email = '");
        builder.append(email);
        builder.append("' AND password = '");
        builder.append(password); //TODO: Encrypt password
        builder.append("';");
        ResultSet result = Application.query(builder.toString());
       try {
           result.next();
           if(result.getRow() == 1) {
               return true;
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return false;
   }
}
