import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { SignupComponent } from './pages/signup/signup.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { UserService} from './services/user.service';
import { HttpModule } from '@angular/http';
import { FormsModule }   from '@angular/forms';
import { Angular2SocialLoginModule } from "angular2-social-login";
import { AlertService } from './services/alert.service';
import { AdminComponent } from './pages/admin/admin.component';
import { CreateSurveyorComponent } from './pages/admin/create-surveyor/create-surveyor.component';
import { SurveyorComponent } from './pages/surveyor/surveyor.component';
import { ResponderComponent } from './pages/responder/responder.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxPaginationModule} from 'ngx-pagination'; // 
import { SurveyResultComponent } from './pages/survey-result/survey-result.component';
import { SurveyService } from './services/survey.service';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {MatTooltipModule, MatFormFieldModule} from '@angular/material';
import { EditProfileComponent } from './pages/edit-profile/edit-profile.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { ManageSurveyComponent } from './pages/surveyor/manage-survey/manage-survey.component';
import { SideNavbarComponent } from './pages/side-navbar/side-navbar.component';
import { ClipboardModule } from 'ngx-clipboard';
import { Ng2OrderModule } from 'ng2-order-pipe';
import { FilterPipe } from './filters';
import { UserFilterPipe } from './user-filter';
import { SearchSurveyComponent } from './pages/responder/search-survey/search-survey.component';
import { SurveyPageComponent } from './pages/responder/survey-page/survey-page.component';
import { SafeUrlPipe } from './safe-url-pipe';
import { MatSlideToggleModule, MatInputModule, MatButtonModule, MatSnackBarModule } from '@angular/material';
import { MatChipsModule } from '@angular/material/chips';
import { FroalaEditorModule, FroalaViewModule } from 'angular-froala-wysiwyg';
import { CreateSurveyComponent } from './pages/create-survey/create-survey.component';
import { ViewSurveyComponent } from './pages/surveyor/view-survey/view-created-survey.component';
import { PreviousResponsesComponent } from './pages/responder/previous-responses/previous-responses.component';
import { SurveyResponseComponent } from './pages/responder/survey-response/survey-response.component';

let providers = {
    "google": {
      "clientId": "107874532235-rgslilik9ul40g70vp62rbn6t7i7bbjc.apps.googleusercontent.com"
    },
    "facebook": {
      "clientId": "1976997799244411",
      "apiVersion": "v2.8"
    }
  };

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    SignupComponent,
    DashboardComponent,
    AdminComponent,
    SurveyorComponent,
    ResponderComponent,
    CreateSurveyorComponent,
    SurveyResultComponent,
    EditProfileComponent,
    ForgotPasswordComponent,
    ManageSurveyComponent,
    SideNavbarComponent,
    FilterPipe,
    UserFilterPipe,
    SearchSurveyComponent,
    SurveyPageComponent,
    SafeUrlPipe,
    CreateSurveyComponent,
    ViewSurveyComponent,
    PreviousResponsesComponent,
    SurveyResponseComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    HttpModule,
    FormsModule,
    Angular2SocialLoginModule,
    ReactiveFormsModule,
    NgxPaginationModule,
    NoopAnimationsModule,
    MatTooltipModule,
    MatFormFieldModule,
    ClipboardModule,
    Ng2OrderModule,
    MatSlideToggleModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    MatChipsModule,
    FroalaEditorModule.forRoot(),
    FroalaViewModule.forRoot()
  ],
  providers: [UserService,AlertService,SurveyService],
  bootstrap: [AppComponent]
})
export class AppModule { }

Angular2SocialLoginModule.loadProvidersScripts(providers);
