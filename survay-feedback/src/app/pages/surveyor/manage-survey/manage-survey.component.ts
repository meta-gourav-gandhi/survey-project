import { Component, OnInit } from '@angular/core';
import { UserService } from "../../../services/user.service";
import { SurveyService } from "../../../services/survey.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../../models/message';
import { User } from '../../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { SurveyInfo } from '../../../models/survey-info';
import { FilterPipe } from '../../../filters';

@Component({
  selector: 'manage-survey',
  templateUrl: './manage-survey.component.html',
  styleUrls: ['./manage-survey.component.css']
})
export class ManageSurveyComponent implements OnInit {
  user : any = {};
  surveyList : SurveyInfo[];
  selectedSurvey : SurveyInfo;
  selectedSurveyForDeletion : SurveyInfo;
  isCopied: boolean = false;
  errorMessageStatus : boolean; 
  errorMessage : string;
  order: string = 'id';
  reverse: boolean = false;
  surveyFilter: any = {surveyName : '', id : ''};
  showOrderIcon : boolean = false;

  constructor(private router: Router,public _auth: AuthService, private userService: UserService, private surveyService: SurveyService){ }

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

      this.getSurveyList();
  }

  onSelect(survey: SurveyInfo): void {
    this.selectedSurvey = survey;
  }

  onSelectForDeletion(survey: SurveyInfo): void {
    this.selectedSurveyForDeletion = survey;
  }

  getSurveyList() {
    this.errorMessageStatus = false;
    this.userService.getSurveyList(JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => { 
        console.log(response);
        if (response.status.toString() == "SUCCESS") {
            this.surveyList = response.body;
        } else {
            this.errorMessageStatus = true;
            this.errorMessage = "Error in fetching survey list";
        }
     });

  }

  deleteSurvey(surveyId : number) {
    this.errorMessageStatus = false;
    this.surveyService.deleteSurvey(surveyId, JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => { 
        console.log(response);
        if (response.status.toString() == "SUCCESS") {
            this.getSurveyList();
        } else {
            this.errorMessageStatus = true;
            this.errorMessage = "Error in survey deletion";
        }
     });
  }

  changeSurveyStatus(surveyId : number) {
    this.errorMessageStatus = false;
    this.surveyService.changeSurveyStatus(surveyId, JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => { 
        console.log(response);
        if (response.status.toString() == "SUCCESS") {
            
        } else {
            this.errorMessageStatus = true;
            this.errorMessage = "Error in Changeing Survey Status";
            if (document.getElementById("sid"+surveyId).classList.contains("active")) {
                document.getElementById("sid"+surveyId).classList.remove('active');
            } else {
                document.getElementById("sid"+surveyId).classList.add('active');
            }
            
        }
     });
  }

  setOrder(value: string) {
    if (this.order === value) {
      this.reverse = !this.reverse;
    }

    this.order = value;
  }
}
