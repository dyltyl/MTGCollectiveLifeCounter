import { Injectable } from '@angular/core';
import { WebService } from './web.service';
import { Player } from './player';
import { HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommanderDamage } from './commanderDamage';
import { Game } from './game';
import { Router } from '@angular/router';
import { DataService } from './data.service';
import { GameService } from './game.service';

@Injectable({
  providedIn: 'root'
})
/**
 * A service used to access api methods involved in the Player table
 */
export class PlayerService {
  constructor(private web: WebService, private router: Router, private dataService: DataService, private gameService: GameService) { }
  /**
   * Gets the Player in the Game, with life stats
   * @param gameId The id of the game
   * @param email The email of the game
   */
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
  /**
   * Gets the Commander damage object of the player, with enemyPlayer and commander
   * @param gameId The id of the game
   * @param email The email of the current player
   * @param enemyPlayer The email of the enemy player
   * @param commander The name of the enemyCommander
   */
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
  /**
   * Returns an array containing all of the games the player is in
   * @param email The email of the player
   */
  getGamesPlayerIsIn(email: string): Observable<Game[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'email': email
      })
    };
    return this.web.http.get<Game[]>(this.web.baseSite + 'gamesPlayerIsIn', httpOptions);
  }
  /**
   * Verifies the email and password with the values in the database
   * @param email The email of the player
   * @param password The password of the player
   */
  login(email: string, password: string): Observable<boolean> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'email': email,
        'password': password
      })
    };
    return this.web.http.get<boolean>(this.web.baseSite + 'login', httpOptions);
  }
  /**
   * Puts the player inside of the game
   * @param player The player to insert into the game
   * @param game The game to put the player in
   * @param commanders The commanders of the player, an empty array if they have none
   */
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
  /**
   * Creates the player in the database
   * @param player The player to create
   */
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
  /**
   * Creates a guest user in the database, and returns the email
   * @param name The name of the guest
   */
  createGuest(name: string): Observable<Player> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8'
      })
    };
    return this.web.http.post<Player>(this.web.baseSite + 'player/' + name, null, httpOptions);
  }
  /**
   * Updates the player's email/password in the database
   * @param originalEmail The original email of the player
   * @param email The new email of the player
   * @param password The new password of the player
   */
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
  /**
   * Updates the life stats of the player in the database
   * @param gameId The gameId of the current game
   * @param player The Player with the updated stats
   */
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
  /**
   * Updates the Life total of the player in the game
   * @param gameId The id of the game
   * @param gamePassword The password of the game
   * @param email The email of the player
   * @param password The password of the player
   * @param amount The new life total
   */
  updateLifeTotal(gameId: string, gamePassword: string, email: string, password: string, amount: number): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'gamePassword': gamePassword,
        'email': email,
        'password': password
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.put(this.web.baseSite + 'life/' + amount, null, httpOptions);
  }
  /**
   * Updates the number of poison counters of the player in the game
   * @param gameId The id of the game
   * @param gamePassword The password of the game
   * @param email The email of the player
   * @param password The password of the player
   * @param amount The new number of poison counters
   */
  updatePoison(gameId: string, gamePassword: string, email: string, password: string, amount: number): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'gamePassword': gamePassword,
        'email': email,
        'password': password
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.put(this.web.baseSite + 'poison/' + amount, null, httpOptions);
  }
  /**
   * Updates the number of experience counters of the player in the game
   * @param gameId The id of the game
   * @param gamePassword The password of the game
   * @param email The email of the player
   * @param password The password of the player
   * @param amount The new number of experience counters
   */
  updateExperience(gameId: string, gamePassword: string, email: string, password: string, amount: number): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'gamePassword': gamePassword,
        'email': email,
        'password': password
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.put(this.web.baseSite + 'experience/' + amount, null, httpOptions);
  }
  /**
   * Updates the commander damage of the player in the game
   * @param gameId The id of the game
   * @param gamePassword The password of the game
   * @param email The email of the player
   * @param password The password of the player
   * @param commanderDamage The commander damage object to put in the database
   */
  updateCommanderDamage(gameId: string, gamePassword: string, email: string, password: string, commanderDamage: CommanderDamage):
   Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json; charset=UTF-8',
        'gameId': gameId,
        'gamePassword': gamePassword,
        'email': email,
        'password': password
      }),
      responseType: 'text' as 'text'
    };
    return this.web.http.put(this.web.baseSite + 'commanderDamage', commanderDamage, httpOptions);
  }
  /**
   * Deletes the player from the database
   * @param email The email of the player
   * @param password The password of the player
   */
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
  /**
   * Removes the player from the game
   * @param email The email of the player
   * @param gameId  The id of the game
   */
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

  /**
   * Puts the player inside of the game. If it is successful then it navigates to WaitingLobby.
   * If this step fails then the newly created player is deleted so that the user can try again.
   * @param player The player to put in the game, should be the newly created player
   * @param game The game to put the player in
   * @param commanders The commanders of the player if there are any. Pass in an empty array if there are none
   */
  putInGame(player: Player, game: Game, commanders: string[]) { // TODO I think there are still some bugs involved with joining games
    this.joinGame(player, game, commanders).subscribe(
      result => {
        if (this.dataService.isHost()) {
          game.host = player.email;
          this.dataService.setGame(game);
          this.gameService.updateHost(game).subscribe(
            res => {
            },
            err => {
              if (err.status !== 200) { // Problem with the NativeScript httpClient with returning a string
                throw err;
              }
            }
          );
        }
        this.router.navigate(['WaitingLobby']);
      },
      err => {
        if (err.status !== 200) {
          // Delete player if join fails
          this.deletePlayer(player.email, player.password).subscribe(
            result2 => {
              console.log('Deleted: ' + player.email);
            },
            err2 => {
              if (err.status !== 200) { // Problem with the NativeScript httpClient with returning a string
                throw err;
              }
            }
          );
          throw err;
        } else {
          if (this.dataService.isHost()) {
            game.host = player.email;
            this.dataService.setGame(game);
            this.gameService.updateHost(game).subscribe(
              res => {
              },
              err2 => {
                if (err2.status !== 200) { // Problem with the NativeScript httpClient with returning a string
                  throw err2;
                }
              }
            );
          }
          this.router.navigate(['WaitingLobby']);
        }
      }
    );
  }
}
