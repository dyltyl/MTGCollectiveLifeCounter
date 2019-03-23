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
            string value = Environment.GetEnvironmentVariable("JDBC_DATABASE_URL");
            Player player = new Player {
                Name = value
            };
            return player;
        }
    }
}
