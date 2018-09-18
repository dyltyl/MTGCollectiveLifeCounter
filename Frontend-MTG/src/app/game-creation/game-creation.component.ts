import { Component, OnInit } from '@angular/core';
import { GameService } from '../game.service';
import { PlayerService } from '../player.service';
import { DataService } from '../data.service';

@Component({
  selector: 'app-game-creation',
  templateUrl: './game-creation.component.html',
  styleUrls: ['./game-creation.component.css']
})
export class GameCreationComponent implements OnInit {

  constructor(public gameService: GameService, public dataService: DataService) { }

  ngOnInit() {
    console.log(this.dataService.isHost());
  }

}
