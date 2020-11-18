import { FormGroup, FormControl } from '@angular/forms';
import { ApiService } from 'src/app/services/api/api.service';
import { model } from './../add-user/model';
import { AuthService } from './../../../services/auth/auth.service';
import { Routes, RouterModule, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import swal from 'sweetalert';
import * as $ from 'jquery'
const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
];

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})

export class UsersComponent implements OnInit {
  allUsers: any = [];
  myModel = new model();
  loader = false
  authorities: any;
  givenAuthorities = [];
  searchVal = 2
  placeholderText = "Search.."
  authorityObj: any = [];
  authorityArray: any = [];
  serchInput = true;
  uname: any;
  currntUserName;
  roleName;
  form: FormGroup
  constructor(private authServices: AuthService, private myServices: ApiService, private routes: Router) { }

  ngOnInit() {
    this.getAllUsers();
    this.getAuthorities()
    this.getCurrentUser();
    this.form = new FormGroup({
      'search': new FormControl(null)
    })
  }

  getCurrentUser() {
    this.uname = JSON.parse(window.localStorage.getItem('userInfo'))
    this.currntUserName = this.uname.username
  }

  getAuthorities() {
    let userInfo = JSON.parse(window.localStorage.getItem('userInfo'));
    this.authorities = userInfo.authorities;
    this.authorities.forEach(element => {
      this.givenAuthorities.push(element.name)
    });
  }

  // get All users
  getAllUsers() {
    this.loader = false;
    this.authServices.getUSers().subscribe((res) => {
      this.allUsers = res;
      console.log('Get All Users');
      console.log(this.allUsers);
      this.loader = true;
    })
  }

  // Update user
  UpdateUser(value) {
    this.myServices.afterAddValue((value))
    this.myModel.name = value.name;
  }


  clearModel() {
    this.myServices.resetMyFormVAlue();
  }

  // send data
  sendData(item: any) {
    this.routes.navigate(['/home/user/' + item.id])
  }
  // Delete single User
  delete(item) {
    swal({
      text: "Are you sure you want to Delete?",
      icon: "warning",
      closeOnClickOutside: false,
      buttons: ['Yes', 'No'],
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
        this.routes.navigate(['/home/users'])
      } else {
        this.authServices.deleteSingleUser(item.id).subscribe(res => {
          console.log(res);
          swal({
            title: 'Goog Job',
            text: 'Deleted Successfully',
            icon: 'success',
            timer: 1000,
            buttons: [false],
          })
          item.active = false;
        })
      }
    });
  }

  getRoleValue(item) {
    this.roleName = item.name
    if (!this.authorityArray) {
      this.authorityObj = item.authorities
      this.authorityObj.forEach(element => {
        this.authorityArray.push(element.name)
      });
    }
    else {
      this.authorityArray = [];
      this.authorityObj = item.authorities
      this.authorityObj.forEach(element => {
        this.authorityArray.push(element.name)
      })
    }
  }

  ActiveDeactive(item) {
    swal({
      text: "Are you sure you want to Activate?",
      icon: "warning",
      closeOnClickOutside: false,
      buttons: ['Yes', 'No'],
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
        this.routes.navigate(['/home/users'])
      } else {
        this.authServices.ativateUser(item.id).subscribe(res => {
          console.log(res);
          swal({
            title: 'Success',
            text: 'Activated Successfully',
            icon: 'success',
            timer: 2000,
            buttons: [false],
          })
          item.active = true;
        }
        )
      }
    });
  }
}
