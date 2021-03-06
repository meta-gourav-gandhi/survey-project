import 'rxjs/add/operator/switchMap';
import { Component, OnInit } from '@angular/core';
import { SurveyService } from "../../../services/survey.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../../models/message';
import { User } from '../../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { Survey } from '../../../models/survey';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';
import { SurveyResponse } from '../../../models/survey-response';
import { QuestionResponse } from '../../../models/question-responses';
import { UserDetailForSurveyor } from '../../../models/user-detail-for-surveyor';
import { UserService } from "../../../services/user.service";

@Component({
  selector: 'view-created-survey',
  templateUrl: './view-created-survey.component.html',
  styleUrls: ['./view-created-survey.component.css']
})
export class ViewSurveyComponent implements OnInit {
    user : any = {};
    errorMessageStatus : boolean; 
    errorMessage : string;
    survey;
    selectedSurvey : Survey;
    allUsers : UserDetailForSurveyor[];
    sub : any;
    formId : number;
    p: number = 1;
    
  
    constructor(private userService: UserService, private _sanitizer: DomSanitizer, private router: Router,public _auth: AuthService, private route: ActivatedRoute, private surveyService: SurveyService){ 
        this.survey = new Survey();
    }

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

        // this.route.paramMap
        // .switchMap((params: ParamMap) => this.surveyService.getSurveyFromId(+params.get('id'), JSON.parse(localStorage.getItem("currentUser")).accessToken))
        // .subscribe(response => this.survey = response.body);

        this.sub = this.route.params.subscribe(params => {
            this.formId = +params['id'];
         });

        this.getSurveyFromId();
        this.getAllUsers();

    }

    getSurveyFromId(){
        this.surveyService.getSurveyFromId(this.formId, JSON.parse(localStorage.getItem("currentUser")).accessToken)
        .then(response => {
            this.survey = response.body;
        });
    }

    getEmbbedUrl(url : string) {
        return this._sanitizer.bypassSecurityTrustResourceUrl(url);
    }

    onSelect(survey: Survey): void {
        this.selectedSurvey = survey;
    }

    getAllUsers() {
        this.userService.getAllUsersForSurveyor(this.formId, JSON.parse(localStorage.getItem("currentUser")).accessToken)
        .then(response => { 
            if (response.status.toString() == "ACCESS_GRANTED") {
                this.allUsers = response.body;
            } else {
                this.router.navigate(['/home']);
            }
         });
      }
    
      /** function creates a surveyor if not already a survyor, if already a server then remove the surveyor role */
      createOrRemoveViewer(user : UserDetailForSurveyor){
        this.surveyService.createOrRemoveViewer(this.formId, user.id , JSON.parse(localStorage.getItem("currentUser")).accessToken)
        .then(response => { 
            
            console.log(response);
            if (response.status.toString() == "SUCCESS") {
    
            } else {
                this.errorMessageStatus = true;
                this.errorMessage = "Error in Changing Viewer Status";
                if (document.getElementById("uid"+user.id).classList.contains("active")) {
                    document.getElementById("uid"+user.id).classList.remove('active');
                } else {
                    document.getElementById("uid"+user.id).classList.add('active');
                }
            }
         });
      }
}
