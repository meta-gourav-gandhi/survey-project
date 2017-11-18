import { Component, OnInit } from '@angular/core';
import { UserService} from '../../services/user.service';
import { AlertService } from '../../services/alert.service';
import { Location }                 from '@angular/common';
import { Router,NavigationEnd } from '@angular/router';
import { Message } from '../../models/message';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { PasswordValidation } from '../../utils/password-validation';
import { DatePipe } from '@angular/common';
 
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
  rForm : FormGroup;
  

  constructor(private userService: UserService,
    private router: Router,private location: Location, private alertService: AlertService,private formBuilder: FormBuilder) {
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

    this.regMessageStatus = false;
    this.validate();
  }

  doSignup() : void {
    if (!this.rForm.valid) {
      this.validateAllFormFields(this.rForm);
    } else {
      console.log('submitted');
    this.regMessageStatus = false;
    this.userService.doSignup(this.user)
      .then(response => {
        if (response.status.toString() === "INVALID_CONTENT") {
          this.regMessageStatus = true;
          this.regMessageClass = "alert alert-danger alert-dismissable";
          this.regMessage = "Oops! Something went wrong!";
        } else if (response.status.toString() === "SUCCESS") {
          this.message = response.body;
          this.regMessageStatus = true;
          this.regMessageClass = "alert alert-success alert-dismissable";
          this.regMessage = "Successfully Sign Up!";
        } else if (response.status.toString() === "DUPLICATE") {
          this.regMessageStatus = true;
          this.regMessageClass = "alert alert-warning alert-dismissable";
          this.regMessage = "Email already Exists!";
        }
      });
    }
  }

  validate() {
    this.rForm = this.formBuilder.group({
      'email': [null, Validators.compose([Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$')])],
      'password': [null, [Validators.compose([Validators.required, Validators.minLength(8), this.NoWhitespaceValidator])]],
      'confpassword': [null, [this.matchOtherValidator('password')]],
      'name': [null, Validators.compose([Validators.required, Validators.minLength(2), this.NoWhitespaceValidator])],
      'dob': [null, Validators.required],
      'gender': [null, Validators.required]
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

 validateAllFormFields(formGroup: FormGroup) {
  Object.keys(formGroup.controls).forEach(field => {
    const control = formGroup.get(field);
    if (control instanceof FormControl) {
      control.markAsTouched({ onlySelf: true });
    } else if (control instanceof FormGroup) {
      this.validateAllFormFields(control);
    }
  });
  }

}
