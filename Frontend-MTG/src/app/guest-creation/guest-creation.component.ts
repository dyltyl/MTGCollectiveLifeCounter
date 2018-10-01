import { Component, OnInit } from '@angular/core';
import { PlayerService } from '../player.service';
import { DataService } from '../data.service';
import { Player } from '../player';
import { Game } from '../game';
import { GameService } from '../game.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-guest-creation',
  templateUrl: './guest-creation.component.html',
  styleUrls: ['./guest-creation.component.css']
})
export class GuestCreationComponent implements OnInit {
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
   * True if the player has a partner commander
   */
  public hasPartner = false;
  constructor(private playerService: PlayerService, private gameService: GameService, private dataService: DataService,
    private router: Router) { }

  ngOnInit() {
  }
  createGuest() {
    this.playerService.createGuest(this.playerName).subscribe(
      result => {
        const player = new Player(this.playerName, result, '', -1);
        const game = this.dataService.getGame();
        const commanders: string[] = [];
        if (this.commanderAName.trim().length > 0) {
          commanders.push(this.commanderAName);
        }
        if (this.hasPartner && this.commanderBName.trim().length > 0) {
          commanders.push(this.commanderBName);
        }
        this.playerService.putInGame(player, game, commanders);
      },
      err => {
        throw err;
      }
    );
  }

}
