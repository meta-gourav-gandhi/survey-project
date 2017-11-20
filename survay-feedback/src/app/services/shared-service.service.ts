import { Injectable, OnInit } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class SharedServiceService implements OnInit {
  private userSubject;
  private titleSubject = new Subject<string>();

  constructor() {
    this.userSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('currentUser')) !== null); 
  }

  ngOnInit() {
   
  }

  saveTitle(title: string) {
    this.titleSubject.next(title);
  }

  getTitle(): Observable<any> {
    return this.titleSubject.asObservable();
  }

  clearTitle() {
    this.titleSubject.next();
  }
  saveUser(loggedIn: boolean) {
    this.userSubject.next(loggedIn);
  }

  getUser(): Observable<any> {
    return this.userSubject.asObservable();
  }

  clearUser() {
    this.userSubject.complete();
  }
}
