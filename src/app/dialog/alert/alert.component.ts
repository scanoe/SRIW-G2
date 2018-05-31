import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Router } from '@angular/router';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit {
    content: string;
    routes = ['/user/login', '/user/suggestion']; 
    whoIsCalling: number;
    infoData = {};
    defaultWindow = true;
    constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              public dialogRef: MatDialogRef<AlertComponent>,
              private router: Router) { 
              
        this.whoIsCalling = data.whoIsCalling;
        if(this.whoIsCalling != 2){
            this.content = data.result.message;
        }else{
            console.log(data.result);
            this.defaultWindow = false;
            this.infoData = data.result['ips'];
        }
    }

    ngOnInit() {
    }

    goToLogin(){
        if(this.whoIsCalling === 2){
            this.dialogRef.close();
            return;
        }
        this.dialogRef.close();     
        this.router.navigate([this.routes[this.whoIsCalling]]);
    }

}
