using Microsoft.AspNetCore.Cors;
using Microsoft.AspNetCore.Mvc;
using MTGCollectiveLifeCounterBackend.Models;
using Npgsql;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Threading.Tasks;

namespace MTGCollectiveLifeCounterBackend.Controllers {
    [EnableCors("*")]
    [Route("[controller]")]
    [ApiController]
    public class PlayerController : ControllerBase {
        [HttpPost]
        public ActionResult<string> CreatePlayer([FromBody] Player player) {
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("INSERT INTO players (email, password, name) VALUES(@email, digest(@password, 'sha512'), @name) RETURNING email", connection)) {
                    cmd.Parameters.AddWithValue("email", player.Email);
                    cmd.Parameters.AddWithValue("password", player.Password);
                    cmd.Parameters.AddWithValue("name", player.Name);
                    try {
                        return (string)cmd.ExecuteScalar();
                    }
                    catch (PostgresException) {
                        return StatusCode((int)HttpStatusCode.Conflict, "A user already exists with the username: " + player.Email);
                    }
                }
            }
        }
        [HttpPut]
        public ActionResult<Player> UpdatePlayer([FromHeader] string email, [FromHeader] string password, [FromBody] Player player) {
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("UPDATE players SET email = @newEmail, password = text(digest(@newPassword, 'sha512')), name = @name WHERE email = @email AND password = text(digest(@password, 'sha512')) RETURNING *;", connection)) {
                    cmd.Parameters.AddWithValue("newEmail", player.Email);
                    cmd.Parameters.AddWithValue("newPassword", player.Password);
                    cmd.Parameters.AddWithValue("name", player.Name);
                    cmd.Parameters.AddWithValue("email", email);
                    cmd.Parameters.AddWithValue("password", password);
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            reader.Read();
                            return (Player)reader;
                        }
                    }
                    catch (PostgresException e) {
                        return StatusCode((int)HttpStatusCode.BadRequest, e.Message);
                    }
                }
            }
        }
        [HttpDelete]
        public ActionResult<Player> DeletePlayer([FromHeader] string email, [FromHeader] string password) {
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("DELETE FROM players WHERE email = @email AND password = text(digest(@password, 'sha512')) RETURNING *", connection)) {
                    cmd.Parameters.AddWithValue("email", email);
                    cmd.Parameters.AddWithValue("password", password);
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            reader.Read();
                            return (Player)reader;
                        }
                    }
                    catch (PostgresException) {
                        return StatusCode((int)HttpStatusCode.BadRequest, "Incorrect User Credentials");
                    }
                }
            }
        }
        [HttpGet]
        public ActionResult<Player[]> GetAllPlayers([FromHeader] string gameId) {
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                Player[] players;
                using (NpgsqlCommand cmd = new NpgsqlCommand("SELECT players.email, life, poison, experience, name FROM life JOIN players ON players.email = life.email WHERE game = @gameId", connection)) {
                    cmd.Parameters.AddWithValue("gameId", gameId);
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            players = Player.GetPlayerArr(reader);
                        }
                    }
                    catch (PostgresException) {
                        return StatusCode((int)HttpStatusCode.BadRequest, "Game Id not valid");
                    }
                }
                //Getting Commander Damage
                for (int i = 0; i < players.Length; i++) {
                    players[i].CommanderDamage = new Dictionary<string, Dictionary<string, int>>();
                    using (NpgsqlCommand cmd = new NpgsqlCommand("SELECT enemy_player, commander, damage FROM commander_damage WHERE game = @gameId AND player = @player", connection)) {
                        cmd.Parameters.AddWithValue("gameId", gameId);
                        cmd.Parameters.AddWithValue("player", players[i].Email);
                        try {
                            using (var reader = cmd.ExecuteReader()) {
                                CommanderDamage[] commanderDamage = CommanderDamage.GetCommanderDamageArr(reader);
                                players[i].CommanderDamage = new Dictionary<string, Dictionary<string, int>>();
                                foreach(CommanderDamage damage in commanderDamage) {
                                    if(!players[i].CommanderDamage.ContainsKey(damage.EnemyPlayer)) {
                                        Dictionary<string, int> dict = new Dictionary<string, int>();
                                        players[i].CommanderDamage.Add(damage.EnemyPlayer, dict);
                                    }
                                    players[i].CommanderDamage[damage.EnemyPlayer].Add(damage.Commander, damage.Damage);
                                }
                            }
                        }
                        catch (PostgresException) {
                            return StatusCode((int)HttpStatusCode.BadRequest, "Game Id not valid");
                        }
                    }
                }
                return players;
            }
        }
    }
}
