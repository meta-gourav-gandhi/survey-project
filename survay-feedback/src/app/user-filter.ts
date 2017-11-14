
import { Component, OnInit } from '@angular/core';

import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
name: 'userFilter'
})
export class UserFilterPipe implements PipeTransform {
    transform(items: any[], searchText): any {

        return searchText 
            ? items.filter(item => (item.name.toLowerCase().indexOf(searchText) !== -1 ||
             item.email.toLowerCase().indexOf(searchText) !== -1 ||
             item.id == searchText)) 
            : items;
    }
}