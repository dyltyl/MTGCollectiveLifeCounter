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
        public ActionResult<string> CreateGame([FromBody] Game  game) {
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
                        return StatusCode((int)HttpStatusCode.Conflict, "Cannot create game\n"+e.Message);
                    }
                }
            }
        }
    }
}
