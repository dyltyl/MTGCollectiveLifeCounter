using Npgsql;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace MTGCollectiveLifeCounterBackend.Models {
    public class Game {
        [Required]
        public string GameId { get; set; }
        [Required]
        public string GamePassword { get; set; }
        [Required]
        public string Host { get; set; }
        [Required]
        public int StartingLife { get; set; }
        [Required]
        public int MaxSize { get; set; }
        public int CurrentSize { get; set; }
        public bool Started { get; set; }

        public static explicit operator Game(NpgsqlDataReader reader) {
            if (reader.HasRows && reader.IsOnRow) {
                Game result = new Game();
                for (int i = 0; i < reader.FieldCount; i++) {
                    switch (reader.GetName(i)) {
                        case "id":
                            result.GameId = reader.GetString(i);
                            break;
                        case "password":
                            result.GamePassword = "*********";
                            break;
                        case "starting_life":
                            result.StartingLife = reader.GetInt32(i);
                            break;
                        case "started":
                            result.Started = reader.GetBoolean(i);
                            break;
                        case "host":
                            result.Host = reader.GetString(i);
                            break;
                        case "current_size":
                            result.CurrentSize = reader.GetInt32(i);
                            break;
                        case "max_size":
                            result.CurrentSize = reader.GetInt32(i);
                            break;
                    }
                }
                return result;
            }
            throw new ArgumentException("Cannot convert result into Game object");
        }
    }
}
