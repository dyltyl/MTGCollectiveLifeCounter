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
    [Route("[controller]")]
    [ApiController]
    public class GameController : ControllerBase {

        [HttpPost]
        public ActionResult<string> CreateGame([FromBody] Game game) {
            if (game == null) {
                return StatusCode((int)HttpStatusCode.BadRequest, "Game cannot be null");
            }
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("INSERT INTO games(id, password, starting_life, host, max_size) VALUES(@id, digest(@password, 'sha512'), @startingLife, @host, @maxSize) RETURNING id; ", connection)) {
                    cmd.Parameters.AddWithValue("id", game.GameId);
                    cmd.Parameters.AddWithValue("password", game.GamePassword);
                    cmd.Parameters.AddWithValue("startingLife", game.StartingLife);
                    cmd.Parameters.AddWithValue("host", game.Host);
                    cmd.Parameters.AddWithValue("maxSize", game.MaxSize);
                    try {
                        return (string)cmd.ExecuteScalar();
                    }
                    catch (PostgresException e) {
                        return StatusCode((int)HttpStatusCode.Conflict, "Cannot create game\n" + e.Message);
                    }
                }
            }
        }

        [HttpPut]
        public ActionResult<Game> UpdateGame([FromHeader] string gameId, [FromHeader] string gamePassword, [FromBody] Game game) {
            if (game == null) {
                return StatusCode((int)HttpStatusCode.BadRequest, "Player cannot be null");
            }
            if (gameId == null || gamePassword == null) {
                return StatusCode((int)HttpStatusCode.BadRequest, "Game credentials cannot be null");
            }
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("UPDATE games SET id = @id, password = text(digest(@password, 'sha512')), starting_life = @startingLife, host = @host, current_size = @currentSize, max_size = @maxSize WHERE id = @gameId AND password = @gamePassword RETURNING *;", connection)) {
                    cmd.Parameters.AddWithValue("id", game.GameId);
                    cmd.Parameters.AddWithValue("password", game.GamePassword);
                    cmd.Parameters.AddWithValue("startingLife", game.StartingLife);
                    cmd.Parameters.AddWithValue("host", game.Host);
                    cmd.Parameters.AddWithValue("currentSize", game.CurrentSize);
                    cmd.Parameters.AddWithValue("maxSize", game.MaxSize);
                    cmd.Parameters.AddWithValue("gameId", gameId);
                    cmd.Parameters.AddWithValue("gamePassword", gamePassword);
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            reader.Read();
                            return (Game)reader;
                        }
                    }
                    catch (PostgresException e) {
                        return StatusCode((int)HttpStatusCode.BadRequest, e.Message);
                    }
                    catch (ArgumentException) {
                        return StatusCode((int)HttpStatusCode.BadRequest, "Incorrect Game Credentials");
                    }
                }
            }
        }

        [HttpDelete]
        public ActionResult<Game> DeleteGame([FromHeader] string gameId, [FromHeader] string gamePassword) {
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("DELETE FROM games WHERE id = @id AND password = text(digest(@password, 'sha512')) RETURNING *", connection)) {
                    cmd.Parameters.AddWithValue("id", gameId);
                    cmd.Parameters.AddWithValue("password", gamePassword);
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            reader.Read();
                            return (Game)reader;
                        }
                    }
                    catch (Exception) {
                        return StatusCode((int)HttpStatusCode.BadRequest, "Incorrect Game Credentials");
                    }
                }
            }
        }
    }
}
