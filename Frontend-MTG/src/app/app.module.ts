import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { GameCreationComponent } from './game-creation/game-creation.component';
import { PlayerCreationComponent } from './player-creation/player-creation.component';
import { WaitingLobbyComponent } from './waiting-lobby/waiting-lobby.component';
import { GameStateComponent } from './game-state/game-state.component';

const appRoutes: Routes = [
  { path: 'GameCreation', component: GameCreationComponent },
  { path: 'PlayerCreation', component: PlayerCreationComponent },
  { path: 'WaitingLobby', component: WaitingLobbyComponent },
  { path: 'GameState', component: GameStateComponent },
  { path: '', component: HomeComponent }
];

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
    BrowserModule,
    RouterModule.forRoot(appRoutes, {enableTracing: true})
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
