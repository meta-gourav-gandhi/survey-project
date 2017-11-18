import { Component, OnInit } from '@angular/core';
import { AuthService } from "angular2-social-login";
import { UserService } from "../../services/user.service";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgClass } from '@angular/common';


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
  rForm : FormGroup;
  loadingData : boolean;

  constructor(private router: Router,public _auth: AuthService, private userService: UserService,private formBuilder: FormBuilder){ 
    if(JSON.parse(localStorage.getItem('currentUser')) !== null) {
      this.router.navigate(['/dashboard']);
    }
  }

  ngOnInit() {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
          return;
      }
      window.scrollTo(0, 0)
    });

    this.error = false; 
    this.validate();
  }

  doLogin() : void {
    this.loadingData = true;
    this.error = false;
    setTimeout(function(){ 
      document.getElementById("loginMessage").innerHTML = "It's taking longer than expected.. Please Wait.";
     }, 5000);
    this.userService.doLogin(this.user)
    .then(response => {
      this.loadingData = false;
      if (response.status.toString() == "SUCCESS") {
        this.currentLoggedInUser = response.body;
        localStorage.setItem("currentUser",JSON.stringify(this.currentLoggedInUser));
        this.router.navigate(['/dashboard']);
        location.reload();
      } else {
        this.error = true;
      }
    });
  }
  
  signIn(provider){
    this.loadingData = true;
    this.sub = this._auth.login(provider).subscribe(
      (data) => {
        this.user=data;

        this.error = false;
        this.userService.doSocialLogin(this.user)
        .then(response => {
          this.loadingData = false;
          if (response.status.toString() == "SUCCESS") {
            this.currentLoggedInUser = response.body;
            localStorage.setItem("currentUser",JSON.stringify(this.currentLoggedInUser));
            this.router.navigate(['/dashboard']);
            location.reload();
          } else {
            this.error = true;
          }
        });
      }
    )
  }

  validate() {
    this.rForm = this.formBuilder.group({
        'email': [null, Validators.compose([Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$')])],
        'password': [null, Validators.required]
      });
  }

  /*ngOnDestroy(){
    this.sub.unsubscribe();
  }*/

}
