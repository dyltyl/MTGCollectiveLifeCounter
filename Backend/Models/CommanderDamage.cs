using Npgsql;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MTGCollectiveLifeCounterBackend.Models {
    public class CommanderDamage {
        public string Player { get; set; }
        public string EnemyPlayer { get; set; }
        public string Commander { get; set; }
        public int Damage { get; set; }

        public static explicit operator CommanderDamage(NpgsqlDataReader reader) {
            if (reader.HasRows && reader.Read()) {
                CommanderDamage result = new CommanderDamage();
                for (int i = 0; i < reader.FieldCount; i++) {
                    switch (reader.GetName(i)) {
                        case "player":
                            result.Player = reader.GetString(i);
                            break;
                        case "enemy_player":
                            result.EnemyPlayer = reader.GetString(i);
                            break;
                        case "commander":
                            result.Commander = reader.GetString(i);
                            break;
                        case "damage":
                            result.Damage = reader.GetInt32(i);
                            break;
                    }
                }
                return result;
            }
            throw new ArgumentException("Cannot convert to Commander Damage object");
        }
        public static CommanderDamage[] GetCommanderDamageArr(NpgsqlDataReader reader) {
            List<CommanderDamage> players = new List<CommanderDamage>();
            while (reader.HasRows && reader.FieldCount > 0) {
                players.Add((CommanderDamage)reader);
            }
            return players.ToArray();
        }
    }
}
