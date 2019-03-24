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
            NpgsqlConnection conn = new NpgsqlConnection(Startup.ConnectionString);
            conn.Open();
            // quite complex sql statement
            string sql = "SELECT * FROM information_schema.tables";
            // data adapter making request from our connection
            NpgsqlDataAdapter da = new NpgsqlDataAdapter(sql, conn);
            // i always reset DataSet before i do
            // something with it.... i don't know why :-)
            DataSet ds = new DataSet();
            DataTable dt = new DataTable();
            ds.Reset();
            // filling DataSet with result from NpgsqlDataAdapter
            da.Fill(ds);
            // since it C# DataSet can handle multiple tables, we will select first
            dt = ds.Tables[0];
            Object[] x = dt.Rows[0].ItemArray;            
            conn.Close();
            return x[0].ToString();
        }
    }
}
