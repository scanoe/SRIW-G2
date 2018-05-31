import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { ErrorObservable } from 'rxjs/observable/ErrorObservable';
import { catchError, retry } from 'rxjs/operators';
import { HttpHeaders } from '@angular/common/http';
import { User } from '../../share/user';

@Injectable()
export class RegisterService {
    static httpOptions = {headers: new HttpHeaders({
                                'Content-Type':  'application/json'
                            })
                        };
    constructor(private http: HttpClient) { }

    static apiUrl = "http://52.67.23.207:8080/usuario/registro";
  
    addUser(user: User){
        return this.http.post(RegisterService.apiUrl, JSON.stringify(user), RegisterService.httpOptions);
    }
}
