import { Router } from '@angular/router';
import { AuthService } from './../../../services/auth/auth.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import swal from 'sweetalert';
import { timeInterval } from 'rxjs/operators';
@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {
  changePasswordForm: FormGroup;
  changePasswordFormValue: any;
  msg: string;
  erroShow: boolean = false;
  errMessage: string;
  canDeactivates: boolean = true;
  constructor(private apiServices: AuthService, private route: Router) { }

  ngOnInit() {
    this.changePasswordForm = new FormGroup({
      'password': new FormControl(null, [Validators.required]),
      'oldPassword': new FormControl(null, [Validators.required]),
      'cnfrmpassword': new FormControl(null, [Validators.required]),
    })
  }

  // clear Password form
  clear() {
    this.erroShow = false;
    this.changePasswordForm.reset()
  }

  // Show or hide PAssword
  pswold: boolean = true
  pswnew: boolean = true
  pswncnfrm: boolean = true
  showHidePAsswordOld() {
    this.pswold = !this.pswold;
  }

  showHidePAsswordNew() {
    this.pswnew = !this.pswnew;
  }

  showHidePAsswordCnfrm() {
    this.pswncnfrm = !this.pswncnfrm;
  }
  canDeactivate() {
    if (this.changePasswordForm.dirty && this.canDeactivates) {
      var confirmMsg = confirm('Do you want to discard changes ?');
      if (confirmMsg) {
        return true;
      } else {
        return false;
      }
    }
    return true
  }
  onSubmit() {
    if (this.changePasswordForm.invalid) {
      this.changePasswordForm.controls['password'].markAsTouched();
      this.changePasswordForm.controls['oldPassword'].markAsTouched();
      this.changePasswordForm.controls['cnfrmpassword'].markAsTouched();
    }
    else {
      if (this.changePasswordForm.value.password != this.changePasswordForm.value.cnfrmpassword) { }
      else {
        swal({
          text: "Are you sure you want to submit?",
          icon: "warning",
          closeOnClickOutside: false,
          buttons: ['Yes', 'No'],
          dangerMode: true,
        }).then((willSubmit) => {
          if (!willSubmit) {
            this.canDeactivates = false;
            this.erroShow = false;
            this.changePasswordFormValue = this.changePasswordForm.value;
            console.log(this.changePasswordFormValue);
            this.apiServices.changePassword(this.changePasswordForm.value).subscribe((res) => {
              console.log(res);
              swal({
                title: "Password changed successfully !!",
                text: 'redirecting',
                icon: "success",
                buttons: [false],
                timer: 1000
              }).then((willDelete) => {
                if (!willDelete) {
                  this.route.navigate(['/home'])
                }
              })
            }
            )
          }
        })
      }
    }
  }

}
