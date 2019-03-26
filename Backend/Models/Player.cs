using Npgsql;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace MTGCollectiveLifeCounterBackend.Models {
    public class Player {
        [Required]
        public string Name { get; set; }
        [Required]
        public string Email { get; set; }
        [Required]
        public string Password { get; set; }
        public int Life { get; set; }
        public int Poison { get; set; }
        public int Experience { get; set; }
        public Dictionary<string, Dictionary<string, int>> CommanderDamage;
        public static explicit operator Player(NpgsqlDataReader reader) {
            if(reader.HasRows && reader.Read()) {
                Player result = new Player();
                for(int i = 0; i < reader.FieldCount; i++) {
                    switch(reader.GetName(i)) {
                        case "name":
                            result.Name = reader.GetString(i);
                            break;
                        case "email":
                            result.Email = reader.GetString(i);
                            break;
                        case "password":
                            result.Password = "*********";
                            break;
                        case "life":
                            result.Life = reader.GetInt32(i);
                            break;
                        case "poison":
                            result.Poison = reader.GetInt16(i);
                            break;
                        case "experience":
                            result.Experience = reader.GetInt32(i);
                            break;
                    }
                }
                return result;
            }
            throw new ArgumentException("Cannot convert result into Player object");
        }
        public static Player[] GetPlayerArr(NpgsqlDataReader reader) {
            List<Player> players = new List<Player>();
            while(reader.HasRows && (reader.IsOnRow || players.Count == 0)) {
                Console.WriteLine(reader.FieldCount);
                Player player = (Player)reader;
                Console.WriteLine(player.Email);
                players.Add(player);
                
            }
            return players.ToArray();
        }
    }
}
