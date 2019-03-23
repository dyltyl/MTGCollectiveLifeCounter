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
    }
}
