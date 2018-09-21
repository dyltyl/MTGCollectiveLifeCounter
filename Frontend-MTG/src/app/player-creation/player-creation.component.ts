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
        this.playerService.putInGame(player, game, commanders);
      },
      err => { throw err; }
    );
  }
}
