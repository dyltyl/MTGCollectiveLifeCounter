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
/**
 * The main page of the app, where life stats are displayed and modified in-game
 */
export class GameStateComponent implements OnInit {
  /**
   * The players in the game that are not the current player
   */
  public players: Player[] = [];
  /**
   * The current player in the game
   */
  public currentPlayer = this.dataService.getCurrentPlayer();
  /**
   * The currently displayed stat
   */
  public currentStat = Stats.Life;
  constructor(private gameService: GameService, private playerService: PlayerService, private dataService: DataService) { }
  /**
   * Is called when this page is initialized, calls the refresh button
   */
  ngOnInit() {
    this.refresh();
  }
  /**
   * Changes the currently displayed stat
   * @param stat The Stat to display
   */
  switchTo(stat: Stats) {
    this.currentStat = stat;
  }
  /**
   * Adds an amount to the current stat
   * @param amount The amount to add to the currentStat
   */
  addStat(amount) {
    if (this.currentStat === Stats.Life) {
      this.currentPlayer.life += amount;
    } else if (this.currentStat === Stats.Poison) {
      this.currentPlayer.poison += amount;
    } else if (this.currentStat === Stats.Experience) {
      this.currentPlayer.experience += amount;
    }
    this.playerService.updateLifeStats(this.dataService.getGame().gameId, this.currentPlayer).subscribe(
      result => {
      }, // TODO: Not actually sure if we need something here, but perhaps
      err => { throw err; }
    );
  }
  /**
   * Refreshes each player, including the current player in the game
   */
  refresh() {
    this.gameService.getPlayers(this.dataService.getGame()).subscribe(
      result => {
        this.adjustArrays(result);
      },
      err => { throw err; }
    );
  }
  /**
   * Sets the current player and the players array
   * @param data The array of players from getPlayers
   */
  adjustArrays(data: Player[]) {
    this.players = [];
    for (let i = 0; i < data.length; i++) {
      if (this.currentPlayer.email !== data[i].email) {
        this.players.push(data[i]);
      } else {
        this.currentPlayer = data[i];
      }
    }
  }
}
/**
 * The available stats to be displayed
 */
enum Stats {
  /**
   * The Life stat of the current player
   */
    Life = 0,
    /**
     * The number of Poison counters for the current player
     */
    Poison = 1,
    /**
     * The number of Experience counters for the current player
     */
    Experience = 2
}
