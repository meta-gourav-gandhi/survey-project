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
import { AlertComponent} from './pages/alert/alert.component';
import { AlertService } from './services/alert.service';

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
    AlertComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    HttpModule,
    FormsModule,
    Angular2SocialLoginModule
  ],
  providers: [UserService,AlertService],
  bootstrap: [AppComponent]
})
export class AppModule { }

Angular2SocialLoginModule.loadProvidersScripts(providers);
