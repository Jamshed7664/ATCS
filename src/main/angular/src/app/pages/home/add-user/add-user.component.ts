import { Router, ActivatedRoute } from '@angular/router';
import { ApiService } from 'src/app/services/api/api.service';
import { AuthService } from './../../../services/auth/auth.service';
import { FormGroup, FormControl, Validators, NgForm } from '@angular/forms';
import { Component, OnInit, ViewChild } from '@angular/core';
import swal from 'sweetalert';
@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {
  @ViewChild('addUserForm') public cerateUserform: NgForm
  selectedRoles: any[] = [];
  addUserForm: FormGroup
  selectedRole: any = [];
  Role: any = [];
  selected: boolean;
  addAser: any = {};
  btnValue = true;
  userId;
  redonly = true;
  canDeactivates: boolean = true;
  constructor(
    private authservices: AuthService,
    private myServices: ApiService,
    private router: ActivatedRoute,
    private route: Router
  ) { }

  ngOnInit() {
    this.addUser()
    this.getRole();
    this.addUserFormValue();
    this.queryParams();
  }
  queryParams() {
    console.log('in query params');
    this.router.params.subscribe((params: any) => {
      this.userId = params.id
    });
  }
  cancelEdit() {
    this.redonly = false
    this.addUserFormValue();
    if (!this.addAser) {
      this.addUserForm.reset()
    }
  }
  // Add User form
  addUser() {
    this.addUserForm = new FormGroup({
      'name': new FormControl('', Validators.required),
      'username': new FormControl(null, [Validators.required, Validators.pattern('^[@A-Za-z0-9_]{3,20}$')]),
      'email': new FormControl(null, [Validators.required, Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$')]),
      'contactNo': new FormControl(null, [Validators.pattern('[789][0-9]{9}')]),
      'roleIds': new FormControl([Validators.required]),
      'search': new FormControl(),
    })
  }

  addUserFormValue() {
    this.addAser = this.myServices.myFormValue;
    if (this.addAser) {
      console.log("add user form value", this.addAser);
      this.addUserForm.patchValue({
        name: this.addAser.name,
        username: this.addAser.username,
        email: this.addAser.email,
        contactNo: this.addAser.contactNo
      })
      // this.roleIds = this.addAser.roles;
      if (this.addAser.roles && this.redonly)
        this.addAser.roles.forEach(role => {
          this.selectedRole.push(role.id)
          this.selectedRoles.push(role);
        });
      this.btnValue = false;
    }
  }

  onChangeRole(item) {
    console.log(item);
    let index = this.selectedRole.indexOf((item.id));
    if (index === -1) {
      this.selectedRole.push((item.id));
      this.selectedRoles.push(item);
    } else {
      this.selectedRole.splice(index, 1)
      this.selectedRoles.splice(index, 1)
    }
    if (!this.selectedRole.length) {
      this.selected = true
    } else {
      this.selected = false
    }
  }

  isChecked(item) {
    console.log(item);

    if (this.selectedRole) {
      return this.selectedRole.find((id: any) => {
        return id === item.id
      });
    } else {
      return false;
    }
  }
  //  get role
  getRole() {
    this.authservices.getRole().subscribe((res) => {
      this.Role = res;
    })
  }



  UpdateVal() {
    this.addUserForm.controls['roleIds'].patchValue(this.selectedRole);
    this.authservices.updateUser(this.userId, this.addUserForm.value).subscribe((res) => {
    })
  }
  canDeactivate() {
    if (this.addUserForm.dirty && this.canDeactivates) {
      var confirmMsg = confirm('Do you want to discard changes');
      if (confirmMsg) {
        return true;
      } else {
        return false;
      }
    }
    return true
  }
  // Submit form
  onSubmit() {
    this.addUserForm.controls['roleIds'].patchValue(this.selectedRole);
    if (this.addUserForm.invalid || !this.selectedRole.length) {
      this.addUserForm.controls['name'].markAsTouched();
      this.addUserForm.controls['username'].markAsTouched();
      this.addUserForm.controls['username'].markAsTouched();
      this.addUserForm.controls['email'].markAsTouched();
      this.addUserForm.controls['contactNo'].markAsTouched();
      this.addUserForm.controls['roleIds'].markAsTouched();
      this.selected = true;
      if (this.selectedRole.length === 0) {
        this.selected = true;
      } else {
        this.selected = false;
      }
    } else {
      if (this.userId) {
        swal({
          text: "Are you sure you want to update?",
          icon: "warning",
          closeOnClickOutside: false,
          buttons: ['Yes', 'No'],
          dangerMode: true,
        }).then((willUpdate) => {
          if (!willUpdate) {
            this.canDeactivates = false
            this.authservices.updateUser(this.userId, this.addUserForm.value).subscribe(() => {
              swal({
                title: ' User Updated Successfully!!',
                text: 'Redirecting...',
                icon: 'success',
                timer: 1000,
                buttons: [false],
              }).then(() => {
                this.route.navigate(['/home/users']);
              })
            })
          }
        })
      } else {
        this.canDeactivates = false
        this.authservices.saveuser(this.addUserForm.value).subscribe(res => {
          console.log(res);
          this.addUserForm.reset();
          swal({
            title: 'User Added Successfully!!',
            text: 'Redirecting...',
            icon: 'success',
            timer: 2000,
            buttons: [false],
          }).then(() => {
            this.route.navigate(['/home/users']);
          })
        })
      }
    }
  }
}

