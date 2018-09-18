import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { GameCreationComponent } from './game-creation/game-creation.component';
import { PlayerCreationComponent } from './player-creation/player-creation.component';
import { WaitingLobbyComponent } from './waiting-lobby/waiting-lobby.component';
import { GameStateComponent } from './game-state/game-state.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    GameCreationComponent,
    PlayerCreationComponent,
    WaitingLobbyComponent,
    GameStateComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
