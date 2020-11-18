import { FormGroup, FormControl } from '@angular/forms';
import { ApiService } from 'src/app/services/api/api.service';
import { Router } from '@angular/router';
import { AuthService } from './../../../services/auth/auth.service';
import { Component, OnInit } from '@angular/core';
import swal from 'sweetalert';
import * as $ from 'jquery'
@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.scss']
})
export class RolesComponent implements OnInit {

  constructor(private authServices: AuthService, private router: Router, private myServices: ApiService) { }

  loader: boolean
  roles: any;
  authorities: any;
  givenAuthorities = [];
  form; FormGroup
  ngOnInit() {
    this.getAllRoles();
    this.getAuthorities()
    this.form = new FormGroup({
      'search': new FormControl(null)
    })
  }

  getAuthorities() {
    let userInfo = JSON.parse(window.localStorage.getItem('userInfo'));
    this.authorities = userInfo.authorities;
    this.authorities.forEach(element => {
      this.givenAuthorities.push(element.name)
    });
  }

  // Get All Roles
  getAllRoles() {
    this.loader = true
    this.authServices.getRole().subscribe((res) => {
      this.roles = res;
      this.loader = false;
    })
  }

  // Update Role send item into service to get it and Update
  updateRole(value) {
    this.myServices.updateRole(value);
  }

  sendData(item: any) {
    this.router.navigate(['home/addrole/' + item.id])
  }
  // Reset roles value from object
  resetRolesValue() {
    this.myServices.resetRoleModel()
  }

  // Delete Role
  deleteRole(item: any) {
    swal({
      text: "Are you sure you want to Delete?",
      icon: "warning",
      closeOnClickOutside: false,
      buttons: ['Yes', 'No'],
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
        this.router.navigate(['/home/roles'])
      } else {
        this.authServices.deleteSingleRole(item.id).subscribe(res => {
          swal({
            title: 'Success',
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
  // Activate Authorities
  ActiveDeactiveRole(item) {
    swal({
      text: "Are you sure you want to Activate?",
      icon: "warning",
      closeOnClickOutside: false,
      buttons: ['Yes', 'No'],
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
        this.router.navigate(['/home/roles'])
      } else {
        this.authServices.ativateRole(item.id).subscribe(res => {
          swal({
            title: 'Success',
            text: 'Activated Successfully',
            icon: 'success',
            timer: 2000,
            buttons: [false],
          })
          item.active = true;
        })
      }
    });
  }
}
