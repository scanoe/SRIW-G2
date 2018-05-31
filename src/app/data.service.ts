import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { User } from './share/user';

@Injectable()
export class DataService {

  private sesionData = new BehaviorSubject(new User());
  currentSesion = this.sesionData.asObservable();

  constructor() { }

  changeSesion(user: User) {
    this.sesionData.next(user)
  }

}

