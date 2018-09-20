import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Frontend-MTG';
  public debug = true; // Turn on to show the developer nav bar
  private message: string;
  handleError(error: any) {
    console.log(error.constructor.name);
    if (error.status === 0) {
      this.message = 'Error: Unable to connect to the server';
    } else if (typeof error.status !== 'undefined') {
      this.message = 'Error Code: ' + error.status + '\n';
      if (error.error.error) {
        this.message += error.error.error;
      } else {
        this.message += error.error;
      }
    } else {
      this.message = error + '';
    }
    console.log(this.message);
    window.alert(this.message);
  }
}
