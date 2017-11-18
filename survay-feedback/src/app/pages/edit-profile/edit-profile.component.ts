import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { FormBuilder, FormGroup, Validators , FormControl} from '@angular/forms';

@Component({
  selector: 'edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
  user : any = {};
  passwords : any = {};
  infoMessage : String;
  infoMessageStatus : boolean;
  infoMessageClass : String;
  rForm : FormGroup;

  constructor(private router: Router,public _auth: AuthService, private userService: UserService, private formBuilder: FormBuilder){ 
    if(JSON.parse(localStorage.getItem('currentUser')) === null) {
      // will be improve when api will be complete
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

    this.validate();
  }

  changePassword(){
      this.infoMessageStatus = false;
      this.userService.changePassword(this.passwords , JSON.parse(localStorage.getItem("currentUser")).accessToken)
      .then(response => {
        console.log(response);
          if (response.status.toString() == "FAILURE") {
                this.infoMessageStatus = true;
                this.infoMessage = "Error while changing password";
                this.infoMessageClass = "alert alert-danger alert-dismissable";
          } else if (response.status.toString() == "DUPLICATE") {    
                this.infoMessageStatus = true;
                this.infoMessage = "New Password is same as Current Password";
                this.infoMessageClass = "alert alert-warning alert-dismissable";
          } else if (response.status.toString() == "SUCCESS") {
                this.infoMessageStatus = true;
                this.infoMessage = "Password changed successfully";
                this.infoMessageClass = "alert alert-success alert-dismissable";
          }
    });
  }

  validate() {
    this.rForm = this.formBuilder.group({
      'password' :  [null, [Validators.compose([Validators.required, Validators.minLength(8), this.NoWhitespaceValidator])]],
      'newPassword': [null, [Validators.compose([Validators.required, Validators.minLength(8), this.NoWhitespaceValidator])]],
      'confNewPassword': [null, [this.matchOtherValidator('newPassword')]],
      });
  }

  public NoWhitespaceValidator(control: FormControl) {
    let isWhitespace = (control.value || '').trim().length === 0;
    let isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true }
  }

  matchOtherValidator (otherControlName: string) {
    let thisControl: FormControl;
    let otherControl: FormControl;

    return function matchOtherValidate (control: FormControl) {
      if (!control.parent) {
        return null;
      }
      // Initializing the validator.
      if (!thisControl) {
        thisControl = control;
        otherControl = control.parent.get(otherControlName) as FormControl;
        if (!otherControl) {
          throw new Error('matchOtherValidator(): other control is not found in parent group');
        }
        otherControl.valueChanges.subscribe(() => {
          thisControl.updateValueAndValidity();
        });
      }

      if (!otherControl) {
        return null;
      }

      if (otherControl.value !== thisControl.value) {
        return {
          matchOther: true
        };
      }

      return null;

    };

  }
}
