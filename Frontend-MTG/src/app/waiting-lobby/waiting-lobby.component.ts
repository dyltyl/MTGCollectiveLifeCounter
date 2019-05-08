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
/**
 * The lobby before entering the game state page
 */
export class WaitingLobbyComponent implements OnInit {
  /**
   * The players that are inside of the lobby
   */
  public players: Player[] = [];
  /**
   * The current player, should be displayed at the top
   */
  public currentPlayer = this.dataService.getCurrentPlayer();
  /**
   * The game containing the players
   */
  public game: Game;
  constructor(private gameService: GameService, private playerService: PlayerService,
    private dataService: DataService, private router: Router) { }
    /**
     * Called when the page is initialized, calls the refresh function
     */
  ngOnInit() {
    console.log(this.currentPlayer);
    this.game = this.dataService.getGame();
    this.refresh();
  }
  /**
   * Refreshes the players inside of the game, and calls updateGame
   */
  refresh() {
    this.updateGame();
    this.gameService.getPlayers(this.game).subscribe(
      result => {
        this.adjustArray(result);
      },
      err => { throw err; }
    );
    if (!this.game.started) {
      setTimeout(_ => {
        this.refresh();
      }, 5000);
    } else {
      console.log('game starting');
      this.dataService.setCurrentPlayer(this.currentPlayer);
      this.router.navigate(['GameState']);
    }
  }
  /**
   * Creates the player array based on the game's max size. If there are open slots then they are filled with null
   * @param data The players obtained from calling getPlayers
   */
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
  /**
   * Updates the game based on what's in the database. Might have some problems later if there's a way to update the game's password
   */
  updateGame() {
    this.gameService.getGame(this.game.gameId).subscribe(
      result => {
        const password = this.game.gamePassword;
        this.game = result[0];
        this.game.gamePassword = password;
      },
      err => { throw err; }
    );
  }
  /**
   * Removes the given player from the lobby and game. Is tied to the boot button next to each player's name from the host's perspective
   * @param player The player to remove from the lobby
   */
  kickPlayer(player: Player) {
    const index = this.players.indexOf(player);
    this.playerService.leaveGame(player.email, this.game.gameId).subscribe(
      result => {
      },
      err => { throw err; }
    );
    this.players[index] = null;
  }
  /**
   * Adds an extra slot to the game as long as the length is less than 8.
   */
  addSlot() {
    if (this.players.length < 8) {
      this.players.push(null);
      this.game.maxSize++;
      this.gameService.updateGame(this.game).subscribe(
        result => {
        },
        err => { throw err; }
      );
    } else {
      throw new Error('Cannot add any more players');
    }
  }
  /**
   * Removes the empty slot, reducing the max size of the array.
   * @param index The index of the slot in the players array
   */
  removeSlot(index: number) {
    this.players.splice(index, 1);
    this.game.maxSize--;
    this.gameService.updateGame(this.game).subscribe(
      result => {
      },
      err => { throw err; }
    );
  }
  /**
   * Begins the game and navigates to the gameState page
   */
  startGame() {
    this.dataService.setCurrentPlayer(this.currentPlayer);
    this.gameService.startGame(this.game).subscribe(
      result => {
        this.router.navigate(['GameState']);
      },
      err => { throw err; }
    );
  }

}
