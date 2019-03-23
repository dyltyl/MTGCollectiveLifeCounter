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
        public string Username { get; set; }
        [Required]
        public string Password { get; set; }
        public int Life { get; set; }
        public int Poison { get; set; }
        public int Experience { get; set; }
        public Dictionary<string, Dictionary<string, int>> CommanderDamage;
    }
}
