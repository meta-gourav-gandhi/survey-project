import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router,NavigationEnd } from '@angular/router';

@Component({
  selector: 'edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
  user : any = {};
  passwords : any = {};
  infoMessage : String;
  infoMessageStatus : boolean;
  infoMessageClass : String;

  constructor(private router: Router,public _auth: AuthService, private userService: UserService){ }

  ngOnInit() {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
          return;
      }
      window.scrollTo(0, 0)
    });

    if(JSON.parse(localStorage.getItem('currentUser')) === null) {
        // will be improve when api will be complete
        this.router.navigate(['/login']);
      } else {
          this.user = JSON.parse(localStorage.getItem('currentUser'));
      }
  }

  changePassword(){
    this.infoMessageStatus = false;
      this.userService.changePassword(this.passwords , JSON.parse(localStorage.getItem("currentUser")).accessToken)
      .then(response => {
          if (response.status.toString() == "FAILURE") {
                this.infoMessageStatus = true;
                this.infoMessage = "Error while changing password";
                this.infoMessageClass = "alert alert-danger alert-dismissable";
          } else if (response.status.toString() == "DUPLICATE") {    
                this.infoMessageStatus = true;
                this.infoMessage = "New Password is same as Current Password";
                this.infoMessageClass = "alert alert-warning alert-dismissable";
          } else if (response.status.toString() == "SUCCESS") {
                this.infoMessageStatus = true;
                this.infoMessage = "Password changed successfully";
                this.infoMessageClass = "alert alert-success alert-dismissable";
          }
    });
  }
}
