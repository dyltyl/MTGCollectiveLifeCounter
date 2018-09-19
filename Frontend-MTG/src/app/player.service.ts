import { Injectable } from '@angular/core';
import { WebService } from './web.service';
import { Player } from './player';
import { HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommanderDamage } from './commanderDamage';
import { Game } from './game';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  constructor(private web: WebService) { }
  getPlayer(gameId: string, email: string): Observable<Player> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'email': email
      })
    };
    return this.web.http.get<Player>(this.web.baseSite + 'player', httpOptions);
  }
  getCommanderDamage(gameId: string, email: string, enemyPlayer: string, commander: string): Observable<CommanderDamage> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'email': email,
        'enemyPlayer': enemyPlayer
      })
    };
    return this.web.http.get<CommanderDamage>(this.web.baseSite + 'commanderDamage/' + commander, httpOptions);
  }
  getGamesPlayerIsIn(email: string): Observable<Game[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'email': email
      })
    };
    return this.web.http.get<Game[]>(this.web.baseSite + 'gamesPlayerIsIn', httpOptions);
  }
  login(email: string, password: string): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'email': email,
        'password': password
      })
    };
    return this.web.http.get<string>(this.web.baseSite + 'login', httpOptions);
  }
  joinGame(player: Player, game: Game, commanders: string[]): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'email': player.email,
        'password': player.password,
        'gameId': game.gameId,
        'gamePassword': game.gamePassword
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.post(this.web.baseSite + 'joinGame', commanders, httpOptions);
  }
  createPlayer(player: Player): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'email': player.email
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.post(this.web.baseSite + 'player', player, httpOptions);
  }
  updatePlayer(originalEmail: string, email: string, password: string): Observable<Player> {
    const player = new Player(name, email, password, 40);
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'email': originalEmail
      })
    };
    return this.web.http.put<Player>(this.web.baseSite + 'player', player, httpOptions);
  }
  updateLifeStats(gameId: string, player: Player): Observable<Player> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'email': player.email
      })
    };
    return this.web.http.post<Player>(this.web.baseSite + 'life', player, httpOptions);
  }
  updateLifeTotal(gameId: string, gamePassword: string, email: string, password: string, amount: number): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'gamePassword': gamePassword,
        'email': email,
        'password': password
      })
    };
    return this.web.http.put<string>(this.web.baseSite + 'life/' + amount, null, httpOptions);
  }
  updatePoison(gameId: string, gamePassword: string, email: string, password: string, amount: number): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'gamePassword': gamePassword,
        'email': email,
        'password': password
      })
    };
    return this.web.http.put<string>(this.web.baseSite + 'poison/' + amount, null, httpOptions);
  }
  updateExperience(gameId: string, gamePassword: string, email: string, password: string, amount: number): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'gamePassword': gamePassword,
        'email': email,
        'password': password
      })
    };
    return this.web.http.put<string>(this.web.baseSite + 'experience/' + amount, null, httpOptions);
  }
  updateCommanderDamage(gameId: string, gamePassword: string, email: string, password: string, commanderDamage: CommanderDamage):
   Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'gamePassword': gamePassword,
        'email': email,
        'password': password
      })
    };
    return this.web.http.put<string>(this.web.baseSite + 'commanderDamage', commanderDamage, httpOptions);
  }
  deletePlayer(email: string, password: string): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'email': email,
        'password': password
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.delete(this.web.baseSite + 'player', httpOptions);
  }
  leaveGame(email: string, gameId: string): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'email': email,
        'gameId': gameId
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.delete(this.web.baseSite + 'leaveGame', httpOptions);
  }
}
