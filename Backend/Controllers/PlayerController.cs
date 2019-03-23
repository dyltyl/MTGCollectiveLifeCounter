using Microsoft.AspNetCore.Cors;
using Microsoft.AspNetCore.Mvc;
using MTGCollectiveLifeCounterBackend.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MTGCollectiveLifeCounterBackend.Controllers {
    [EnableCors("*")]
    [Route("[controller]")]
    [ApiController]
    public class PlayerController : ControllerBase {
        [HttpGet]
        public ActionResult<Player> Get() {
            string dbUrl = Environment.GetEnvironmentVariable("JDBC_DATABASE_URL");
            string dbUsername = Environment.GetEnvironmentVariable("JDBC_DATABASE_USERNAME");
            Player player = new Player {
                Name = dbUrl,
                Username = dbUsername
            };
            return player;
        }
    }
}
