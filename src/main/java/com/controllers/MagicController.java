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
    public ResponseEntity<?> joinGame(@RequestBody Player player) {
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
            int id = result.getInt(1);
            return new ResponseEntity<>(id,new HttpHeaders(), HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>("-1", HttpStatus.BAD_REQUEST);
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
   /* public ResponseEntity<?> loseLife(@RequestBody Player player) {
        return null;
    }*/
}
