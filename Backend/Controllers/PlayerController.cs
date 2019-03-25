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
            Program.Connection.Open();
            string result = "";
            using(NpgsqlCommand cmd = new NpgsqlCommand("INSERT INTO players (email, password, name) VALUES(@email, digest(@password, 'sha512'), @name) RETURNING email", Program.Connection)) {
                cmd.Parameters.AddWithValue("email", player.Email);
                cmd.Parameters.AddWithValue("password", player.Password);
                cmd.Parameters.AddWithValue("name", player.Name);
                try {
                    result = (string)cmd.ExecuteScalar();
                }
                catch(PostgresException e) {
                    return StatusCode((int)HttpStatusCode.Conflict, "A user already exists with the username: " + player.Email);
                } 
                finally {
                    Program.Connection.Close();
                }
            }
            return result;
        }
        [HttpPut]
        public ActionResult<Player> UpdatePlayer([FromHeader] string email, [FromHeader] string password, [FromBody] Player player) {
            Program.Connection.Open();
            Player result = null;
            using(NpgsqlCommand cmd = new NpgsqlCommand("UPDATE players SET email = @newEmail, password = digest(@newPassword, 'sha512'), name = @name WHERE email = @email AND password = digest(@password, 'sha512') RETURNING *;", Program.Connection)) {
                cmd.Parameters.AddWithValue("newEmail", player.Email);
                cmd.Parameters.AddWithValue("newPassword", player.Password);
                cmd.Parameters.AddWithValue("name", player.Name);
                cmd.Parameters.AddWithValue("email", email);
                cmd.Parameters.AddWithValue("password", password);
                try {
                    using (var reader = cmd.ExecuteReader()) {
                        result = (Player)reader;
                    }
                }
                catch(PostgresException e) {
                    return StatusCode((int)HttpStatusCode.BadRequest, e.Message);
                }
                finally {
                    Program.Connection.Close();
                }
            }
            return result;
        }
        /*[HttpGet]
        public ActionResult<string> Get() {
            // quite complex sql statement
            string sql = "SELECT table_name FROM information_schema.tables";
            string result = "";
            Program.Connection.Open();
            using (var cmd = new NpgsqlCommand(sql, Program.Connection)) {
                using (var reader = cmd.ExecuteReader()) {
                    Console.WriteLine(reader.FieldCount);
                    while(reader.Read()) {
                        result += reader.GetString(0) + "\n";
                    }
                }
            }
            Program.Connection.Close();
            return result;
        }*/
    }
}
