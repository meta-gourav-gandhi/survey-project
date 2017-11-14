import { Component, OnInit } from '@angular/core';
import { UserService } from "../../../services/user.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../../models/message';
import { User } from '../../../models/user';
import { UserDetail } from '../../../models/user-detail';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'create-surveyor',
  templateUrl: './create-surveyor.component.html',
  styleUrls: ['./create-surveyor.component.css']
})
export class CreateSurveyorComponent implements OnInit {
  user : any = {};
  allUsers : UserDetail[];
  p: number = 1;
  errorMessageStatus : boolean = false;
  errorMessage : string;

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
    
      this.getAllUsers();
  }

  /** function get all users from user service */
  getAllUsers() {
    this.userService.getAllUsers(JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => { 
        if (response.status.toString() == "ACCESS_GRANTED") {
            this.allUsers = response.body;
        } else {
            this.router.navigate(['/home']);
        }
     });
  }

  /** function creates a surveyor if not already a survyor, if already a server then remove the surveyor role */
  surveyorCheck(user : UserDetail){
    this.userService.surveyorCheck(user.id , JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => { 
        if (response.status.toString() == "SUCCESS") {

        } else {
            this.errorMessageStatus = true;
            this.errorMessage = "Error in Changeing Survey Status";
            if (document.getElementById("uid"+user.id).classList.contains("active")) {
                document.getElementById("uid"+user.id).classList.remove('active');
            } else {
                document.getElementById("uid"+user.id).classList.add('active');
            }
        }
     });
  }
}
