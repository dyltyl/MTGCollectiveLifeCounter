using Backend.Controllers;
using MTGCollectiveLifeCounterBackend;
using MTGCollectiveLifeCounterBackend.Controllers;
using MTGCollectiveLifeCounterBackend.Models;
using Npgsql;
using System;
using System.Collections.Generic;
using System.Text;

namespace BackendTests {
    public abstract class TestBase : IDisposable {
        protected readonly GameController gameController;
        protected readonly PlayerController playerController;
        protected List<Player> createdPlayers;
        protected List<Game> createdGames;

        //Setup
        public TestBase() {
            Startup.ConfigureConnectionString();
            gameController = new GameController();
            playerController = new PlayerController();
            createdPlayers = new List<Player>();
            createdGames = new List<Game>();
        }

        //Tear Down
        public void Dispose() {
            foreach (Player player in createdPlayers) {
                playerController.DeletePlayer(player.Email, player.Password);
            }
            createdPlayers = new List<Player>();
            foreach (Game game in createdGames) {
                gameController.DeleteGame(game.GameId, game.GamePassword);
            }
        }

        public static string GenerateRandomString() {
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

        public Game GenerateGame() {
            Player host = GeneratePlayer();
            playerController.CreatePlayer(host);
            Game game = new Game {
                GameId = GenerateRandomString(),
                GamePassword = GenerateRandomString(),
                Host = host.Email,
                StartingLife = 40,
                MaxSize = 5
            };
            createdGames.Add(game);
            return game;
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

        public Game GetGame(string gameId) {
            using (NpgsqlConnection connection = new NpgsqlConnection(Program.ConnectionString)) {
                connection.Open();
                using (NpgsqlCommand cmd = new NpgsqlCommand("SELECT * FROM games WHERE id = '" + gameId + "'", connection)) {
                    using (var reader = cmd.ExecuteReader()) {
                        reader.Read();
                        return (Game)reader;
                    }
                }
            }
        }
    }
}
