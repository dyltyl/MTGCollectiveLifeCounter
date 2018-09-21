import { Injectable } from '@angular/core';
import { Game } from './game';
import { Player } from './player';

@Injectable()
/**
 * A service used to easily access local storage and to share information between components
 */
export class DataService {
  /**
   * Sets the isHost value in local storage
   * @param isHost True if the current player is the host
   */
  public setIsHost(isHost: boolean) {
    localStorage.setItem('isHost', JSON.stringify(isHost));
  }
  /**
   * Retrieves the isHost value from local storage
   */
  public isHost(): boolean {
    return localStorage.getItem('isHost') === 'true';
  }
  /**
   * Sets the current game in local storage
   * @param game The current game
   */
  public setGame(game: Game) {
    localStorage.setItem('game', JSON.stringify(game));
  }
  /**
   * Retrieves the current game from local storage
   */
  public getGame(): Game {
    return JSON.parse(localStorage.getItem('game'));
  }
  /**
   * Sets the current player in local storage
   * @param player The current player
   */
  public setCurrentPlayer(player: Player) {
    localStorage.setItem('currentPlayer', JSON.stringify(player));
  }
  /**
   * Retrieves the current player from local storage
   */
  public getCurrentPlayer(): Player {
    return JSON.parse(localStorage.getItem('currentPlayer'));
  }
}
