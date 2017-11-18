import { SurveyInfo } from './models/survey-info';
import { Component, OnInit } from '@angular/core';

import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
name: 'filter'
})
export class FilterPipe implements PipeTransform {
    transform(items: any[], searchText): any {
        return searchText 
            ? items.filter(item => ((item.surveyName.toLowerCase().indexOf(searchText) !== -1 ||
             item.labels.toLowerCase().indexOf(searchText) !== -1 ||
             item.id == searchText) && (searchText.toLowerCase().indexOf(",") == -1))) 
            : items;
    }
}