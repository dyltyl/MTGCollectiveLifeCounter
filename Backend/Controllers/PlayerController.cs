using Microsoft.AspNetCore.Cors;
using Microsoft.AspNetCore.Mvc;
using MTGCollectiveLifeCounterBackend.Models;
using Npgsql;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace MTGCollectiveLifeCounterBackend.Controllers {
    [EnableCors("*")]
    [Route("[controller]")]
    [ApiController]
    public class PlayerController : ControllerBase {
        [HttpPost]
        public ActionResult<string> CreatePlayer([FromBody] Player player) {
            string test = JsonConvert.SerializeObject(player);
            Console.WriteLine(test);
            Program.Connection.Open();
            string result = "";
            using(NpgsqlCommand cmd = new NpgsqlCommand("INSERT INTO players (email, password, name) VALUES(@email, digest(@password, 'sha512'), @name) RETURNING email")) {
                cmd.Parameters.AddWithValue("email", player.Username);
                cmd.Parameters.AddWithValue("password", player.Password);
                cmd.Parameters.AddWithValue("name", player.Name);
                result = (string)cmd.ExecuteScalar();
            }
            Program.Connection.Close();
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
