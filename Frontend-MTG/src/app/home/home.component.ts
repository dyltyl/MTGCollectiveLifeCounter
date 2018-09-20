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
export class HomeComponent implements OnInit {
  public searchBarVisible = false;
  public query: string;
  public placeHolderText: string;
  public games: Game[] = [];
  constructor(public gameService: GameService, public dataService: DataService, private router: Router) { }
  ngOnInit() {
  }
  search() {
    this.gameService.getGame(this.query).subscribe(
      games => { this.games = games; },
      err => { throw err; }
    );
  }
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
  joinGame(gameId: string) {
    console.log('joining: ' + gameId);
    const password = prompt('Please enter the password for the game');
    this.gameService.login(gameId, password).subscribe(
      result => {
        if ( result === 'true') {
          const game = new Game(gameId, password, -1, null, -1, false);
          this.dataService.setGame(game);
          this.router.navigate(['PlayerCreation']);
        } else {
          console.log('Password incorrect');
        }
      },
      err => { throw err; }
    );
  }

}
