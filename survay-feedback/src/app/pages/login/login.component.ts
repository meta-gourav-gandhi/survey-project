import { Component, OnInit } from '@angular/core';
import { AuthService } from "angular2-social-login";
import { UserService } from "../../services/user.service";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router } from '@angular/router';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  error : boolean;
  user : any = {};
  sub: any;
  message : Message;
  currentLoggedInUser : User;

  constructor(private router: Router,public _auth: AuthService, private userService: UserService){ }

  ngOnInit() {
    this.error = false; 
  }

  doLogin() : void {
    this.error = false;
    this.userService.doLogin(this.user)
    .then(user => {
      this.currentLoggedInUser = user;
      console.log(this.currentLoggedInUser);
      if (this.currentLoggedInUser.status == 200) {
        localStorage.setItem("currentUser",JSON.stringify(this.currentLoggedInUser));
        this.router.navigate(['/dashboard']);
      } else {
        this.error = true;
      }
    });
  }
  
  signIn(provider){
    
    this.sub = this._auth.login(provider).subscribe(
      (data) => {
        console.log(data);
        this.user=data;

        this.error = false;
        this.userService.doSocialLogin(this.user)
        .then(user => {
          this.currentLoggedInUser = user;
          if (this.currentLoggedInUser.status === 200) {
            localStorage.setItem("currentUser",JSON.stringify(this.currentLoggedInUser));
            this.router.navigate(['/dashboard']);
          } else {
            this.error = true;
          }
        });
      }
    )
  }

  /*ngOnDestroy(){
    this.sub.unsubscribe();
  }*/

}
