import { Component, OnInit } from '@angular/core';
import { Player } from '../player';
import { DataService } from '../data.service';
import { PlayerService } from '../player.service';
import { GameService } from '../game.service';
import { Game } from '../game';

@Component({
  selector: 'app-waiting-lobby',
  templateUrl: './waiting-lobby.component.html',
  styleUrls: ['./waiting-lobby.component.css']
})
export class WaitingLobbyComponent implements OnInit {
  public players: Player[] = [];
  public currentPlayer = this.dataService.getCurrentPlayer();
  public game: Game;
  constructor(private gameService: GameService, private playerService: PlayerService, private dataService: DataService) { }

  ngOnInit() {
    this.game = this.dataService.getGame();
    this.refresh();
  }
  refresh() {
    this.updateGame();
    this.gameService.getPlayers(this.game).subscribe(
      result => {
        this.adjustArray(result);
      },
      err => {
        console.log(err);
      }
    );
    setTimeout(_ => {
      this.refresh();
    }, 5000);
  }
  adjustArray(data: Player[]) {
    const gameSize = this.game.maxSize;
    this.players = [];
    for (let i = 0; i < gameSize; i++) {
      if (i < data.length) { // Player
        this.players.push(data[i]);
        if (this.currentPlayer.email === data[i].email) {
          this.currentPlayer = data[i];
        }
      } else { // Waiting Slot
        this.players.push(null);
      }
    }
  }
  updateGame() {
    this.gameService.getGame(this.game.gameId).subscribe(
      result => {
        this.game = result[0];
      },
      err => {
        console.log(err);
      }
    );
  }
  kickPlayer(player: Player) {
    const index = this.players.indexOf(player);
    this.playerService.leaveGame(player.email, this.game.gameId).subscribe(
      result => {
      },
      err => {
        console.log(err);
      }
    );
    this.players[index] = null;
  }
  addSlot() {
    if (this.players.length < 8) {
      this.players.push(null);
      this.game.maxSize++;
      this.gameService.updateGame(this.game).subscribe(
        result => {
        },
        err => {
          console.log(err);
        }
      );
    } else {
      console.log('Cannot add any more players');
    }
  }
  removeSlot(index: number) {
    this.players.splice(index, 1);
    this.game.maxSize--;
    this.gameService.updateGame(this.game).subscribe(
      result => {
      },
      err => {
        console.log(err);
      }
    );
  }

}
