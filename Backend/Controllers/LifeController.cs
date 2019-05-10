using Microsoft.AspNetCore.Cors;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Backend.Controllers {
    [EnableCors("*")]
    [Route("[controller]")]
    [ApiController]
    public class LifeController : ControllerBase {

    }
}
