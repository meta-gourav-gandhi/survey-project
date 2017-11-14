import { Component, OnInit } from '@angular/core';
import { Survey } from '../../../models/survey';
import { Question } from '../../../models/question';
import { Option } from '../../../models/option';
import { SurveyService} from '../../../services/survey.service';
import { FormGroup, FormArray, FormBuilder, Validators, FormControl } from '@angular/forms';
import { AuthService } from "angular2-social-login";
import { User } from '../../../models/user';
import { Router,NavigationEnd } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';


@Component({
  selector: 'survey-page',
  templateUrl: './survey-page.component.html',
  styleUrls: ['./survey-page.component.css']
})
export class SurveyPageComponent implements OnInit {
  public surveyForm: FormGroup;
  survey;
  sub : any;
  user : User;
  surveyId : number;
  response = new FormArray([]);
  result: {
    surveyId,
    questionResponse,
    };

  constructor(private _fb: FormBuilder, private surveyService : SurveyService,private router: Router,public _auth: AuthService, private route: ActivatedRoute) {
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

    this.sub = this.route.params.subscribe(params => {
        this.surveyId = +params['id'];
     });

    this.getSurvey();

  }

//   getSurveyFromId(){
//     this.surveyService.getSurveyFromId(this.surveyId, JSON.parse(localStorage.getItem("currentUser")).accessToken)
//     .then(response => {
//         this.survey = response.body;
//     });
//     this.updateForm();
//   }

getSurvey() {
    //call service
    //assign value to this.survey
   
    //mock data just for testing
    //remove it when calling service
    let questions: Question[] = [];
    let question: Question;
    let options: Option[] = [];
    let option: Option;
    option = {
      id: 0,
      text: 'Option 1',
    };
    options.push(option);
    option = {
      id: 1,
      text: 'Option 2',
    };
    options.push(option);
    question = {
      id: 0,
      text: '<p>Question 1</p>',
      required: true,
      options: options
    }
    questions.push(question);
    question = {
      id: 1,
      text: '<p>Question 2</p>',
      required: false,
      options: options
    }
    questions.push(question);
    this.survey = {
      id: 0,
      name: 'survey 1',
      description: 'some survey',
      labels: null,
      questions: questions
    }

    //call this always
    this.updateForm();
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
    if (!this.surveyForm.valid) {
        this.validateAllFormFields(this.surveyForm); 
        } else {
        let questionResponse = [];
        let response: {
        questionId,
        optionId,
        }
        for(var i=0; i < this.survey.questions.length; i++){
        response = {
        questionId: this.survey.questions[i].id,
        optionId: this.surveyForm.get('response').value[i]
        };
        questionResponse.push(response);
        }
        this.result = {
        surveyId: this.survey.id,
        questionResponse: questionResponse
        }
        }
  }
}