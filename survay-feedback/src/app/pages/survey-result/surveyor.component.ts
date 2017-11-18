import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router,NavigationEnd } from '@angular/router';

@Component({
  selector: 'surveyor-content',
  templateUrl: './surveyor.component.html',
  styleUrls: ['./surveyor.component.css']
})
export class SurveyorComponent implements OnInit {
  user : any = {};

  constructor(private router: Router,public _auth: AuthService, private userService: UserService){ 
    if (JSON.parse(localStorage.getItem('currentUser')) === null) {
      this.router.navigate(['/login']);
    } else {
        this.user = JSON.parse(localStorage.getItem('currentUser'));
    }
  }

  ngOnInit() {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
          return;
      }
      window.scrollTo(0, 0)
    });
  }
}
