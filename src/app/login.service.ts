import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { ErrorObservable } from 'rxjs/observable/ErrorObservable';
import { catchError, retry } from 'rxjs/operators';
import { HttpHeaders } from '@angular/common/http';
import { User } from './share/user';
import { BehaviorSubject } from 'rxjs';


@Injectable()
export class LoginService {
    currentUser: User;
    static httpOptions = {headers: new HttpHeaders({
                                'Content-Type':  'application/json'
                            })
                        };
    constructor(private http: HttpClient) { 
        this.currentUser = new User();
    }
    
    private sesionData = new BehaviorSubject(new User());
    currentSesion = this.sesionData.asObservable();
    private showReviews = new BehaviorSubject(false);
    suggestionsState = this.showReviews.asObservable();
   
    changeReviewsState(state: boolean) {
        this.showReviews.next(state);
    }

    changeSesion(user: User) {
        this.currentUser = user;
        this.sesionData.next(user);
    }

    static apiUrl = "http://52.67.23.207:8080/usuario/iniciar-sesion";
  
    login(user: User){
        return this.http.post(LoginService.apiUrl, JSON.stringify(user), LoginService.httpOptions);
    }
    
    reviewData(data){
        let url = "http://52.67.23.207:8080/usuario/calificar";
        return this.http.post(url, JSON.stringify(data), LoginService.httpOptions);
    }

    seeInfo(id:string){
        let url = "http://52.67.23.207:8080/verinfo?id=" + id;
        return this.http.get(url);
    }

    getSuggestions(){
        let url = "http://52.67.23.207:8080/usuario/recomendar";
        console.log(JSON.stringify({email:this.currentUser.email}));
        return this.http.post(url, JSON.stringify({email:this.currentUser.email}), LoginService.httpOptions);
    }

    getIps(){
        let url = "http://52.67.23.207:8080/ips";
        return this.http.get(url);
    }
}
