import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router,NavigationEnd } from '@angular/router';

@Component({
  selector: 'forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  user : any = {};
  passwords : any = {};
  infoMessage : String;
  infoMessageStatus : boolean;
  infoMessageClass : String;
  loadingData : boolean;

  constructor(private router: Router,public _auth: AuthService, private userService: UserService){ }

  ngOnInit() {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
          return;
      }
      window.scrollTo(0, 0)
    });
  }

  recoverPassword(email : string) {
    this.loadingData = true;
    this.infoMessageStatus = false;
    this.userService.recoverPassword(email)
    .then(response => {
      this.loadingData = false;
      if (response.status.toString() == "SUCCESS") {
          this.infoMessageStatus = true;
          this.infoMessage = "Email Sent to mail id " + email ;
          this.infoMessageClass = "alert alert-success alert-dismissable";
      } else {
          this.infoMessageStatus = true;
          this.infoMessage = "Email not exists in System Database";
          this.infoMessageClass = "alert alert-danger alert-dismissable";
      }
    });
  }
}
