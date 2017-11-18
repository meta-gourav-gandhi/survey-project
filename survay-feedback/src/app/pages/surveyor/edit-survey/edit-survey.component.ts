import { Component, OnInit } from '@angular/core';
import { Survey } from '../../../models/survey';
import { Question } from '../../../models/question';
import { Option } from '../../../models/option';
import { MatSnackBar } from '@angular/material';
import { FormGroup, FormArray, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Label } from '../../../models/label';
import { MatChipInputEvent } from '@angular/material';
import { ENTER } from '@angular/cdk/keycodes';
import { Router, NavigationEnd } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { SurveyService } from '../../../services/survey.service';

const COMMA = 188;

@Component({
  selector: 'app-edit-survey',
  templateUrl: './edit-survey.component.html',
  styleUrls: ['./edit-survey.component.css']
})
export class EditSurveyComponent implements OnInit {

  public surveyForm: FormGroup;
  survey;
  labels: string[] = [];
  visible: boolean = true;
  sub : any;
  selectable: boolean = true;
  removable: boolean = true;
  addOnBlur: boolean = true;
  separatorKeysCodes = [ENTER, COMMA];
  constructor(private surveyService: SurveyService,private router: Router,
    private route: ActivatedRoute,public snackBar: MatSnackBar, private _fb: FormBuilder) {
      this.survey = new Survey();
      
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
    this.route.paramMap
    .switchMap((params: ParamMap) =>
    this.surveyService.getSurveyFromId(+params.get('id'), JSON.parse(localStorage.getItem('currentUser')).accessToken))
    .subscribe(response => {
      this.survey = response.body;
      this.updateForm();
    });

    this.surveyForm = this._fb.group({
      'surveyQuestions': this._fb.array([]),
      'surveyName': [],
      'surveyDesc': [],
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
      'questionId': 0,
      'questionText': ['Question', Validators.required],
      'questionOptions': this._fb.array([
          this.initOptions(),
        ]),
      'required': [false, null]
    });
  }

  initOptions() {
    return this._fb.group({
      optionId: 0,
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

  updateForm() {
    this.surveyForm.controls['surveyName'].setValue(this.survey.name);
    this.surveyForm.controls['surveyName'].setValidators([Validators.required, this.NoWhitespaceValidator]);
    this.surveyForm.controls['surveyDesc'].setValue(this.survey.description);
    const control = <FormArray>this.surveyForm.controls['surveyQuestions'];
    this.survey.questions.forEach(question => {
      control.push(this.setQuestions(question));
    });
    this.survey.labels.forEach(label => {
      this.labels.push(label);
    });
  }

  editSurvey() {
    if (!this.surveyForm.valid) {
      this.validateAllFormFields(this.surveyForm);
    } else {
      let questions: Question[] = [];
      let question: Question;
      let options: Option[];
      let option: Option;
      this.surveyForm.get('surveyQuestions').value.forEach(question => {
        options = [];
        question.questionOptions.forEach(questionOptions => {
          option = {
            id: questionOptions.optionId,
            text: questionOptions.optionText,
          };
          options.push(option);
        });
        question = {
          id: question.questionId,
          text: question.questionText,
          required: question.required,
          options: options
        }
        questions.push(question);
      });

      this.survey.questions = questions;
      this.survey.labels = this.surveyForm.get('surveyLabels').value;
      this.survey.description = this.surveyForm.get('surveyDesc').value;
      this.survey.name = this.surveyForm.get('surveyName').value;

      console.log(this.survey);

      this.surveyService.editSurvey(this.survey, JSON.parse(localStorage.getItem("currentUser")).accessToken)
      .then(response => { 
          console.log(response);
          // if (response.status.toString() == "SUCCESS") {
          //     this.surveyList = response.body;
          //     this.loading = false;
          // } else {
          //     this.errorMessageStatus = true;
          //     this.errorMessage = "Error in fetching survey list";
          // }
       });
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

  setQuestions(question: Question){
    let options = new FormArray([]);
    let group = this._fb.group({
      'questionId': [question.id],
      'questionText': [question.text, Validators.required],
      'questionOptions': options,
      'required': question.required
    });
    question.options.forEach(element => {
      options.push(this.setOptions(element));
    });
    return group;
  }
 
  setOptions(option: Option){
    return this._fb.group({
      optionId: [option.id],
      optionText: [option.text, Validators.required]
    });
  }
}

