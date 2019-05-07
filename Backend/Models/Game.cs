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
    }
}
