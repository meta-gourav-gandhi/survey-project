import { Component, OnInit } from '@angular/core';
import { SurveyService } from "../../../services/survey.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../../models/message';
import { User } from '../../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { Survey } from '../../../models/survey'; 
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'search-survey',
  templateUrl: './search-survey.component.html',
  styleUrls: ['./search-survey.component.css']
})
export class SearchSurveyComponent implements OnInit {
  user : any = {};
  errorMessageStatus : boolean; 
  errorMessage : string;
  rForm : FormGroup;

  constructor(private router: Router,public _auth: AuthService, private surveyService: SurveyService,private formBuilder: FormBuilder){ 
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

  checkSurveyExists(surveyId : number){
    this.errorMessageStatus = false;
    this.surveyService.checkSurveyExists(surveyId, JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => { 
        if (response.status.toString() == "SUCCESS") {
            this.router.navigate(['/survey',surveyId]);
        } else if (response.status.toString() == "NOT_ACCESSIBLE") {
            this.errorMessageStatus = true;
            this.errorMessage = "Survey is not live right now. Please come back after some time.";
        } else if(response.status.toString() == "DUPLICATE") {
            this.errorMessageStatus = true;
            this.errorMessage = "Survey is already filled by you.";
        }else {
            this.errorMessageStatus = true;
            this.errorMessage = "Survey not exists with given id.";
        }
    });
  }

  validate() {
      console.log("Entered");
    this.rForm = this.formBuilder.group({
        'surveyID': [null, Validators.compose([Validators.required, Validators.pattern('^[0-9]*$')])],
      });
  }
}
