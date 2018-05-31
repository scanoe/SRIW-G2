import { Component, Inject,  OnInit } from '@angular/core';
import { User } from '../../share/user';
import { RegisterService } from './register.service';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { AlertComponent } from '../../dialog/alert/alert.component';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
    userData: User; 

    constructor(private service: RegisterService,
                public modalDialog: MatDialog,
                ) { 
        this.userData = new User();
    }

    ngOnInit() {
    }

    register(){
        let self = this;
        this.service.addUser(this.userData).subscribe(function(result){
            let i = 0;
            const modalRef = self.modalDialog
                                    .open(AlertComponent, {
                                                width: '235px',
                                                data: {result: result, whoIsCalling: i }})
                                    });
            
        
    }
}
