import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedServiceService } from "../../services/shared-service.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  title = 'Survey-Feedback Project';
  
  constructor(private router: Router, private sharedService: SharedServiceService){}

  ngOnInit() {
    setTimeout(() => {
      this.sharedService.clearTitle();    
    });
  }

}
