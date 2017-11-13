import { Component, OnInit } from '@angular/core';
import { AuthService } from "angular2-social-login";
import { UserService } from "../../services/user.service";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { NgSwitch } from '@angular/common';

@Component({
  selector: 'side-navbar',
  templateUrl: './side-navbar.component.html',
  styleUrls: ['./side-navbar.component.css']
})

export class SideNavbarComponent implements OnInit {
  user : User;
  sub: any;
  message : Message;
  currentLoggedInUser : User;

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
}
