import 'rxjs/add/operator/switchMap';
import { Component, OnInit } from '@angular/core';
import { SurveyService } from '../../../services/survey.service';
import { AuthService } from 'angular2-social-login';
import { Message } from '../../../models/message';
import { User } from '../../../models/user';
import { Router, NavigationEnd } from '@angular/router';
import { Survey } from '../../../models/survey';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';
import { SurveyResponse } from '../../../models/survey-response';
import { QuestionResponse } from '../../../models/question-responses';
import { SurveyResult } from '../../../models/survey-result';
import { Option } from '../../../models/option';
import { QuestionResult } from '../../../models/question-result';
import { UtilService } from '../../../services/util.service'; 
import { SharedServiceService } from "../../../services/shared-service.service";

@Component({
  selector: 'app-view-survey-result',
  templateUrl: './view-survey-result.component.html',
  styleUrls: ['./view-survey-result.component.css']
})
export class ViewSurveyResultComponent implements OnInit {
    user: any = {};
    errorMessageStatus: boolean;
    errorMessage: string;
    survey;
    surveyResult: SurveyResult;
    questionResults: QuestionResult[];
    data;
    chartTypeCustom = new Map() // Here
    optionset;
    order: string = 'id';

    public barChartOptions: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        scales: {
            xAxes: [{
                barPercentage: 0.4,
                ticks: {
                    beginAtZero: false,
                }
            }],
            yAxes: [{
                ticks: {
                    beginAtZero: true,
                }
            }]
        }
    };
        
    public pieChartOptions: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true,
                }
            }]
        }
    };

    myType = 'bar';
    public chartLabels: string[] = ['option A', 'option B', 'option C', 'option D'];
    public chartType: string = this.myType;
    public chartLegend = true;
    public chartData: any[] = [
        {data: [65, 59, 44, 81], label: 'Selected Option'  }
      ];

    constructor(private _sanitizer: DomSanitizer,
        private router: Router,
        public _auth: AuthService,
        private route: ActivatedRoute,
        private surveyService: SurveyService,
        private utilService: UtilService,
        private sharedService: SharedServiceService
    ) {
        this.data = new Map();
        this.optionset = new Map();

        this.survey = new Survey();
        if (JSON.parse(localStorage.getItem('currentUser')) === null) {
            this.router.navigate(['/login']);
        } else {
            this.user = JSON.parse(localStorage.getItem('currentUser'));
        }

        this.route.paramMap
        .switchMap((params: ParamMap) =>
        this.surveyService.getSurveyFromId(+params.get('id'), JSON.parse(localStorage.getItem('currentUser')).accessToken))
        .subscribe(response => {
            if (response.status.toString() == "SUCCESS") {
                this.survey = response.body;
                this.sharedService.saveTitle(this.survey.name + ' - Result');
            } else {
                this.router.navigate(['/404']);
            }
        });

        this.updateSet();
    }

    ngOnInit() {
        this.router.events.subscribe((evt) => {
            if (!(evt instanceof NavigationEnd)) {
                return;
            }
            window.scrollTo(0, 0);
        });    
    } 

    updateSet() {
        this.route.paramMap
        .switchMap((params: ParamMap) =>
        this.surveyService.getSurveyResult(+params.get('id'), JSON.parse(localStorage.getItem('currentUser')).accessToken))
        .subscribe(response => { 
            this.surveyResult = response.body;
            this.questionResults = this.surveyResult.questionResults;

            for(let questionResultInstance of this.questionResults) {
                let optionset = questionResultInstance.option;
                let dataset = questionResultInstance.data;

                this.chartTypeCustom.set(questionResultInstance.id,"bar");
                let clone = JSON.parse(JSON.stringify(this.chartData));
                clone[0].data = dataset;
    
                this.chartData = clone;
                this.chartLabels = optionset;
    
                this.optionset.set(questionResultInstance.id, this.chartLabels);
                this.data.set(questionResultInstance.id, this.chartData);
            }
        });
    }

    getChartType(id : number) : string {
        return this.chartTypeCustom.get(id);
    }
    getOptionSet(id : number) : String[]{
        return this.optionset.get(id);
    }

    getDataSet(id : number) : Object{
        return this.data.get(id);
    }

    public changeType(curType: string, id: number) {
        if (curType === 'pie') {
            this.chartTypeCustom.set(id,'bar');
        } else {
            this.chartTypeCustom.set(id,'pie');
        }
    }

    public chartClicked(e: any): void {
       
    }

    public chartHovered(e: any): void {
        
    }

    getEmbbedUrl(url: string) {
        return this._sanitizer.bypassSecurityTrustResourceUrl(url);
    }

    back() {
        this.utilService.back();
    }
}
