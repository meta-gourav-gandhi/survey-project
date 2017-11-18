import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Message } from '../models/message';
import { User } from '../models/user';
import { UserDetail } from '../models/user-detail';
import { ServerResponse } from '../models/server-response';

@Injectable()
export class SurveyService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private surveyServiceUrl = 'http://172.16.33.118:8080/wesurve/survey/';  // <--- will change here

    constructor(private http: Http) { }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    }

    getViewableSurveyList(accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.surveyServiceUrl+"viewable", {headers: this.headers})  
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    deleteSurvey(surveyId : number, accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .delete(this.surveyServiceUrl+"?id="+surveyId, {headers: this.headers}) 
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    changeSurveyStatus(surveyId : number, accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .put(this.surveyServiceUrl+"changestatus/?id="+surveyId , surveyId,  {headers: this.headers}) 
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    getSurveyFromId(surveyId : number, accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.surveyServiceUrl+"?id="+surveyId ,  {headers: this.headers}) 
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    checkSurveyExists(surveyId : number, accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.surveyServiceUrl+"exists/?id="+surveyId ,  {headers: this.headers}) 
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    createOrRemoveViewer(surveyId : number, userId : number, accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .put(this.surveyServiceUrl+"edit/viewer/?id="+surveyId+"&userid="+userId, surveyId, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    getSurveyResponse(surveyId: number, accessToken: any): Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.surveyServiceUrl + 'response/?id=' + surveyId ,  {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    createSurvey(survey: any, accessToken: any): Promise<ServerResponse>{
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .post(this.surveyServiceUrl, survey, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    submitSurvey(surveyResponse : any, accessToken : any): Promise<ServerResponse>{
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .post(this.surveyServiceUrl+"save/response", surveyResponse, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    getSurveyResult(surveyId: number, accessToken: any): Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.surveyServiceUrl + 'result/?id=' + surveyId ,  {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    editSurvey(survey: any, accessToken: any): Promise<ServerResponse>{
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .put(this.surveyServiceUrl, survey, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }
}