import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent }   from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component'; 
import { SignupComponent } from './pages/signup/signup.component'; 
import { DashboardComponent } from './pages/dashboard/dashboard.component'; 
import { CreateSurveyorComponent } from './pages/admin/create-surveyor/create-surveyor.component';
import { SurveyResultComponent } from './pages/survey-result/survey-result.component';
import { EditProfileComponent } from './pages/edit-profile/edit-profile.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { ManageSurveyComponent } from './pages/surveyor/manage-survey/manage-survey.component';
import { SearchSurveyComponent } from './pages/responder/search-survey/search-survey.component';
import { SurveyPageComponent } from './pages/responder/survey-page/survey-page.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'login',  component: LoginComponent },
  { path: 'home',  component: HomeComponent },
  { path: 'signup',  component: SignupComponent },
  { path: 'dashboard',  component: DashboardComponent },
  { path: 'createSurveyor',  component: CreateSurveyorComponent },
  { path: 'surveyResults',  component: SurveyResultComponent },
  { path: 'editProfile',  component: EditProfileComponent },
  { path: 'forgotPassword',  component: ForgotPasswordComponent },
  { path: 'manageSurveys',  component: ManageSurveyComponent },
  { path: 'searchSurvey',  component: SearchSurveyComponent },
  { path: 'survey/:id',  component: SurveyPageComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
