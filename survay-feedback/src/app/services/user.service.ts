import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Message } from '../models/message';
import { User } from '../models/user';
import { UserDetail } from '../models/user-detail';
import { ServerResponse } from '../models/server-response';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable()
export class UserService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private userServiceUrl = 'http://172.16.33.118:8080/wesurve/user/';  // URL to web api

    constructor(private http: Http) { }

    doSignup(user : any) : Promise<ServerResponse>  {

        return this.http
        .post(this.userServiceUrl+"register", user, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    doSocialLogin(user : any) : Promise<ServerResponse>  {

        return this.http
        .post(this.userServiceUrl+"sociallogin", user, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    doLogin(user : any) : Promise<ServerResponse> {
        
        return this.http
        .post(this.userServiceUrl+"login", user, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    doLogout(accessToken : string) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.userServiceUrl+"logout", {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    }

    setCurrentUser(currentUser: any) {
        localStorage.setItem("currentUser",JSON.stringify(currentUser));
    }

    getCurrentUser(): any {

    }

    getAllUsers(accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.userServiceUrl, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    getAllUsersForSurveyor(surveyId : number, accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.userServiceUrl+"surveyor/userlist/?id="+surveyId, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    surveyorCheck(userId : number, accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .post(this.userServiceUrl+"createsurveyor/"+userId, userId, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }


    changePassword(passwords : any, accessToken : any) : Promise<ServerResponse> {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .put(this.userServiceUrl+"changepassword", passwords, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    getViewableSurveyList(accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.userServiceUrl+"viewer/surveylist", {headers: this.headers})  
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    recoverPassword(email : string) : Promise<ServerResponse> {
        return this.http
        .get(this.userServiceUrl+"forgotpassword?email="+email, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    getSurveyList(accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.userServiceUrl+"surveyor/surveylist", {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }

    getFilledSurveyList(accessToken : any) : Promise<ServerResponse>  {
        this.headers = new Headers({'Content-Type': 'application/json', 'X-auth' : accessToken});
        return this.http
        .get(this.userServiceUrl+"responder/surveylist", {headers: this.headers})
        .toPromise()
        .then(res => res.json() as ServerResponse)
        .catch(this.handleError);
    }
}