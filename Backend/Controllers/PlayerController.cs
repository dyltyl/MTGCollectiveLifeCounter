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
            string dbUrl = Environment.GetEnvironmentVariable("DATABASE_URL");
            string dbUsername = Environment.GetEnvironmentVariable("DATABASE_USERNAME");
            Player player = new Player {
                Name = dbUrl,
                Username = dbUsername
            };
            return player;
        }
    }
}
