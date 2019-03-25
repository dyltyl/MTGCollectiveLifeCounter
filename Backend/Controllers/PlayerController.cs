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
                using (NpgsqlCommand cmd = new NpgsqlCommand("UPDATE players SET email = @newEmail, password = text(digest(@newPassword, 'sha512')), name = @name WHERE email = @email AND password = text(digest(@password, 'sha512')) RETURNING *;", connection)) {
                    cmd.Parameters.AddWithValue("newEmail", player.Email);
                    cmd.Parameters.AddWithValue("newPassword", player.Password);
                    cmd.Parameters.AddWithValue("name", player.Name);
                    cmd.Parameters.AddWithValue("email", email);
                    cmd.Parameters.AddWithValue("password", password);
                    try {
                        using (var reader = cmd.ExecuteReader()) {
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
                using (NpgsqlCommand cmd = new NpgsqlCommand("DELETE FROM players WHERE email = @email AND password = text(digest(@password, 'sha512')) RETURNING *", connection)) {
                    cmd.Parameters.AddWithValue("email", email);
                    cmd.Parameters.AddWithValue("password", password);
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            return (Player)reader;
                        }
                    }
                    catch (PostgresException) {
                        return StatusCode((int)HttpStatusCode.BadRequest, "Incorrect User Credentials");
                    }
                }
            }
        }
    }
}
