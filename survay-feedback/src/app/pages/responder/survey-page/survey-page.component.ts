import 'rxjs/add/operator/switchMap';
import { Component, OnInit } from '@angular/core';
import { SurveyService } from "../../../services/survey.service";
import { AuthService } from "angular2-social-login";
import { Message } from '../../../models/message';
import { User } from '../../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { Survey } from '../../../models/survey';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { FormGroup, FormArray, FormBuilder, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { SurveyResponse } from '../../../models/survey-response';
import { QuestionResponse } from '../../../models/question-responses';
import { NavigationCancel } from '@angular/router';
import { URLSearchParams, } from '@angular/http';

@Component({
  selector: 'survey-page',
  templateUrl: './survey-page.component.html',
  styleUrls: ['./survey-page.component.css']
})
export class SurveyPageComponent implements OnInit {
    user : any = {};
    errorMessageStatus : boolean; 
    errorMessage : string;
    survey : Survey;
    surveyResponse : SurveyResponse;
    public myForm: FormGroup;
    formId : number;
    
  
    constructor(private _fb: FormBuilder, private _sanitizer: DomSanitizer, private router: Router,public _auth: AuthService, private route: ActivatedRoute, private surveyService: SurveyService, private formBuilder: FormBuilder){ 
        
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
         .switchMap((params: ParamMap) => this.surveyService.getSurveyFromId(this.formId =+params.get('id'), JSON.parse(localStorage.getItem("currentUser")).accessToken))
         .subscribe(response => this.survey = response.body);

        //  this.myForm = this._fb.group({
        //     'name': [this.survey.name],
        //     'questionResponse': this._fb.array([
        //         this.initQuestionResponse(),
        //     ])
        // });
    }
    
    initQuestionResponse() {
        return this._fb.group({
            'questionId': ['', Validators.required],
            'responseId': ['']
        });
    }

    addQuestionResponse() {
        const control = <FormArray>this.myForm.controls['questionResponse'];
        const addrCtrl = this.initQuestionResponse();
        
        control.push(addrCtrl);
        
    }

    getEmbbedUrl(url : string) {
        return this._sanitizer.bypassSecurityTrustResourceUrl(url);
    }
  
  
    // getSurveyFromId(surveyId : number){
    //   this.errorMessageStatus = false;
    //   this.surveyService.getSurveyFromId(surveyId, JSON.parse(localStorage.getItem("currentUser")).accessToken)
    //   .then(response => { 
    //       console.log(response);
    //       if (response.status.toString() == "SUCCESS") {
    //           this.survey = response.body;
    //       } else {
    //           this.errorMessageStatus = true;
    //           this.errorMessage = "Error in fetching survey list";
    //       }
    //   });
    // }
}
