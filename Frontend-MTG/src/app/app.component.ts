import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Frontend-MTG';
  public debug = true; // Turn on to show the developer nav bar
  private message: string;

  handleError(error: HttpErrorResponse | Error | any) {
    if (error instanceof HttpErrorResponse) {
      if (error.error instanceof ProgressEvent) {
        this.message = 'Error: Unable to connect to the server';
      } else {
        this.message = 'Error Code: ' + error.status + '\n' + error.error;
      }
    } else if (error instanceof Error) {
      this.message = error + '';
    } else {
      this.message = 'Something went wrong';
      console.log(error);
      console.log(error.constructor.name);
    }
    console.log(this.message);
    window.alert(this.message);
  }
}
