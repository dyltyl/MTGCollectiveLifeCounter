using Microsoft.AspNetCore.Mvc;
using MTGCollectiveLifeCounterBackend;
using MTGCollectiveLifeCounterBackend.Controllers;
using MTGCollectiveLifeCounterBackend.Models;
using Npgsql;
using System;
using System.Text;
using Xunit;

namespace BackendTests {
    public class PlayerControllerTests : IDisposable {

        private readonly PlayerController playerController;

        //Setup
        public PlayerControllerTests() {
            Startup.ConfigureConnectionString();
            playerController = new PlayerController();
        }

        //Tear Down
        public void Dispose() {

        }

        private string GenerateRandomString() {
            StringBuilder builder = new StringBuilder();
            Random random = new Random();
            char ch;
            for (int i = 0; i < 10; i++) {
                ch = Convert.ToChar(Convert.ToInt32(Math.Floor(26 * random.NextDouble() + 65)));
                builder.Append(ch);
            }
            return builder.ToString();
        }

        private Player GetPlayer(string email) {
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("SELECT * FROM players WHERE email = '" + email + "'", connection)) {
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            reader.Read();
                            return (Player)reader;
                        }
                    }
                    catch (PostgresException) {
                        return null;
                    }
                }
            }
        }

        [Fact]
        public void TestCreatePlayer() {
            //Setup
            Player player = new Player {
                Name = GenerateRandomString(),
                Email = GenerateRandomString() + "@test.com",
                Password = GenerateRandomString()
            };

            //API call
            ActionResult<string> result = playerController.CreatePlayer(player);

            //Assertions
            Assert.NotNull(result);
            Assert.Equal(player.Email, result.Value);
            Player resultPlayer = GetPlayer(player.Email);
            Assert.NotNull(resultPlayer);
            Assert.Equal(player.Email, resultPlayer.Email);
            Assert.Equal(player.Name, resultPlayer.Name);
            Assert.NotEqual(player.Password, resultPlayer.Password); //The password should be obscured

            //Tear down
            playerController.DeletePlayer(player.Email, player.Password);
        }
    }
}