import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Message } from '../models/message';
import { User } from '../models/user';

@Injectable()
export class UserService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private userServiceUrl = 'http://172.16.33.118:8080/wesurve/user/';  // URL to web api

    constructor(private http: Http) { }

    doSignup(user : any) : Promise<Message>  {

        return this.http
        .post(this.userServiceUrl+"register", user, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as Message)
        .catch(this.handleError);
    }

    doSocialLogin(user : any) : Promise<User>  {

        return this.http
        .post(this.userServiceUrl+"sociallogin", user, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as User)
        .catch(this.handleError);
    }

    doLogin(user : any) : Promise<User> {

        return this.http
        .post(this.userServiceUrl+"login", user, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as User)
        .catch(this.handleError);
    }

    doLogout(accessToken : string) : Promise<Message>  {

        return this.http
        .post(this.userServiceUrl+"logout", accessToken, {headers: this.headers})
        .toPromise()
        .then(res => res.json() as Message)
        .catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    }
}