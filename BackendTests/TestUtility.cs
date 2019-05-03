using MTGCollectiveLifeCounterBackend;
using MTGCollectiveLifeCounterBackend.Models;
using Npgsql;
using System;
using System.Collections.Generic;
using System.Text;

namespace BackendTests {
    public class TestUtility {
        public List<Player> createdPlayers;

        public TestUtility() {
            createdPlayers = new List<Player>();
        }

        public string GenerateRandomString() {
            StringBuilder builder = new StringBuilder();
            Random random = new Random();
            char ch;
            for (int i = 0; i < 10; i++) {
                ch = Convert.ToChar(Convert.ToInt32(Math.Floor(26 * random.NextDouble() + 65)));
                builder.Append(ch);
            }
            return builder.ToString();
        }

        public Player GeneratePlayer() {
            Player player = new Player {
                Name = GenerateRandomString(),
                Email = GenerateRandomString() + "@test.com",
                Password = GenerateRandomString()
            };
            createdPlayers.Add(player);
            return player;
        }

        public Player GetPlayer(string email) {
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("SELECT * FROM players WHERE email = '" + email + "'", connection)) {
                    try {
                        using (var reader = cmd.ExecuteReader()) {
                            reader.Read();
                            return (Player)reader;
                        }
                    }
                    catch (Exception) {
                        return null;
                    }
                }
            }
        }
    }
}
