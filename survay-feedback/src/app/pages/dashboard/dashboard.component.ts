import { Component, OnInit } from '@angular/core';
import { AuthService } from "angular2-social-login";
import { UserService } from "../../services/user.service";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router } from '@angular/router';

@Component({
  selector: 'dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {
  user : User;
  sub: any;
  message : Message;
  currentLoggedInUser : User;

  constructor(private router: Router,public _auth: AuthService, private userService: UserService){ }

  ngOnInit() {
      //localStorage.clear();
    if(JSON.parse(localStorage.getItem('currentUser')) === null) {
        // will be improve when api will be complete
        this.router.navigate(['/login']);
      } else {
          this.user = JSON.parse(localStorage.getItem('currentUser'));
      }
  }

  logout() {
      alert("You will be logout soon");
  }

//   logout(){
//       if (localStorage.getItem("provider"))
//     this._auth.logout().subscribe(
//       (data)=>{console.log(data);this.user=null;}
//     )
//   }

  /*ngOnDestroy(){
    this.sub.unsubscribe();
  }*/

}
