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
/**
 * The component used to create a Game
 */
export class GameCreationComponent implements OnInit {
  /**
   * The maximum number of players allowed inside of the Game. Value should be between 2 and 8.
   */
  public gameSize = 2;
  /**
   * The amount of life that each player starts with
   */
  public baseLife = 40;
  /**
   * The name of the Game being created
   */
  public gameName = '';
  /**
   * The password to enter the game. (Can be empty)
   */
  public gamePassword = '';
  constructor(public gameService: GameService, public dataService: DataService, private router: Router) { }

  ngOnInit() {
  }
  /**
   * Creates the game and then navigates to PlayerCreation
   */
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
