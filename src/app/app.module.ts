import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { routing  } from './routes.module';
import { LoginComponent } from './user/login/login.component';
import { LoginService } from './login.service';
import { RegisterComponent } from './user/register/register.component';
import { RegisterService } from './user/register/register.service';
import { MaterializeModule } from 'angular2-materialize';
import { RankComponent } from './user/rank/rank.component';
import { DataService } from './data.service';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AlertComponent } from './dialog/alert/alert.component';
import { MatTableModule } from '@angular/material/table'
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule, MatInputModule } from '@angular/material';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { SuggestionComponent } from './user/suggestion/suggestion.component';

@NgModule({
  declarations: [
    AppComponent,
    RankComponent,
    LoginComponent,
    RegisterComponent,
    AlertComponent,
    SuggestionComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    routing,
    MaterializeModule,
    MatDialogModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule
  ],
  entryComponents: [AlertComponent],
  providers: [LoginService, DataService, RegisterService],
  bootstrap: [AppComponent]
})
export class AppModule { }
