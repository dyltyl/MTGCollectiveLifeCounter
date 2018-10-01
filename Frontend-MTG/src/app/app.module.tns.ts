import { NgModule, NO_ERRORS_SCHEMA, ErrorHandler } from '@angular/core';
import { NativeScriptModule } from 'nativescript-angular/nativescript.module';

import { AppRoutingModule } from './app-routing.module.tns';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { GameCreationComponent } from './game-creation/game-creation.component';
import { PlayerCreationComponent } from './player-creation/player-creation.component';
import { WaitingLobbyComponent } from './waiting-lobby/waiting-lobby.component';
import { GameStateComponent } from './game-state/game-state.component';
import { UserTypeComponent } from './user-type/user-type.component';
import { LoginComponent } from './login/login.component';
import { GuestCreationComponent } from './guest-creation/guest-creation.component';


// Uncomment and add to NgModule imports if you need to use two-way binding
// import { NativeScriptFormsModule } from 'nativescript-angular/forms';

// Uncomment and add to NgModule imports  if you need to use the HTTP wrapper
import { NativeScriptHttpClientModule } from 'nativescript-angular/http-client';
import { NativeScriptFormsModule } from 'nativescript-angular/forms';
import { DataService } from './data.service';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    GameCreationComponent,
    PlayerCreationComponent,
    WaitingLobbyComponent,
    GameStateComponent,
    UserTypeComponent,
    LoginComponent,
    GuestCreationComponent
    ],
  imports: [
    NativeScriptModule,
    AppRoutingModule,
    NativeScriptHttpClientModule,
    NativeScriptFormsModule
  ],
  providers: [DataService], // Says to use AppComponent to handle errors in the app
  bootstrap: [AppComponent]
})
/*
Pass your application module to the bootstrapModule function located in main.ts to start your app
*/
export class AppModule { }
