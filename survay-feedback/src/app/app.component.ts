import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService} from './services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit{
  displayUser : boolean;
  userName : string;

  constructor(
    private router: Router, private userService: UserService) { }

    ngOnInit() {
      if (localStorage.getItem("currentUser") != null) {
        this.displayUser = true;
        this.userName = JSON.parse(localStorage.getItem("currentUser")).name;
      } else {
        this.displayUser = false;
      }
    }

    logout() {
      this.userService.doLogout(JSON.parse(localStorage.getItem("currentUser")).accessToken)
      .then(response => {
        if (response.status.toString() == "SUCCESS") {
          localStorage.clear();
          this.router.navigate(['/home']);
          location.reload();
        } else {
          this.router.navigate(['/dashboard']);
        }
      });
    }
}
