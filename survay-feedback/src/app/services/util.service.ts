import { Injectable } from '@angular/core';
import {Location} from '@angular/common';

@Injectable()
export class UtilService {

    constructor(private _location: Location) { }
    
    back() {
        this._location.back();
    }
}