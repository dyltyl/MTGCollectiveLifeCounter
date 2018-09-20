import { Component, OnInit } from '@angular/core';
import { PlayerService } from '../player.service';
import { GameService } from '../game.service';
import { DataService } from '../data.service';
import { Player } from '../player';
import { Game } from '../game';
import { Router } from '@angular/router';

@Component({
  selector: 'app-player-creation',
  templateUrl: './player-creation.component.html',
  styleUrls: ['./player-creation.component.css']
})
export class PlayerCreationComponent implements OnInit {
  public playerName = '';
  public commanderAName = '';
  public commanderBName = '';
  public email = '';
  public password = '';
  public hasPartner = false;

  constructor(private playerService: PlayerService, private gameService: GameService,
    private dataService: DataService, private router: Router) { }

  ngOnInit() {
  }
  createPlayer() {
    if (this.playerName.trim().length < 1) {
      throw new Error('Player name must be set');
    }
    if (this.email.trim().length < 1) {
      throw new Error('Player email must be set');
    }
    const game = this.dataService.getGame();
    if (game === null || game.gameId === null) {
      throw new Error('The game must be set');
    }
    const commanders: string[] = [];
    if (this.commanderAName.trim().length > 0) {
      commanders.push(this.commanderAName);
    }
    if (this.hasPartner && this.commanderBName.trim().length > 0) {
      commanders.push(this.commanderBName);
    }
    const player = new Player(this.playerName, this.email, this.password, -1);
    this.dataService.setCurrentPlayer(player);
    this.playerService.createPlayer(player).subscribe(
      result => {
        this.joinGame(player, game, commanders);
      },
      err => { throw err; }
    );
  }
  joinGame(player: Player, game: Game, commanders: string[]) { // TODO I think there are still some bugs involved with joining games
    this.playerService.joinGame(player, game, commanders).subscribe(
      result => {
        if (this.dataService.isHost()) {
          game.host = player.email;
          console.log(game);
          this.dataService.setGame(game);
          this.gameService.updateHost(game).subscribe(
            res => {
            },
            err => {
              console.log(err);
            }
          );
        }
        this.router.navigate(['WaitingLobby']);
      },
      err => {
        this.deletePlayer(player);
        throw err;
      }
    );
  }
  deletePlayer(player: Player) {
    this.playerService.deletePlayer(player.email, player.password).subscribe(
      result => {
        console.log('Deleted: ' + player.email);
      },
      err => { throw err; }
    );
  }

}
