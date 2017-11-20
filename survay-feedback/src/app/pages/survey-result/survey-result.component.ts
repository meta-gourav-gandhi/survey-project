import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { UtilService } from "../../services/util.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../models/message';
import { User } from '../../models/user';
import { Router } from '@angular/router';
import { SurveyInfo } from '../../models/survey-info';
import { SharedServiceService } from "../../services/shared-service.service";


@Component({
  selector: 'survey-results',
  templateUrl: './survey-result.component.html',
  styleUrls: ['./survey-result.component.css']
})
export class SurveyResultComponent implements OnInit {
  user : any = {};
  allViewableSurveys : SurveyInfo[]; 
  order: string = 'id';
  reverse: boolean = false;
  surveyFilter: any = {surveyName : '', id : ''};
  resultFetched : boolean = false;
  errorMessageStatus : boolean;
  errorMessage : String;

  constructor(private router: Router,
              public _auth: AuthService, 
              private userService: UserService,
              private utilService: UtilService,
              private sharedService: SharedServiceService
            ){ }

  ngOnInit() {
    setTimeout(() => {
      this.sharedService.saveTitle('View Survey Result');    
    });
    if(JSON.parse(localStorage.getItem('currentUser')) === null) {
        // will be improve when api will be complete
        this.router.navigate(['/login']);
      } else {
          this.user = JSON.parse(localStorage.getItem('currentUser'));
      }

      this.getViewableSurveyList();
  }

  getViewableSurveyList() {
    this.resultFetched = false;
    this.errorMessageStatus = false;
    this.userService.getViewableSurveyList(JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => {   
        this.resultFetched = true;
        if (response.status.toString() == "SUCCESS") {
            this.allViewableSurveys = response.body;
        }  else {
          this.errorMessageStatus = true;
          this.errorMessage = "Error in fetching survey list";
        }
     });
  }

  setOrder(value: string) {
    if (this.order === value) {
      this.reverse = !this.reverse;
    }

    this.order = value;
  }

  back() {
    this.utilService.back();
  }

}
