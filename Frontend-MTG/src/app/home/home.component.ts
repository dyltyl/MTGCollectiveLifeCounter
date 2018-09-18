import { Component, OnInit, Input } from '@angular/core';
import { GameService } from '../game.service';
import { Game } from '../game';
import { DataService } from '../data.service';

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
  constructor(public gameService: GameService, public dataService: DataService) { }
  ngOnInit() {
  }
  search() {
    this.gameService.getGame(this.query).subscribe(
      games => { this.games = games; },
      err => { console.log(err); }
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

}
