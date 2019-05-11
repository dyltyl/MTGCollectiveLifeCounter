using Microsoft.AspNetCore.Cors;
using Microsoft.AspNetCore.Mvc;
using MTGCollectiveLifeCounterBackend;
using MTGCollectiveLifeCounterBackend.Models;
using Npgsql;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading.Tasks;

namespace Backend.Controllers {
    [EnableCors("*")]
    [ApiController]
    public class LifeController : ControllerBase {

        [HttpGet("/commanderDamage/{commander}")] //TODO: Probably change this to return CommanderDamage object
        public ActionResult<int> GetCommanderDamage(string commander, [FromHeader] string playerEmail, [FromHeader] string enemyPlayerEmail, [FromHeader] string gameId) {
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("SELECT damage FROM commander_damage WHERE player = @player AND enemy_player = @enemyPlayer AND commander = @commander AND game = @gameId;", connection)) {
                    cmd.Parameters.AddWithValue("player", playerEmail);
                    cmd.Parameters.AddWithValue("enemyPlayer", enemyPlayerEmail);
                    cmd.Parameters.AddWithValue("gameId", gameId);
                    cmd.Parameters.AddWithValue("commander", commander);
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            reader.Read();
                            return reader.GetInt32(0);
                        }
                    }
                    catch (PostgresException e) {
                        return StatusCode((int)HttpStatusCode.BadRequest, e.Message);
                    }
                    
                }
            }
        }
        [HttpPut("/commanderDamage")]
        public ActionResult<int> SetCommanderDamage([FromBody] CommanderDamage commanderDamage, [FromHeader] string gameId, [FromHeader] string gamePassword, [FromHeader] string email, [FromHeader] string password) {
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("INSERT INTO commander_damage(player, enemy_player, game, commander, damage) Values(@player, @enemyPlayer, @gameId, @commanderName, @damage) ON CONFLICT ON CONSTRAINT commander_damage_pkey DO UPDATE SET damage = excluded.damage RETURNING damage;", connection)) {
                    cmd.Parameters.AddWithValue("player", email);
                    cmd.Parameters.AddWithValue("enemyPlayer", commanderDamage.EnemyPlayer);
                    cmd.Parameters.AddWithValue("gameId", gameId);
                    cmd.Parameters.AddWithValue("commander", commanderDamage.Commander);
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            reader.Read();
                            return reader.GetInt32(0);
                        }
                    }
                    catch (PostgresException e) {
                        return StatusCode((int)HttpStatusCode.BadRequest, e.Message);
                    }

                }
            }
        }
        [HttpPost("/life")] //TODO: This method needs optimizing
        public ActionResult<Player> SetLife([FromBody] Player player, [FromHeader] string email, [FromHeader] string gameId) {
            Player resultingPlayer;
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("UPDATE life SET life = @life, poison = @poison, experience = @experience WHERE email = email AND game = gameId RETURNING *;", connection)) {
                    cmd.Parameters.AddWithValue("player", email);
                    cmd.Parameters.AddWithValue("life", player.Life);
                    cmd.Parameters.AddWithValue("gameId", gameId);
                    cmd.Parameters.AddWithValue("poison", player.Poison);
                    cmd.Parameters.AddWithValue("experience", player.Experience);
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            reader.Read();
                            resultingPlayer = (Player)reader;
                        }
                    }
                    catch (PostgresException e) {
                        return StatusCode((int)HttpStatusCode.BadRequest, e.Message);
                    }
                }
            }
            resultingPlayer.CommanderDamage = new Dictionary<string, Dictionary<string, int>>();
            foreach(string enemyPlayer in player.CommanderDamage.Keys) {
                foreach(string commander in player.CommanderDamage[enemyPlayer].Keys) {
                    using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                        connection.Open();
                        using (NpgsqlCommand cmd = new NpgsqlCommand("INSERT INTO commander_damage (player, enemy_player, game, commander, damage) VALUES (@player, @enemyPlayer, @gameId, @commander, @damage) ON CONFLICT ON CONSTRAINT commander_damage_pkey DO UPDATE SET damage = excluded.damage RETURNING *;", connection)) {
                            cmd.Parameters.AddWithValue("player", email);
                            cmd.Parameters.AddWithValue("enemyPlayer", enemyPlayer);
                            cmd.Parameters.AddWithValue("gameId", gameId);
                            cmd.Parameters.AddWithValue("commander", commander);
                            cmd.Parameters.AddWithValue("damage", player.CommanderDamage[enemyPlayer][commander]);
                            try {
                                using (var reader = cmd.ExecuteReader()) {
                                    reader.Read();
                                    CommanderDamage commanderDamage = (CommanderDamage)reader;
                                    Dictionary<string, int> dict = new Dictionary<string, int> {
                                        { commanderDamage.Commander, commanderDamage.Damage }
                                    };
                                    resultingPlayer.CommanderDamage.Add(commanderDamage.EnemyPlayer, dict);
                                }
                            }
                            catch (PostgresException e) {
                                return StatusCode((int)HttpStatusCode.BadRequest, e.Message);
                            }
                        }
                    }
                }
            }
            return resultingPlayer;
        }
    }
}
