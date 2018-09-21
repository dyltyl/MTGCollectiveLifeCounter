import { Component, OnInit } from '@angular/core';
import { PlayerService } from '../player.service';
import { GameService } from '../game.service';
import { DataService } from '../data.service';
import { Player } from '../player';
import { Game } from '../game';
import { Router } from '@angular/router';

@Component({
  selector: 'app-player-creation',
  templateUrl: './player-creation.component.html',
  styleUrls: ['./player-creation.component.css']
})
/**
 * The player creation page
 */
export class PlayerCreationComponent implements OnInit {
  /**
   * The name of the player, cannot be left blank
   */
  public playerName = '';
  /**
   * The name of the commander, can be left blank if not playing commander
   */
  public commanderAName = ''; // TODO: Have different game types, don't prompt this if not playing commander
  /**
   * The name of the partner commander, if there is one
   */
  public commanderBName = '';
  /**
   * The email of the player, cannot be left blank
   */
  public email = '';
  /**
   * The password for the player, can be left blank
   */
  public password = '';
  /**
   * True if the player has a partner commander
   */
  public hasPartner = false;

  constructor(private playerService: PlayerService, private gameService: GameService,
    private dataService: DataService, private router: Router) { }

  ngOnInit() {
  }
  /**
   * Creates the player in the database and then calls joinGame if it was successful.
   * Also sets the host of the game if this player created it
   */
  createPlayer() {
    if (this.playerName.trim().length < 1) {
      throw new Error('Player name must be set');
    }
    if (this.email.trim().length < 1) {
      throw new Error('Player email must be set');
    }
    const game = this.dataService.getGame();
    if (game === null || game.gameId === null) {
      throw new Error('The game must be set');
    }
    const commanders: string[] = [];
    if (this.commanderAName.trim().length > 0) {
      commanders.push(this.commanderAName);
    }
    if (this.hasPartner && this.commanderBName.trim().length > 0) {
      commanders.push(this.commanderBName);
    }
    const player = new Player(this.playerName, this.email, this.password, -1);
    this.dataService.setCurrentPlayer(player);
    this.playerService.createPlayer(player).subscribe(
      result => {
        this.joinGame(player, game, commanders);
      },
      err => { throw err; }
    );
  }
  /**
   * Puts the player inside of the game. If it is successful then it navigates to WaitingLobby.
   * If this step fails then the newly created player is deleted so that the user can try again.
   * @param player The player to put in the game, should be the newly created player
   * @param game The game to put the player in
   * @param commanders The commanders of the player if there are any. Pass in an empty array if there are none
   */
  joinGame(player: Player, game: Game, commanders: string[]) { // TODO I think there are still some bugs involved with joining games
    this.playerService.joinGame(player, game, commanders).subscribe(
      result => {
        if (this.dataService.isHost()) {
          game.host = player.email;
          this.dataService.setGame(game);
          this.gameService.updateHost(game).subscribe(
            res => {
            },
            err => { throw err; }
          );
        }
        this.router.navigate(['WaitingLobby']);
      },
      err => {
        this.deletePlayer(player);
        throw err;
      }
    );
  }
  /**
   * Deletes the player from the database, called if joinGame fails
   * @param player The player to delete
   */
  deletePlayer(player: Player) {
    this.playerService.deletePlayer(player.email, player.password).subscribe(
      result => {
        console.log('Deleted: ' + player.email);
      },
      err => { throw err; }
    );
  }

}
