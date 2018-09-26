import { Component, OnInit } from '@angular/core';
import { GameService } from '../game.service';
import { Game } from '../game';
import { DataService } from '../data.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
/**
 * The home page of the app
 */
export class HomeComponent implements OnInit {
  /**
   * When true, the search bar is displayed
   */
  public searchBarVisible = false;
  /**
   * The value inside of the search bar
   */
  public query: string;
  /**
   * The placeholder text inside of the search bar
   */
  public placeHolderText: string;
  /**
   * The resulting Games from the search
   */
  public games: Game[] = [];

  constructor(public gameService: GameService, public dataService: DataService, private router: Router) { }
  ngOnInit() {
  }
  /**
   * Queries the database for games similar to the value in the search bar
   */
  search() {
    this.gameService.getGame(this.query).subscribe(
      games => { this.games = games; },
      err => { throw err; }
    );
  }
  /**
   * Shows the search bar and sets the placeholder text depending on if the user is joining or rejoining
   * @param joinGame True if the user is joining the game, not rejoining
   */
  openSearchBar(joinGame: boolean) {
    if (!this.searchBarVisible) {
      this.searchBarVisible = true;
    } else {
      this.games = [];
    }
    if (joinGame) {
      this.placeHolderText = 'WaitingLobbyID';
    } else {
      this.placeHolderText = 'PreviousLobbyID';
    }
  }
  /**
   * Prompts the user for the password to the game, if it is correct then it navigates to the Player Creation page
   * @param gameId The id of the Game to join
   */
  joinGame(gameId: string) {
    console.log('joining: ' + gameId);
    const password = prompt('Please enter the password for the game');
    this.gameService.login(gameId, password).subscribe(
      result => {
        if ( result === true) {
          const game = new Game(gameId, password, -1, null, -1, false);
          this.dataService.setGame(game);
          this.router.navigate(['PlayerCreation']);
        } else {
          throw new Error('Password incorrect');
        }
      },
      err => { throw err; }
    );
  }

}