package com.controllers;

import com.Application;
import com.objects.Game;
import com.objects.Player;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO games (\"gameId\", \"gamePassword\") VALUES ('");
        builder.append(game.getGameId());
        builder.append("', '");
        builder.append(game.getGamePassword());
        builder.append("');");
        Application.queryNoResults(builder.toString());
        return new ResponseEntity<>("Success",new HttpHeaders(), HttpStatus.OK);
    }
    @RequestMapping(value={"/joinGame"}, method = POST)
    public ResponseEntity<?> joinGame(@RequestBody Game game, @RequestBody Player player) {
        System.out.println(game);
        System.out.println(player);

        return new ResponseEntity<>("Success",new HttpHeaders(), HttpStatus.OK);
    }
}
