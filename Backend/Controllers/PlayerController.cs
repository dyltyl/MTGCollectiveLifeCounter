using Microsoft.AspNetCore.Cors;
using Microsoft.AspNetCore.Mvc;
using MTGCollectiveLifeCounterBackend.Models;
using Npgsql;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Threading.Tasks;

namespace MTGCollectiveLifeCounterBackend.Controllers {
    [EnableCors("*")]
    [Route("[controller]")]
    [ApiController]
    public class PlayerController : ControllerBase {
        [HttpGet]
        public ActionResult<string> Get() {
            // quite complex sql statement
            string sql = "SELECT * FROM information_schema.tables";
            string result = "";
            using (var cmd = new NpgsqlCommand(sql, Program.Connection)) {
                using (var reader = cmd.ExecuteReader()) {
                    reader.Read();
                    result = reader.GetString(0);
                }
            }
            return result;
        }
    }
}
