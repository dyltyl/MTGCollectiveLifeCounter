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
import java.sql.SQLException;

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
            String[][] result = Application.query(builder.toString());
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
            StringBuilder builder = new StringBuilder("INSERT INTO commanders (game, player, commander) VALUES ('");
            builder.append(headers.getHeader("gameId"));
            builder.append("', '");
            builder.append(headers.getHeader("email"));
            builder.append("', '");
            builder.append(commander);
            builder.append("');");
            try {
                Application.query(builder.toString());
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        StringBuilder builder = new StringBuilder("INSERT INTO life (email, game, life) VALUES ('");
        builder.append(headers.getHeader("email"));
        builder.append("', '");
        builder.append(headers.getHeader("gameId"));
        builder.append("', ");
        builder.append(startingLife);
        builder.append(") RETURNING email;");

        try {
            String[][] result = Application.query(builder.toString());
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
        StringBuilder builder = new StringBuilder("SELECT password FROM games WHERE id = '");
        builder.append(gameId);
        builder.append("' AND password = '");
        builder.append(gamePassword);
        builder.append("';");
        try {
            String[][] result = Application.query(builder.toString());
            return result.length > 0;
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
            String[][] result = Application.query(builder.toString());
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
        StringBuilder builder = new StringBuilder("SELECT email FROM players WHERE email = '");
        builder.append(email);
        builder.append("' AND password = '");
        builder.append(password); //TODO: Encrypt password
        builder.append("';");

       try {
           String[][] result = Application.query(builder.toString());
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
        StringBuilder builder = new StringBuilder("UPDATE life SET life = ");
        builder.append(life);
        builder.append(" WHERE email = '");
        builder.append(headers.getHeader("email"));
        builder.append("' AND game = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("' RETURNING life;");
        try {
            String[][] result = Application.query(builder.toString());
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
        StringBuilder builder = new StringBuilder("UPDATE life SET poison = ");
        builder.append(poison);
        builder.append(" WHERE email = '");
        builder.append(headers.getHeader("email"));
        builder.append("' AND game = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("' RETURNING poison;");
        try {
            String[][] result = Application.query(builder.toString());
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
        StringBuilder builder = new StringBuilder("UPDATE life SET experience = ");
        builder.append(experience);
        builder.append(" WHERE email = '");
        builder.append(headers.getHeader("email"));
        builder.append("' AND game = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("' RETURNING experience;");
        try {
            String[][] result = Application.query(builder.toString());
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
            String[][] result = Application.query(builder.toString());
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
        StringBuilder builder = new StringBuilder("SELECT commander, player FROM commanders WHERE game = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("';");
        try {
            String[][] result = Application.query(builder.toString());
            System.out.println(Application.getJson(result, true));
            return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
        }
        catch(SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/getAllPlayers"}, method = GET) //TODO: errors when there are none
    public ResponseEntity<?> getAllPlayers(HttpServletRequest headers) {
        StringBuilder builder = new StringBuilder("SELECT players.email, life, poison, experience, name FROM life JOIN players ON players.email = life.email  WHERE game = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("';");
        Player[] players;
        try {
            String[][] result = Application.query(builder.toString());
            players = new Player[result.length];
            for(int i = 0; i < result.length; i++) {
                players[i] = new Player();
                players[i].setEmail(result[i][0]);
                players[i].setLife(Integer.parseInt(result[i][1]));
                players[i].setPoison(Integer.parseInt(result[i][2]));
                players[i].setExperience(Integer.parseInt(result[i][3]));
                players[i].setName(result[i][4]);

            }
            return new ResponseEntity<>(players, new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/player"}, method = GET)
    public ResponseEntity<?> getPlayer(HttpServletRequest headers) {
        StringBuilder builder = new StringBuilder("SELECT players.email, life, poison, experience, name FROM life JOIN players ON players.email = life.email  WHERE game = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("' AND life.email = '");
        builder.append(headers.getHeader("email"));
        builder.append("';");
        try {
            String[][] result = Application.query(builder.toString());
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
        //SELECT damage FROM commander_damage WHERE player = 'Dylan@gmail.com' AND enemy_player = 'tyler@gmail.com' AND commander = 'Narset' AND game = 'newGame';
        StringBuilder builder = new StringBuilder("SELECT damage FROM commander_damage WHERE player = '");
        builder.append(headers.getHeader("email"));
        builder.append("' AND enemy_player = '");
        builder.append(headers.getHeader("enemyPlayer"));
        builder.append("' AND commander = '");
        builder.append(commander);
        builder.append("' AND game = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("';");
        try {
            String[][] result = Application.query(builder.toString());
            return new ResponseEntity<>(result[0][0], new HttpHeaders(), HttpStatus.OK);
        }
        catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = {"/game/{gameId}"}, method = DELETE)
    public ResponseEntity<?> deleteGame(@PathVariable String gameId) {
        StringBuilder builder = new StringBuilder("DELETE FROM games WHERE id = '");
        builder.append(gameId);
        builder.append("' RETURNING id;");
        try {
            String[][] result = Application.query(builder.toString());
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
        StringBuilder builder = new StringBuilder("DELETE FROM players WHERE email = '");
        builder.append(headers.getHeader("email"));
        builder.append("' RETURNING email;");
        try {
            String[][] result = Application.query(builder.toString());
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
        StringBuilder builder = new StringBuilder("UPDATE players SET email = '");
        builder.append(player.getEmail());
        if(player.getPassword() != null) {
            builder.append("', password = '");
            builder.append(player.getPassword());
        }
        if(player.getName() != null) {
            builder.append("', name = '");
            builder.append(player.getName());
        }
        builder.append("' WHERE email = '");
        builder.append(headers.getHeader("email"));
        builder.append("' RETURNING *;");
        try {
            String[][] result = Application.query(builder.toString());
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
        StringBuilder builder = new StringBuilder("UPDATE games SET id = '");
        builder.append(game.getGameId());
        builder.append("', password = '");
        builder.append(game.getGamePassword());
        builder.append("', starting_life = '");
        builder.append(game.getStartingLife());
        builder.append("' WHERE id = '");
        builder.append(headers.getHeader("gameId"));
        builder.append("' RETURNING *;");
        try {
            String[][] result = Application.query(builder.toString());
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
}
