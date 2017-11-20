import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedServiceService } from './services/shared-service.service';
import { UserService } from './services/user.service';

declare var $;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit{
  displayUser : boolean;
  userName : string;
  isHidden : boolean = true;
  title: string;
  constructor(private router: Router,
     private userService: UserService,
    private sharedService: SharedServiceService) { }

    ngOnInit() {
      this.sharedService.getUser().subscribe(response => this.displayUser = response);
      this.sharedService.getTitle().subscribe(response => this.title = response);
      if (localStorage.getItem("currentUser") != null) {
        this.userName = JSON.parse(localStorage.getItem("currentUser")).name;
      } else {
      }
    }

    ngDoCheck() {
      if (localStorage.getItem("currentUser") != null) {
        this.userName = JSON.parse(localStorage.getItem("currentUser")).name;
      }
    }

    logout() {
      this.userService.doLogout(JSON.parse(localStorage.getItem("currentUser")).accessToken)
      .then(response => {
        if (response.status.toString() == "SUCCESS") {
          localStorage.clear();
          this.sharedService.saveUser(false);
          this.router.navigate(['/home']);
        } else {
          this.router.navigate(['/dashboard']);
        }
      });
    }

    editProfile() {
      this.router.navigate(['/editProfile']);
    }

    changeMenuDisplay() {
      if (this.isHidden == true) {
        this.isHidden = false;
      } else {
        this.isHidden = true;
      }
    }
}
