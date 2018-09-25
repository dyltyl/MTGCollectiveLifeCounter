import { NgModule } from '@angular/core';
import { NativeScriptRouterModule } from 'nativescript-angular/router';
import { Routes } from '@angular/router';

import { GameCreationComponent } from './game-creation/game-creation.component';
import { PlayerCreationComponent } from './player-creation/player-creation.component';
import { WaitingLobbyComponent } from './waiting-lobby/waiting-lobby.component';
import { GameStateComponent } from './game-state/game-state.component';
import { UserTypeComponent } from './user-type/user-type.component';
import { GuestCreationComponent } from './guest-creation/guest-creation.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';

export const routes: Routes = [
  { path: 'GameCreation', component: GameCreationComponent },
  { path: 'PlayerCreation', component: PlayerCreationComponent },
  { path: 'WaitingLobby', component: WaitingLobbyComponent },
  { path: 'GameState', component: GameStateComponent },
  { path: 'UserType', component: UserTypeComponent}, // TODO: Perhaps find a better path?
  { path: 'GuestLogin', component: GuestCreationComponent },
  { path: 'Login', component: LoginComponent },
  { path: '', component: HomeComponent } // TODO: Add a default with a 404 page
];

@NgModule({
  imports: [NativeScriptRouterModule.forRoot(routes)],
  exports: [NativeScriptRouterModule]
})
export class AppRoutingModule { }
