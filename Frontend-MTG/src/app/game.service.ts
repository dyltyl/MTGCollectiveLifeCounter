import { Injectable } from '@angular/core';
import { WebService } from './web.service';
import { Game } from './game';
import { Observable } from 'rxjs';
import { Player } from './player';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GameService {
  public game: Game;
  constructor(private web: WebService) { }
  getCommanders(): Observable<string[][]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': this.game.gameId,
        'gamePassword': this.game.gamePassword
      })
    };
    return this.web.http.get<string[][]>(this.web.baseSite + 'commanders', httpOptions);
  }
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
  getGame(gameId: string): Observable<Game[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId
      })
    };
    return this.web.http.get<Game[]>(this.web.baseSite + 'game', httpOptions);
  }
  startGame(): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': this.game.gameId,
        'gamePassword': this.game.gamePassword
      })
    };
    return this.web.http.get<string>(this.web.baseSite + 'startGame', httpOptions);
  }
  hasGameStarted(): Observable<boolean> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': this.game.gameId,
        'gamePassword': this.game.gamePassword
      })
    };
    return this.web.http.get<boolean>(this.web.baseSite + 'hasGameStarted', httpOptions);
  }
  login(gameId: string, gamePassword: string): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'gamePassword': gamePassword
      })
    };
    return this.web.http.get<string>(this.web.baseSite + 'verifyGame', httpOptions);
  }
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
  deleteGame(): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': this.game.gameId,
        'gamePassword': this.game.gamePassword
      })
    };
    return this.web.http.delete<string>(this.web.baseSite + 'game', httpOptions);
  }

}
