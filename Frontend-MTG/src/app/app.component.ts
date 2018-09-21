import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
/**
 * The base AppComponent
 */
export class AppComponent {
  /**
   * The title of the app
   */
  title = 'Frontend-MTG';
  /**
   * Handles whether or not the developer toolbar is displayed
   */
  public debug = true; // Turn on to show the developer nav bar
  /**
   * The most recent error message
   */
  private message: string;
  /**
   * Handles any error thrown by the app
   * @param error The error thrown
   */
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
    // Displays the error
    console.log(this.message);
    window.alert(this.message);
  }
}
