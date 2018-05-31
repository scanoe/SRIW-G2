import { Component,  OnInit, ViewChild} from '@angular/core';
import { LoginService } from '../../login.service';
import { User } from '../../share/user';
import { Router } from "@angular/router";
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import { DataSource } from '@angular/cdk/table';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { AlertComponent } from '../../dialog/alert/alert.component';

@Component({
  selector: 'app-rank',
  templateUrl: './rank.component.html',
  styleUrls: ['./rank.component.css']
})
export class RankComponent implements OnInit {
    loginData: User;
    displayedColumns = ['id', 'nombre', 'like', 'dislike'];
    dataSource: MatTableDataSource<any>;
    ipsArray = [];
    requestData = {};
    unRankedData = [];

    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;

    constructor(private service: LoginService,
                private router: Router,
                public modalDialog: MatDialog) { 
    
        this.dataSource = new MatTableDataSource();
    }

    ngOnInit() {
        this.service.currentSesion.subscribe(data => this.loginData = data);
        this.requestData = {email: this.loginData.email, calificaciones:[]} 
        if(this.loginData.email === ""){
            this.router.navigate(["/user/login"]);
        }
        this.service.getIps().subscribe(data => this.initializeTable(data));
    }

    initializeTable(data: any){
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        for(let i = 0; i < data['ips'].length; i++){
            let idIps = data['ips'][i]['id'];
            let nameIps = data['ips'][i]['nombre'];
            let row = {index: i, id: idIps, nombre: nameIps, likeState: "favorite_border",
                                dislikeState: "close", requestIndex: -1};
            this.ipsArray.push(row);
        }
        this.unRankedData = JSON.parse(JSON.stringify(this.ipsArray)); 
        this.dataSource.data = this.ipsArray;
    }


    applyFilter(filterValue: string) {
        filterValue = filterValue.trim(); // Remove whitespace
        filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
        this.dataSource.filter = filterValue;
        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }
    }

    rank(i: number, likeState:string, dislikeState:string, like:number){
        if(this.ipsArray[i].likeState === likeState && this.ipsArray[i].dislikeState === dislikeState){
            return;
        }
        this.ipsArray[i].likeState = likeState;
        this.ipsArray[i].dislikeState = dislikeState;
        this.dataSource.data = this.ipsArray;
        let size = this.requestData['calificaciones'].length;
        let index = this.ipsArray[i].requestIndex; 
        if(index > -1){
            this.requestData['calificaciones'][index]['like'] = like;
        }else{
            this.requestData['calificaciones'].push({id:this.ipsArray[i]['id'], like: like});
            this.ipsArray[i].requestIndex = size;
        }
        return;
    }

    reviewData(){
        if(this.requestData['calificaciones'].length === 0){
            return;
        }
        this.ipsArray = JSON.parse(JSON.stringify(this.unRankedData));
        this.dataSource.data = this.ipsArray;
        let self = this;
        let i = 1;
        this.service.reviewData(this.requestData).subscribe(function(result){
                                            const modalRef = self.modalDialog
                                                    .open(AlertComponent, {
                                                        width: '235px',
                                                        data: {result: result, whoIsCalling: i }
                                                    });
                                            });
    }

    cancelReview(){
        this.ipsArray = JSON.parse(JSON.stringify(this.unRankedData));
        this.dataSource.data = this.ipsArray;
    }

}
