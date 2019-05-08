import { Injectable } from '@angular/core';
import { WebService } from './web.service';
import { Game } from './game';
import { Observable } from 'rxjs';
import { Player } from './player';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
/**
 * A service used to access api methods involved in the Game table
 */
export class GameService {
  constructor(private web: WebService) { }
  /**
   * Gets the commanders in the game with their associated player
   */
  getCommanders(game: Game): Observable<string[][]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': game.gameId,
        'gamePassword': game.gamePassword
      })
    };
    return this.web.http.get<string[][]>(this.web.baseSite + 'commanders', httpOptions);
  }
  /**
   * Gets all of the players with their life stats in the game
   * @param game The current game
   */
  getPlayers(game: Game): Observable<Player[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': game.gameId,
        'gamePassword': game.gamePassword
      })
    };
    return this.web.http.get<Player[]>(this.web.baseSite + 'players', httpOptions);
  }
  /**
   * Searches for a game and returns the results
   * @param gameId The gameId to search for
   */
  getGame(gameId: string): Observable<Game[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId
      })
    };
    return this.web.http.get<Game[]>(this.web.baseSite + 'game', httpOptions);
  }
  /**
   * Starts the current game
   * @param game The game to start
   */
  startGame(game: Game): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': game.gameId,
        'gamePassword': game.gamePassword
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.get(this.web.baseSite + 'startGame', httpOptions);
  }
  /**
   * Checks if the game has started
   * @param game The game to check
   */
  hasGameStarted(game: Game): Observable<boolean> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': game.gameId,
        'gamePassword': game.gamePassword
      })
    };
    return this.web.http.get<boolean>(this.web.baseSite + 'hasGameStarted', httpOptions);
  }
  /**
   * Checks the login credentials to the game
   * @param gameId The gameId to login to
   * @param gamePassword The password to enter the game
   */
  login(gameId: string, gamePassword: string): Observable<boolean> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'gamePassword': gamePassword
      })
    };
    return this.web.http.get<boolean>(this.web.baseSite + 'verifyGame', httpOptions);
  }
  /**
   * Creates the game in the database
   * @param game The game to create
   */
  createGame(game: Game): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': game.gameId,
        'gamePassword': game.gamePassword
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.post(this.web.baseSite + 'game', game, httpOptions);
  }
  /**
   * Updates the game in the database with the provided game
   * @param game The updated game
   */
  updateGame(game: Game):
  Observable<Game> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': game.gameId,
        'gamePassword': game.gamePassword
      })
    };
    return this.web.http.put<Game>(this.web.baseSite + 'game', game, httpOptions);
  }
  /**
   * Updates the host of the game, based on the provided game's host value
   * @param game The game to update the host on
   */
  updateHost(game: Game): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': game.gameId,
        'hostEmail': game.host
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.put(this.web.baseSite + 'host', null, httpOptions);
  }
  /**
   * Deletes the game from the database
   */
  deleteGame(game: Game): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': game.gameId,
        'gamePassword': game.gamePassword
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.delete(this.web.baseSite + 'game', httpOptions);
  }

}
