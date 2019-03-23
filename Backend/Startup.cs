using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;

namespace MTGCollectiveLifeCounterBackend {
    public class Startup {
        public Startup(IConfiguration configuration) {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }
        public static string ConnectionString { get; set; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services) {
            services.AddMvc().SetCompatibilityVersion(CompatibilityVersion.Version_2_1);

            //Get Database Connection 
            string connectionString = Environment.GetEnvironmentVariable("DATABASE_URL");
            connectionString.Replace("//", "");

            char[] delimiterChars = { '/', ':', '@', '?' };
            string[] strConn = connectionString.Split(delimiterChars);
            strConn = strConn.Where(x => !string.IsNullOrEmpty(x)).ToArray();

            string User = strConn[1];
            string Pass = strConn[2];
            string Server = strConn[3];
            string Database = strConn[5];
            string Port = strConn[4];
            ConnectionString = "host=" + Server + ";port=" + Port + ";database=" + Database + ";uid=" + User + ";pwd=" + Pass + ";sslmode=Require;Trust Server Certificate=true;Timeout=1000";
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env) {
            if (env.IsDevelopment()) {
                app.UseDeveloperExceptionPage();
            }
            else {
                app.UseHsts();
            }

            app.UseHttpsRedirection();
            app.UseMvc();
        }
    }
}
