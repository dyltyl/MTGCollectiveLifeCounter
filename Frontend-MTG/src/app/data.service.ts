import { Injectable } from '@angular/core';
import { Game } from './game';
import { Player } from './player';

@Injectable()
// This service is for sharing data between components
export class DataService {

  public setIsHost(isHost: boolean) {
    localStorage.setItem('isHost', JSON.stringify(isHost));
  }
  public isHost(): boolean {
    return localStorage.getItem('isHost') === 'true';
  }
  public setGame(game: Game) {
    localStorage.setItem('game', JSON.stringify(game));
  }
  public getGame(): Game {
    return JSON.parse(localStorage.getItem('game'));
  }
  public setCurrentPlayer(player: Player) {
    localStorage.setItem('currentPlayer', JSON.stringify(player));
  }
  public getCurrentPlayer(): Player {
    return JSON.parse(localStorage.getItem('currentPlayer'));
  }
}
