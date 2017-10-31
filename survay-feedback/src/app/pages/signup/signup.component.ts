import { Component, OnInit } from '@angular/core';
import { UserService} from '../../services/user.service';
import { AlertService } from '../../services/alert.service';
import { Location }                 from '@angular/common';
import { Router } from '@angular/router';
import { Message } from '../../models/message';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  user : any = {};
  message : Message;
  regMessageStatus : boolean;
  regMessageClass : string;
  regMessage : string;

  constructor(private userService: UserService,
    private router: Router,private location: Location, private alertService: AlertService,) {
     }

  ngOnInit() {
    this.regMessageStatus = false;
  }

  doSignup() : void {
    this.regMessageStatus = false;
    this.userService.doSignup(this.user)
      .then(message => {
        this.message = message;
        if (this.message.toString() === "FAILURE") {
          this.regMessageStatus = true;
          this.regMessageClass = "alert alert-danger alert-dismissable";
          this.regMessage = "Some Error Occures";
        } else if (this.message.toString() === "SUCCESS") {
          this.regMessageStatus = true;
          this.regMessageClass = "alert alert-success alert-dismissable";
          this.regMessage = "Successfully Signup";
        } else if (this.message.toString() === "DUPLICATE") {
          this.regMessageStatus = true;
          this.regMessageClass = "alert alert-warning alert-dismissable";
          this.regMessage = "Email  already Exists";
        }
      });
  }

}
