package com.controllers;

import com.Application;
import com.objects.CommanderDamage;
import com.objects.Player;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.controllers.GameController.verifyGame;
import static com.controllers.PlayerController.verifyUser;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@CrossOrigin(origins = "*")
@RestController
public class LifeController {
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
    @RequestMapping(value = {"/commanderDamage"}, method = PUT)
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
    @RequestMapping(value = {"/life/{life}"}, method = PUT)
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
    @RequestMapping(value = {"/poison/{poison}"}, method = PUT)
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
    @RequestMapping(value = {"/experience/{experience}"}, method = PUT)
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

}
