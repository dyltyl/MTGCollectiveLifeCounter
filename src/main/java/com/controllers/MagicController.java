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
        StringBuilder builder = new StringBuilder("INSERT INTO games (\"gameId\", \"gamePassword\") VALUES ('");
        builder.append(game.getGameId());
        builder.append("', '");
        builder.append(game.getGamePassword());
        builder.append("');");
        Application.queryNoResults(builder.toString());
        return new ResponseEntity<>("Success",new HttpHeaders(), HttpStatus.OK);
    }
    @RequestMapping(value={"/joinGame"}, method = POST)
    public ResponseEntity<?> joinGame(HttpServletRequest headers, @RequestBody Player player) {
        StringBuilder builder = new StringBuilder("INSERT INTO players (\"name\", \"life\", \"poison\", \"experience\", \"game\", \"commanders\") VALUES('");
        builder.append(player.getName());
        builder.append("', '");
        builder.append(player.getLife());
        builder.append("', '");
        builder.append(player.getPoison());
        builder.append("', '");
        builder.append(player.getExperience());
        builder.append("', '");
        builder.append(headers.getHeader("gameId"));
        builder.append("', '");
        builder.append(player.getCommanders());
        builder.append("') RETURNING \"playerId\";");
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
    @RequestMapping(value = {"/verify"}, method = POST)
    public boolean verifyGame(@RequestBody Game game) {
        StringBuilder builder = new StringBuilder("SELECT \"gamePassword\" FROM games WHERE \"gameId\" = '");
        builder.append(game.getGameId());
        builder.append("';");
        ResultSet result = Application.query(builder.toString());
        try {
            result.next();
            System.out.println(result.getRow());
        }
        catch(Exception e) {

        }
        return true;
    }
}
