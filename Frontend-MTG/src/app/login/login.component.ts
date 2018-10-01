import { Component, OnInit } from '@angular/core';
import { PlayerService } from '../player.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public email = '';
  public password = '';
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
  constructor(private playerService: PlayerService) { }

  ngOnInit() {
  }
  login() {
    this.playerService.login(this.email, this.password).subscribe(
      result => {
        console.log(result);
        if (result) {

        }
      },
      err => {
        throw err;
      }
    );
  }

}
