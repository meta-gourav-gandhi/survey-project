import { Component, OnInit } from '@angular/core';
import { SurveyService } from "../../../services/survey.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../../models/message';
import { User } from '../../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { Survey } from '../../../models/survey'; 
@Component({
  selector: 'search-survey',
  templateUrl: './search-survey.component.html',
  styleUrls: ['./search-survey.component.css']
})
export class SearchSurveyComponent implements OnInit {
  user : any = {};
  errorMessageStatus : boolean; 
  errorMessage : string;
  //survey : Survey;

  constructor(private router: Router,public _auth: AuthService, private surveyService: SurveyService){ }

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

  checkSurveyExists(surveyId : number){
    this.errorMessageStatus = false;
    this.surveyService.checkSurveyExists(surveyId, JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => { 
        console.log(response);
        if (response.status.toString() == "SUCCESS") {
            //this.survey = response.body;
            this.router.navigate(['/survey',surveyId]);
        } else {
            this.errorMessageStatus = true;
            this.errorMessage = "Error in fetching survey list";
        }
    });
  }
}
