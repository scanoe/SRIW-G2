import { Component,  OnInit, ViewChild} from '@angular/core';
import { LoginService } from '../../login.service';
import { Router } from "@angular/router";
import { User } from '../../share/user';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import { DataSource } from '@angular/cdk/table';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { AlertComponent } from '../../dialog/alert/alert.component';

@Component({
  selector: 'app-suggestion',
  templateUrl: './suggestion.component.html',
  styleUrls: ['./suggestion.component.css']
})
export class SuggestionComponent implements OnInit {
    localData = [];
    loginData: User;

    displayedColumns = ['id', 'nombre', 'seeInfo'];
    dataSource: MatTableDataSource<any>;

    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;

    constructor(private service: LoginService,
                private router: Router,
                public modalDialog: MatDialog) { 
    
        this.dataSource = new MatTableDataSource();
    }


  ngOnInit() {
        this.service.currentSesion.subscribe(data => this.loginData = data);
        if(this.loginData.email === ""){
            this.router.navigate(["/user/login"]);
        }
        this.service.getSuggestions().subscribe(data => this.initializeTable(data));
  }

  initializeTable(data: any){
        if(data['ips'].length > 0){
            this.service.changeReviewsState(true);
        }
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        for(let i = 0; i < data['ips'].length; i++){
            let idIps = data['ips'][i]['id'];
            let nameIps = data['ips'][i]['nombre'];
            let row = {id: idIps, nombre: nameIps};
            this.localData.push(row);
        }
        this.dataSource.data = this.localData;
    }

  applyFilter(filterValue: string) {
        filterValue = filterValue.trim(); // Remove whitespace
        filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
        this.dataSource.filter = filterValue;
        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }
  }

  seeInfo(idIps:string){
        let self = this;
        this.service.seeInfo(idIps)
                .subscribe(function(result){
                    const modalRef = self.modalDialog
                            .open(AlertComponent, {
                                width: '235px',
                                data: {result: result, whoIsCalling: 2 }
                            });
                });

  }

}
