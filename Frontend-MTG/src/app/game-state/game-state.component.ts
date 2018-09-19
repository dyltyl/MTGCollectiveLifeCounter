import { Component, OnInit } from '@angular/core';
import { Player } from '../player';
import { GameService } from '../game.service';
import { PlayerService } from '../player.service';
import { DataService } from '../data.service';

@Component({
  selector: 'app-game-state',
  templateUrl: './game-state.component.html',
  styleUrls: ['./game-state.component.css']
})
export class GameStateComponent implements OnInit {
  public players: Player[] = [];
  public currentPlayer = this.dataService.getCurrentPlayer();
  public currentStat = Stats.Life;
  constructor(private gameService: GameService, private playerService: PlayerService, private dataService: DataService) { }

  ngOnInit() {
  }
  switchTo(stat: Stats) {
    this.currentStat = stat;
  }
  addStat(amount) {
    if (this.currentStat === Stats.Life) {
      this.currentPlayer.life += amount;
    } else if (this.currentStat === Stats.Poison) {
      this.currentPlayer.poison += amount;
      console.log(this.currentPlayer.poison);
    } else if (this.currentStat === Stats.Experience) {
      this.currentPlayer.experience += amount;
    }
    this.playerService.updateLifeStats(this.dataService.getGame().gameId, this.currentPlayer).subscribe(
      result => {
      },
      err => {
      }
    );
  }
  refresh() {

  }
  adjustArrays(data: Player[]) {
    this.players = [];
    for (let i = 0; i < data.length; i++) {
      if (this.currentPlayer.email !== data[i].email) {
        this.players.push(data[i]);
      }
    }
  }
}
enum Stats {
    Life = 0, Poison = 1, Experience = 2
}
