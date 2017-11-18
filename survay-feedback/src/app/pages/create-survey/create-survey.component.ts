import { Component, OnInit } from '@angular/core';
import { Survey } from '../../models/survey';
import { User } from '../../models/user';
import { Question } from '../../models/question';
import { Option } from '../../models/option';
import { MatSnackBar } from '@angular/material';
import { FormGroup, FormArray, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Label } from '../../models/label';
import { MatChipInputEvent } from '@angular/material';
import { ENTER } from '@angular/cdk/keycodes';
import { SurveyService} from '../../services/survey.service';
import { Router,NavigationEnd } from '@angular/router';
import { CreatedSurveyResponse } from '../../models/created-survey-response';

const COMMA = 188;

@Component({
  selector: 'app-create-survey',
  templateUrl: './create-survey.component.html',
  styleUrls: ['./create-survey.component.css'],
})
export class CreateSurveyComponent implements OnInit {
  public surveyForm: FormGroup;
  survey = {name: 'Untitled Survey', description: ''};
  labels: string[] = [];
  visible: boolean = true;
  selectable: boolean = true;
  removable: boolean = true;
  addOnBlur: boolean = true;
  errorMessageStatus : boolean; 
  errorMessage : string;
  errorMessageClass : string;
  separatorKeysCodes = [ENTER, COMMA];
  user : User;
  loadingData : boolean;
  createdSurvey : CreatedSurveyResponse;

  constructor(private router: Router,public snackBar: MatSnackBar, private _fb: FormBuilder, private surveyService : SurveyService) {
    if(JSON.parse(localStorage.getItem('currentUser')) === null) {
      // will be improve when api will be complete
      this.router.navigate(['/login']);
    } else {
        this.user = JSON.parse(localStorage.getItem('currentUser'));
    }
  }

  public questionEditor: Object = {
    charCounterCount: false,
    placeholderText: 'Write a question',
    toolbarButtons: ['bold', 'italic', 'underline', 'fontFamily', 'fontSize', 'color', 'insertImage', 'insertVideo', 'undo', 'redo'],
    toolbarButtonsXS: ['bold', 'italic', 'underline', 'fontFamily', 'fontSize', 'color', 'insertImage', 'insertVideo', 'undo', 'redo'],
    toolbarButtonsSM: ['bold', 'italic', 'underline', 'fontFamily', 'fontSize', 'color', 'insertImage', 'insertVideo', 'undo', 'redo'],
    toolbarButtonsMD: ['bold', 'italic', 'underline', 'fontFamily', 'fontSize', 'color', 'insertImage', 'insertVideo', 'undo', 'redo'],
    events : {
      'froalaEditor.initialized': function (e, editor) {
          editor.toolbar.hide();
      },
      'froalaEditor.focus': function (e, editor) {
          editor.toolbar.show();
      },
      'froalaEditor.blur': function (e, editor) {
          editor.toolbar.hide();
      }
    }
  };

  public optionEditor: Object = {
    charCounterCount: false,
    placeholderText: 'Option',
    toolbarButtons: ['bold', 'italic', 'underline', 'fontFamily', 'fontSize', 'color', 'insertImage', 'insertVideo', 'undo', 'redo'],
    toolbarButtonsXS: ['bold', 'italic', 'underline', 'fontFamily', 'fontSize', 'color', 'insertImage', 'insertVideo', 'undo', 'redo'],
    toolbarButtonsSM: ['bold', 'italic', 'underline', 'fontFamily', 'fontSize', 'color', 'insertImage', 'insertVideo', 'undo', 'redo'],
    toolbarButtonsMD: ['bold', 'italic', 'underline', 'fontFamily', 'fontSize', 'color', 'insertImage', 'insertVideo', 'undo', 'redo'],
    events : {
      'froalaEditor.initialized': function (e, editor) {
          editor.toolbar.hide();
      },
      'froalaEditor.focus': function (e, editor) {
          editor.toolbar.show();
      },
      'froalaEditor.blur': function (e, editor) {
          editor.toolbar.hide();
      }
    }
  };

  ngOnInit() {
    this.surveyForm = this._fb.group({
      'surveyQuestions': this._fb.array([
        this.initQuestions(),
      ]),
      'surveyName': [this.survey.name, [Validators.required, this.NoWhitespaceValidator]],
      'surveyDesc': [null, null],
      'surveyLabels': [this.labels, null]
    });
    
  }

  public NoWhitespaceValidator(control: FormControl) {
    let isWhitespace = (control.value || '').trim().length === 0;
    let isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true };
  }

  initQuestions() {
    return this._fb.group({
      'questionText': ['Question', Validators.required],
      'questionOptions': this._fb.array([
          this.initOptions(),
        ]),
      'required': [false, null]
    });
  }

  initOptions() {
    return this._fb.group({
      optionText: ['Option', Validators.required]
    });
  }

  addQuestion(id: number) {
    const control = <FormArray>this.surveyForm.controls['surveyQuestions'];
    control.insert((id + 1), this.initQuestions());
  }

  deleteQuestion(questionId: number) {
    const control = <FormArray>this.surveyForm.controls['surveyQuestions'];
    control.removeAt(questionId);
  }

  addOption(questionId: number) {
    const control = (<FormArray>(<FormArray>this.surveyForm.controls['surveyQuestions']).controls[questionId]).controls['questionOptions'];
    control.push(this.initOptions());
  }


  deleteOption(questionId: number, optionId: number) {
    const control = (<FormArray>(<FormArray>this.surveyForm.controls['surveyQuestions']).controls[questionId]).controls['questionOptions'];
    control.removeAt(optionId);
  }

  add(event: MatChipInputEvent): void {
    let input = event.input;
    let value = event.value;

    // Add our person
    if ((value || '').trim()) {
      this.labels.push(value.trim());
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }

  remove(label: any): void {
    let index = this.labels.indexOf(label);

    if (index >= 0) {
      this.labels.splice(index, 1);
    }
  }

  createSurvey() {
    if (!this.surveyForm.valid) {
      this.validateAllFormFields(this.surveyForm); 
    } else {
      let questions: Question[] = [];
      let question: Question;
      this.surveyForm.get('surveyQuestions').value.forEach(element => {
        
          let options: Option[] = [];
          let option: Option
          element.questionOptions.forEach(questionOptions => {
          option = {
            id: 0,
            text: questionOptions.optionText,
          };
          options.push(option);
        });
        question = {
          id: 0,
          text: element.questionText,
          required: element.required,
          options: options
        }
        questions.push(question);
      });
      let survey: Survey = {
        id: 0,
        status : null,
        name: this.surveyForm.get('surveyName').value,
        description: this.surveyForm.get('surveyDesc').value,
        labels: this.surveyForm.get('surveyLabels').value,
        questions: questions
      }
      console.log(survey);
      this.saveSurvey(survey);
    }
  }

  saveSurvey(survey : any){
    this.loadingData = true;
    this.surveyService.createSurvey(survey, JSON.parse(localStorage.getItem("currentUser")).accessToken)
    .then(response => { 
        this.loadingData = false;
        if (response.status.toString() == "SUCCESS") {
          this.createdSurvey = response.body;
        } else {
          this.errorMessageStatus = true;
          this.errorMessageClass = "alert alert-danger flipInX animated";
          this.errorMessage = "Error in creating survey.";
        }
    });
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
}