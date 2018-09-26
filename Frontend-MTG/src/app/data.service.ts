import { Injectable } from '@angular/core';
import { Game } from './game';
import { Player } from './player';

@Injectable()
/**
 * A service used to easily access local storage and to share information between components
 */
export class DataService {
  private host: boolean;
  private myGame: Game;
  private myPlayer: Player;
  /**
   * Sets the isHost value in local storage
   * @param isHost True if the current player is the host
   */
  public setIsHost(isHost: boolean) {
    if (localStorage !== undefined) {
      localStorage.setItem('isHost', JSON.stringify(isHost));
    } else {
      this.host = isHost;
    }
  }
  /**
   * Retrieves the isHost value from local storage
   */
  public isHost(): boolean {
    if (localStorage !== undefined) {
      return localStorage.getItem('isHost') === 'true';
    } else {
      return this.host;
    }
  }
  /**
   * Sets the current game in local storage
   * @param game The current game
   */
  public setGame(game: Game) {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem('game', JSON.stringify(game));
    } else {
      this.myGame = game;
    }
  }
  /**
   * Retrieves the current game from local storage
   */
  public getGame(): Game {
    if (localStorage !== undefined) {
      return JSON.parse(localStorage.getItem('game'));
    } else {
      return this.myGame;
    }
  }
  /**
   * Sets the current player in local storage
   * @param player The current player
   */
  public setCurrentPlayer(player: Player) {
    if (localStorage !== undefined) {
      localStorage.setItem('currentPlayer', JSON.stringify(player));
    } else {
      this.myPlayer = player;
    }
  }
  /**
   * Retrieves the current player from local storage
   */
  public getCurrentPlayer(): Player {
    if (localStorage !== undefined) {
      return JSON.parse(localStorage.getItem('currentPlayer'));
    } else {
      return this.myPlayer;
    }
  }
}
