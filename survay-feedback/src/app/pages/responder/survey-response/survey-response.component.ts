import 'rxjs/add/operator/switchMap';
import { Component, OnInit } from '@angular/core';
import { SurveyService } from '../../../services/survey.service';
import { UtilService } from '../../../services/util.service';
import { AuthService } from 'angular2-social-login';
import { Message } from '../../../models/message';
import { User } from '../../../models/user';
import { Router, NavigationEnd } from '@angular/router';
import { Survey } from '../../../models/survey';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';
import { SurveyResponse } from '../../../models/survey-response';
import { QuestionResponse } from '../../../models/question-responses';
import { Option } from '../../../models/option';
import { SharedServiceService } from "../../../services/shared-service.service";

@Component({
  selector: 'app-survey-response',
  templateUrl: './survey-response.component.html',
  styleUrls: ['./survey-response.component.css']
})
export class SurveyResponseComponent implements OnInit {
    user: any = {};
    errorMessageStatus: boolean;
    errorMessage: string;
    survey: Survey;
    surveyResponseMap = new Map();

    constructor(private _sanitizer: DomSanitizer,
        private router: Router,
        public _auth: AuthService,
        private route: ActivatedRoute,
        private surveyService: SurveyService, 
        private utilService: UtilService,
        private sharedService: SharedServiceService
    ) {
        if (JSON.parse(localStorage.getItem('currentUser')) === null) {
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
            window.scrollTo(0, 0);
        });

        this.route.paramMap
        .switchMap((params: ParamMap) =>
        this.surveyService.getSurveyFromId(+params.get('id'), JSON.parse(localStorage.getItem('currentUser')).accessToken))
        .subscribe(response => {
            if (response.status.toString() == "INTERNAL_ERROR") {
                this.router.navigate(['/404']);
            }
            this.survey = response.body;
            this.sharedService.saveTitle(this.survey.name + ' - Response');
            
        });

        this.route.paramMap
        .switchMap((params: ParamMap) =>
        this.surveyService.getSurveyResponse(+params.get('id'), JSON.parse(localStorage.getItem('currentUser')).accessToken))
        .subscribe(response => this.surveyResponseMap = response.body);
    }

    getEmbbedUrl(url: string) {
        return this._sanitizer.bypassSecurityTrustResourceUrl(url);
    }

    back() {
        this.utilService.back();
    }
}