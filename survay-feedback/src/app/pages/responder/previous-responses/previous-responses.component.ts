import { Component, OnInit } from '@angular/core';
import { UserService } from "../../../services/user.service";
import { SurveyService } from "../../../services/survey.service";
import { UtilService } from "../../../services/util.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../../models/message';
import { User } from '../../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { SurveyInfo } from '../../../models/survey-info';
import { FilterPipe } from '../../../filters';
import { SharedServiceService } from "../../../services/shared-service.service";

@Component({
  selector: 'app-previous-responses',
  templateUrl: './previous-responses.component.html',
  styleUrls: ['./previous-responses.component.css']
})
export class PreviousResponsesComponent implements OnInit {
  user : any = {};
  surveyList : SurveyInfo[];
  errorMessageStatus : boolean; 
  errorMessage : string;
  order: string = 'id';
  reverse: boolean = false;
  surveyFilter: any = {surveyName : '', id : ''};
  showOrderIcon : boolean = false;
  resultFetched : boolean = false;

  constructor(private router: Router,
            public _auth: AuthService, 
            private userService: UserService, 
            private surveyService: SurveyService, 
            private utilService: UtilService,
            private sharedService: SharedServiceService){ 
    if(JSON.parse(localStorage.getItem('currentUser')) === null) {
      // will be improve when api will be complete
      this.router.navigate(['/login']);
    } else {
        this.user = JSON.parse(localStorage.getItem('currentUser'));
    }
  }

  ngOnInit() {
    setTimeout(() => {
      this.sharedService.saveTitle('Filled Surveys');
    });
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
          return;
      }
      window.scrollTo(0, 0)
    });

      this.getSurveyList();
    }

  getSurveyList() {
    this.resultFetched = false;
    this.errorMessageStatus = false;
    this.userService.getFilledSurveyList(JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => { 
        this.resultFetched = true;
        if (response.status.toString() == "SUCCESS") {
            this.surveyList = response.body;
        } else {
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