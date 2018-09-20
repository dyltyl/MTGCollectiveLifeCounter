import { Component, OnInit } from '@angular/core';
import { GameService } from '../game.service';
import { DataService } from '../data.service';
import { Game } from '../game';
import { Router } from '@angular/router';

@Component({
  selector: 'app-game-creation',
  templateUrl: './game-creation.component.html',
  styleUrls: ['./game-creation.component.css']
})
export class GameCreationComponent implements OnInit {
  public gameSize = 2;
  public baseLife = 40;
  public gameName = '';
  public gamePassword = '';
  constructor(public gameService: GameService, public dataService: DataService, private router: Router) { }

  ngOnInit() {
  }
  createGame() {
    if (this.gameSize > 8 || this.gameSize < 2) {
      throw new Error('There must be between 2 and 8 players');
    }
    if (this.baseLife < 1) {
      throw new Error('Life must start at a value above 0');
    }
    if (this.gameName.trim().length < 1) {
      throw new Error('Please name the game');
    }
    const game = new Game(this.gameName, this.gamePassword, this.baseLife, null, this.gameSize, false);
    this.dataService.setGame(game);
    this.gameService.createGame(game).subscribe(
      result => {
        this.router.navigate(['PlayerCreation']);
      },
      err => { throw err; }
    );
  }

}
