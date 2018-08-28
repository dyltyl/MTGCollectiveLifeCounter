package com.controllers;

import com.Application;
import com.objects.CommanderDamage;
import com.objects.Game;
import com.objects.Player;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@CrossOrigin(origins = "*")
@RestController
public class MagicController {

    @RequestMapping(value= {"/status"}, method = GET)
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Server is online", new HttpHeaders(), HttpStatus.OK);
    }
    @RequestMapping(value={"/createGame"}, method = POST)
    public ResponseEntity<?> createGame(@RequestBody Game game) {
        StringBuilder builder = new StringBuilder("INSERT INTO games (id, password, starting_life) VALUES ('");
        builder.append(game.getGameId());
        builder.append("', '");
        builder.append(game.getGamePassword());
        builder.append("', ");
        builder.append(game.getStartingLife());
        builder.append(");");
        try {
            Application.queryNoResults(builder.toString());
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
        try {
            List<String[]> result = Application.query(builder.toString());
            String email = result.get(0)[0];
            return new ResponseEntity<>(email,new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/joinGame"}, method = POST)
    public ResponseEntity<?> joinGame(HttpServletRequest headers) {
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

        try {
            List<String[]> result = Application.query(builder.toString());
            if(result.size() > 0) {
                String email = result.get(0)[0];
                return new ResponseEntity<>(email, new HttpHeaders(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    public boolean verifyGame(String gameId, String gamePassword) {
        StringBuilder builder = new StringBuilder("SELECT password FROM games WHERE id = '");
        builder.append(gameId);
        builder.append("' AND password = '");
        builder.append(gamePassword);
        builder.append("';");
        try {
            List<String[]> result = Application.query(builder.toString());
            if(result.size() > 0) {
                return true;
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
        try {
            List<String[]> result = Application.query(builder.toString());
            if(result.size() > 0) {
                return Integer.parseInt(result.get(0)[0]);
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

       try {
           List<String[]> result = Application.query(builder.toString());
           if(result.size() == 1) {
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
        StringBuilder builder = new StringBuilder("UPDATE life SET life = ");
        builder.append(life);
        builder.append(" WHERE email = '");
        builder.append(headers.getHeader("email"));
        builder.append("' AND game = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("' RETURNING life;");
        try {
            List<String[]> result = Application.query(builder.toString());
            if(result.size() > 0) {
                return new ResponseEntity<>(result.get(0)[0], new HttpHeaders(), HttpStatus.OK);
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
        StringBuilder builder = new StringBuilder("UPDATE life SET poison = ");
        builder.append(poison);
        builder.append(" WHERE email = '");
        builder.append(headers.getHeader("email"));
        builder.append("' AND game = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("' RETURNING poison;");
        try {
            List<String[]> result = Application.query(builder.toString());
            if(result.size() > 0) {
                return new ResponseEntity<>(result.get(0)[0], new HttpHeaders(), HttpStatus.OK);
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
        StringBuilder builder = new StringBuilder("UPDATE life SET experience = ");
        builder.append(experience);
        builder.append(" WHERE email = '");
        builder.append(headers.getHeader("email"));
        builder.append("' AND game = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("' RETURNING experience;");
        try {
            List<String[]> result = Application.query(builder.toString());
            if(result.size() > 0) {
                return new ResponseEntity<>(result.get(0)[0], new HttpHeaders(), HttpStatus.OK);
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
        StringBuilder builder = new StringBuilder("INSERT INTO commander_damage (player, enemy_player, game, commander, damage) VALUES ('");
        builder.append(damage.getPlayer());
        builder.append("', '");
        builder.append(damage.getEnemyPlayer());
        builder.append("', '");
        builder.append(headers.getHeader("gameId"));
        builder.append("', '");
        builder.append(damage.getCommander());
        builder.append("', ");
        builder.append(damage.getDamage());
        builder.append(") ON CONFLICT ON CONSTRAINT commander_damage_pkey DO UPDATE SET damage = excluded.damage RETURNING damage;");
        try {
            List<String[]> result = Application.query(builder.toString());
            if(result.size() > 0) {
                System.out.println(Application.getJson(damage));
                return new ResponseEntity<>(result.get(0)[0], new HttpHeaders(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        catch(SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
