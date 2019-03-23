using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MTGCollectiveLifeCounterBackend.Models {
    public class Game {
        public string GameId { get; set; }
        public string GamePassword { get; set; }
        public string Host { get; set; }
        public int StartingLife { get; set; }
        public int CurrentSize { get; set; }
        public int MaxSize { get; set; }
        public bool Started { get; set; }
    }
}
