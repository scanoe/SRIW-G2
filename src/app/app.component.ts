import { Component, OnInit } from '@angular/core';
import { Assets } from './share/assets';
import { LoginService } from './login.service';
import { User } from './share/user';
import { Router, NavigationEnd } from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'app';
    logo = Assets.appLogo;
    inSesion : boolean;
    barTitle : string;
    data : User;
    showSuggestions = false;
    constructor (private service: LoginService,
                 private router: Router){
        this.inSesion = false;
        this.barTitle = "Recomendador IPS";
    }

    ngOnInit() {
        this.service.currentSesion.subscribe(data => this.data = data);
        this.service.suggestionsState.subscribe(data => this.showSuggestions = data);
        this.router.events.subscribe(this.onUrlChange.bind(this))
    }

    
    onUrlChange(ev) {
        if(ev instanceof NavigationEnd) {
            let url = ev.url;
            console.log(url);
            if(url.indexOf('user/rank') != -1)  {
                this.barTitle = this.data.email;
                this.inSesion = true;
            }
            if(url.indexOf('user/login') != -1 || url.indexOf('user/register') != -1)  {
                this.barTitle = "Recomendador IPS";
                this.inSesion = false;
            }

        }
    }
}
