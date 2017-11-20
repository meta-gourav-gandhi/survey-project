import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { FormBuilder, FormGroup, Validators , FormControl} from '@angular/forms';

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
  rForm : FormGroup;

  constructor(private router: Router,public _auth: AuthService, private userService: UserService, private formBuilder: FormBuilder){ }

  ngOnInit() {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
          return;
      }
      window.scrollTo(0, 0)
    });

    this.validate();
  }

  validate() {
    this.rForm = this.formBuilder.group({
      'email': [null, Validators.compose([Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$')])]
      });
  }


  recoverPassword() {
    this.loadingData = true;
    this.infoMessageStatus = false;
    this.userService.recoverPassword(this.rForm.value.email)
    .then(response => {
      this.loadingData = false;
      if (response.status.toString() == "SUCCESS") {
          this.infoMessageStatus = true;
          this.infoMessage = "Email Sent to mail id " + this.rForm.value.email ;
          this.infoMessageClass = "alert alert-success alert-dismissable";
      } else {
          this.infoMessageStatus = true;
          this.infoMessage = "Email not exists in System Database";
          this.infoMessageClass = "alert alert-danger alert-dismissable";
      }
    });
  }
}
