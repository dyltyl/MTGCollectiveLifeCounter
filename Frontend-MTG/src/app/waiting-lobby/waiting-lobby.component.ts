import { Component, OnInit } from '@angular/core';
import { Player } from '../player';
import { DataService } from '../data.service';
import { PlayerService } from '../player.service';
import { GameService } from '../game.service';
import { Game } from '../game';
import { Router } from '@angular/router';

@Component({
  selector: 'app-waiting-lobby',
  templateUrl: './waiting-lobby.component.html',
  styleUrls: ['./waiting-lobby.component.css']
})
export class WaitingLobbyComponent implements OnInit {
  public players: Player[] = [];
  public currentPlayer = this.dataService.getCurrentPlayer();
  public game: Game;
  constructor(private gameService: GameService, private playerService: PlayerService,
    private dataService: DataService, private router: Router) { }

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
    if (!this.game.started) {
      console.log(this.game.started);
      setTimeout(_ => {
        this.refresh();
      }, 5000);
    } else {
      this.dataService.setCurrentPlayer(this.currentPlayer);
      this.router.navigate(['GameState']);
    }
  }
  adjustArray(data: Player[]) {
    const gameSize = this.game.maxSize;
    this.players = [];
    for (let i = 0; i < gameSize; i++) {
      if (i < data.length) { // Player
        this.players.push(data[i]);
        if (this.currentPlayer.email === data[i].email) {
          this.currentPlayer = data[i];
          console.log(this.currentPlayer);
        }
      } else { // Waiting Slot
        this.players.push(null);
      }
    }
  }
  updateGame() {
    this.gameService.getGame(this.game.gameId).subscribe(
      result => {
        const password = this.game.gamePassword;
        this.game = result[0];
        console.log(this.game);
        this.game.gamePassword = password;
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
  startGame() {
    this.dataService.setCurrentPlayer(this.currentPlayer);
    this.gameService.startGame(this.game).subscribe(
      result => {
        this.router.navigate(['GameState']);
      },
      err => {
        console.log(err);
      }
    );
  }

}
