import { Routes, Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery'
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ApiService } from 'src/app/services/api/api.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import swal from 'sweetalert';

@Component({
  selector: 'app-add-role',
  templateUrl: './add-role.component.html',
  styleUrls: ['./add-role.component.scss']
})
export class AddRoleComponent implements OnInit {
  selectedAuthorities: any[] = [];
  authorityIds = [];
  createRoleForm: FormGroup;
  eventValue: boolean;
  authId: any;
  roleValues: any;
  authVal: boolean;
  authorities: any = [];
  canDeactivates: boolean = true;
  constructor(private myApiServices: ApiService,
    private authServices: AuthService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    this.createRoleForm = new FormGroup({
      'name': new FormControl('', Validators.required),
      'authorityIds': new FormControl([]),
      'search': new FormControl(),
    })
    this.showAuthoities();
    this.queryParams();
    this.onGetRoleValue()
  }
  // get Authority Id-
  queryParams() {
    this.route.params.subscribe((params: any) => {
      this.authId = params.id
    });
  }
  cancelUpdate() {
    this.val = false;
    this.onGetRoleValue();
    this.createRoleForm.reset();
    this.eventValue = false;
  }
  // get role value from service and pathc it
  val = true
  onGetRoleValue() {
    this.roleValues = this.myApiServices.rolesItem;
    if (this.roleValues) {
      this.createRoleForm.patchValue({
        name: this.roleValues.name
      })
      if (this.roleValues.authorities && this.val) {
        console.log('length is', this.authorityIds.length);
        this.roleValues.authorities.forEach(role => {
          this.authorityIds.push(role.id)
          this.selectedAuthorities.push(role);
        });
      }
    }
  }
  // Get Authorities
  onChangeAuthorities(item) {
    let index = this.authorityIds.indexOf((item.id));
    if (index == -1) {
      this.authorityIds.push((item.id));
      this.selectedAuthorities.push(item);
    } else {
      this.authorityIds.splice(index, 1)
      this.selectedAuthorities.splice(index, 1)
    }
    if (!this.authorityIds.length) {
      this.eventValue = true;
    } else {
      this.eventValue = false;
    }
  }
  // Get authorities
  showAuthoities() {
    this.authServices.getAuthority().subscribe(res => {
      this.authorities = res;
    }, err => {
      swal('Warning', err.error.message, 'warning')
    });
  }
  valuechange(e) {

  }


  isChecked(item) {
    if (this.authorityIds) {
      return this.authorityIds.find(id => {
        return id === item.id
      });
    } else {
      return false;
    }
  }
  canDeactivate() {
    if (this.createRoleForm.dirty && this.canDeactivates) {
      var confirmMsg = confirm('Do you want to discard changes');
      if (confirmMsg) {
        return true;
      } else {
        return false;
      }
    }
    return true
  }
  // Submit Form
  onSubmit() {
    this.createRoleForm.controls['authorityIds'].patchValue(this.authorityIds);
    if (this.createRoleForm.invalid || !this.authorityIds.length) {
      this.createRoleForm.controls['name'].markAsTouched();
      this.eventValue = true;
      if (this.authorityIds.length == 0) {
        this.eventValue = true;
      } else {
        this.eventValue = false;
      }
    }
    else {
      if (this.authId) {
        swal({
          text: "Are you sure you want to update?",
          icon: "warning",
          closeOnClickOutside: false,
          buttons: ['Yes', 'No'],
          dangerMode: true,
        }).then((willDelete) => {
          if (willDelete) {
          } else {
            this.canDeactivates = false
            this.authServices.updateRole(this.authId, this.createRoleForm.value).subscribe(res => {
              console.log(res);
              swal({
                title: ' Role Updated Successfully!!',
                text: 'Redirecting...',
                icon: 'success',
                timer: 1000,
                buttons: [false],
              }).then(() => {
                this.router.navigate(['/home/roles']);
              })
            })
          }
        });

      } else {
        var name = this.createRoleForm.value.name;
        this.canDeactivates = false
        this.authServices.onCreateRole(name, this.authorityIds).subscribe(res => {
          this.createRoleForm.reset()
          swal({
            title: 'Role Created Successfully!!',
            text: 'Redirecting...',
            icon: 'success',
            timer: 1000,
            buttons: [false],
          }).then(() => {
            this.router.navigate(['/home/roles']);
          })
        });
      }
    }
  }
}
