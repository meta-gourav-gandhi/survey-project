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

@Component({
  selector: 'view-created-survey',
  templateUrl: './view-created-survey.component.html',
  styleUrls: ['./view-created-survey.component.css']
})
export class ViewSurveyComponent implements OnInit {
    user : any = {};
    errorMessageStatus : boolean; 
    errorMessage : string;
    survey : Survey;
    selectedSurvey : Survey;
    
  
    constructor(private _sanitizer: DomSanitizer, private router: Router,public _auth: AuthService, private route: ActivatedRoute, private surveyService: SurveyService){ 
        
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

         this.route.paramMap
         .switchMap((params: ParamMap) => this.surveyService.getSurveyFromId(+params.get('id'), JSON.parse(localStorage.getItem("currentUser")).accessToken))
         .subscribe(response => this.survey = response.body);

    }

    getEmbbedUrl(url : string) {
        return this._sanitizer.bypassSecurityTrustResourceUrl(url);
    }

    onSelect(survey: Survey): void {
        this.selectedSurvey = survey;
      }
}
