import { Component, OnInit } from '@angular/core';
import { Survey } from '../../../models/survey';
import { Question } from '../../../models/question';
import { Option } from '../../../models/option';
import { SurveyService} from '../../../services/survey.service';
import { FormGroup, FormArray, FormBuilder, Validators, FormControl } from '@angular/forms';
import { AuthService } from "angular2-social-login";
import { User } from '../../../models/user';
import { SurveyInfo } from '../../../models/survey-info';
import { Router,NavigationEnd } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';


@Component({
  selector: 'survey-page',
  templateUrl: './survey-page.component.html',
  styleUrls: ['./survey-page.component.css']
})
export class SurveyPageComponent implements OnInit {
  public surveyForm: FormGroup;
  survey: Survey;
  sub : any;
  user : User;
  surveyId : number;
  errorMessageStatus : boolean; 
  errorMessage : string;
  errorMessageClass : string;
  selectedSurvey : Survey;
  success : boolean;
  response = new FormArray([]);
  result: {
    surveyId,
    questionResponses,
    };

  constructor(private _fb: FormBuilder, private surveyService : SurveyService,private router: Router,public _auth: AuthService, private route: ActivatedRoute) {
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

    this.sub = this.route.params.subscribe(params => {
        this.surveyId = +params['id'];
     });

    this.getSurveyFromId();

  }

  getSurveyFromId(){
    this.surveyService.getSurveyFromId(this.surveyId, JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => {
        this.survey = response.body;
        this.updateForm();
    });
  }

getSurvey() {
    this.getSurveyFromId();
}

  updateForm() {
      this.surveyForm = this._fb.group({
        'response': this.response
      });
      for(var i=0; i < this.survey.questions.length; i++){
        if(this.survey.questions[i].required) {
          this.response.push(new FormControl('',Validators.required));
        } else {
          this.response.push(new FormControl(''));
        }
      } 
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

  submitSurvey() {
    debugger;
    if (!this.surveyForm.valid) {
        this.validateAllFormFields(this.surveyForm); 
    } else {
        let questionResponses = [];
        let response: {
          quesId,
          optionId,
        }

        for(var i=0; i < this.survey.questions.length; i++){
          response = {
          quesId: this.survey.questions[i].id,
          optionId: this.surveyForm.get('response').value[i]
        };
          questionResponses.push(response);
        }

        this.result = {
          surveyId: this.survey.id,
          questionResponses: questionResponses
        }

        this.saveSurveyResponse(this.result);
    }
  }

  saveSurveyResponse(result : any){
    console.log(result);
    this.errorMessageStatus = false;
    this.surveyService.submitSurvey(result, JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => {
        if (response.status.toString() === "SUCCESS"){
          this.errorMessageStatus = true;
          this.errorMessageClass = "alert alert-success flipInX animated";
          this.errorMessage = "Your response saved.";
        } else if (response.status.toString() === "DUPLICATE") {
          this.success = true;
          this.errorMessageStatus = true;
          this.errorMessageClass = "alert alert-warning flipInX animated";
          this.errorMessage = "Survey Already filled by you.";
        }else {
          this.errorMessageStatus = true;
          this.errorMessageClass = "alert alert-danger flipInX animated";
          this.errorMessage = "Error in Response Saving";
        }
    });
  }

  onSelect(survey: Survey): void {
    this.selectedSurvey = survey;
  }

}