import { Component, OnInit } from '@angular/core';
import { AuthService } from "angular2-social-login";
import { UserService } from "../../services/user.service";
import { SharedServiceService } from "../../services/shared-service.service";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { NgSwitch } from '@angular/common';

@Component({
  selector: 'dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {
  user : User;
  sub: any;
  currentLoggedInUser : User;
  sidebar : boolean = false;

  constructor(private router: Router,
    public _auth: AuthService, 
    private userService: UserService, 
    private sharedService: SharedServiceService){ 
    if(JSON.parse(localStorage.getItem('currentUser')) === null) {
      // will be improve when api will be complete
      this.router.navigate(['/login']);
    } else {
        this.user = JSON.parse(localStorage.getItem('currentUser'));
    }
  }

  ngOnInit() {
      setTimeout(() => {
        this.sharedService.saveTitle('Dashboard');
      });
      this.router.events.subscribe((evt) => {
        if (!(evt instanceof NavigationEnd)) {
            return;
        }
        window.scrollTo(0, 0)
      });
  }

  ngAfterViewInit() {
  }
}
