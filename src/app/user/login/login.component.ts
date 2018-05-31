import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { LoginService } from '../../login.service';
import { User } from '../../share/user';
import { DataService } from '../../data.service';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
    loginData: User; 
    data: User;
    @Output() changeBar = new EventEmitter<string>();

    constructor(private service: LoginService,
                private dataService: DataService,
                private router: Router) { 
    }

    ngOnInit() {
        this.loginData = new User();
        this.data = new User();
        this.service.currentSesion.subscribe(data => this.data = data);
    }

    login(){
        let self = this;
        this.service.login(this.loginData).subscribe(function(result){
            self.service.changeSesion(self.loginData);
            self.router.navigate(['/user/rank']);
            self.changeBar.emit(self.loginData.email);
        });
    }

    updateSesion(){
    }
}
