import { ApiService } from './../../services/api/api.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import swal from 'sweetalert';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  clients: any[] = [];
  loginForm: FormGroup;
  forgetPasswordForm: FormGroup;
  GenratePasswordForm: FormGroup;
  logging = false;
  errorMsgForUserField = '';
  errorMsgForPasswordField = '';
  errorMsg;
  errMessagePasswordForget;
  errShow;
  genrateOtpMsg;
  genrateOtperror = false;
  hideForm = false;
  logIn = false;
  msg: any;
  constructor(
    private fb: FormBuilder,
    private fp: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private apiServices: ApiService,
    private route: Router) { }

  ngOnInit() {
    this.authService.getClients().subscribe(response => {
      this.clients = response;
      this.loginForm.controls['client'].patchValue(response[0]);
      this.forgetPasswordForm.controls['client'].patchValue(response[0]);

    });
    this.createForm();
    this.generateOTP();
    this.changePassword();
  }

  /**getter for easier access to form controls in html */
  get username() { return this.loginForm.get('username'); }
  get password() { return this.loginForm.get('password'); }

  /**creates an instance of @class FormGroup */
  createForm = () => {
    this.loginForm = new FormGroup({
      'client': new FormControl(this.clients[0], Validators.required),
      'username': new FormControl(null, Validators.required),
      'password': new FormControl(null, Validators.required),
    })
  }

  // Create forget password Form
  generateOTP = () => {
    this.forgetPasswordForm = new FormGroup({
      'fpuser': new FormControl('', Validators.required),
      'client': new FormControl(this.clients[0], Validators.required)
    })
  }

  changePassword() {
    this.GenratePasswordForm = new FormGroup({
      'username': new FormControl(null, Validators.required),
      'generatedPassword': new FormControl(null, Validators.required),
      'password': new FormControl(null, [Validators.required]),
      'client': new FormControl(null, [Validators.required])
    })
  }

  // For hide and Show form during Forget password
  forgetPassword() {
    this.logIn = !this.logIn;
    // this.forgetPasswordForm.reset();
    this.errShow = false;
    // this.loginForm.reset();

  }
  // For hide and Show form during Forget password second Form
  forgetPassword2() {
    this.logIn = true;
    this.hideForm = false;
    this.GenratePasswordForm.reset();
    this.genrateOtperror = false;
  }

  /**called on click of form submit button*/
  onSubmit() {

    if (!this.loginForm.valid) {
      this.loginForm.controls['username'].markAsTouched();
      this.loginForm.controls['password'].markAsTouched();
    } else {

      this.login();
    }
  }

  onForgetPassword() {
    this.errShow = false;
    if (!this.forgetPasswordForm.valid) {
      this.forgetPasswordForm.controls['fpuser'].markAsTouched()
    }
    else {
      this.GenratePasswordForm.patchValue({
        username: this.forgetPasswordForm.value.fpuser
      })
      const userName = this.forgetPasswordForm.value.fpuser;
      this.apiServices.getGeneratedPassword(this.forgetPasswordForm.value).subscribe(res => {
        if (res != null) {
          swal("Sent!", "OTP has been sent to your registered mail", "success");
          this.hideShowForm()
        }
      }, err => {
        this.errShow = true;
        this.errMessagePasswordForget = err.error.message;
        swal('warning', this.errMessagePasswordForget, 'warning')

      })
    }
  }

  onGeneratePAssword() {
    this.GenratePasswordForm.controls['client'].patchValue(this.forgetPasswordForm.value.client);
    if (!this.GenratePasswordForm.valid) {
      this.GenratePasswordForm.controls['username'].markAsTouched();
      this.GenratePasswordForm.controls['generatedPassword'].markAsTouched();
      this.GenratePasswordForm.controls['password'].markAsTouched();
    }
    else {
      this.apiServices.forgotPassword(this.GenratePasswordForm.value).subscribe(res => {
        this.msg = res
        swal("Good Job!!", this.msg.message, "success");
        this.GenratePasswordForm.reset();
        this.forgetPassword();
      }, (err: any) => {
        this.logging = false;
        // this.genrateOtperror=true;
        this.genrateOtpMsg = err.error.message;
        swal('warning', this.genrateOtpMsg, 'warning')

      });
    }
  }

  showError(msg?: string) {
    if (msg) { this.errorMsgForUserField = msg; return; }
    if (this.loginForm.controls.username.invalid && this.loginForm.controls.password.invalid) {
      this.errorMsgForUserField = 'Please enter username';
      this.errorMsgForPasswordField = 'Please enter password';
      return;
    }
    if (this.loginForm.controls.username.invalid) {
      this.errorMsgForUserField = 'Please enter username';
      return;
    }
    if (this.loginForm.controls.password.invalid) {
      this.errorMsgForPasswordField = 'Please enter password';
      return;
    }
  }
  // Login 
  login() {
    this.logging = true;
    this.errorMsg = false;
    this.errorMsg = '';
    this.authService.login(this.loginForm.value)
      .subscribe((res: any) => {
        this.authService.saveToken(res.access_token);
        this.authService.fetchUserDetails(true)
          .subscribe((resp: any) => {
            this.authService.saveUserDetails(resp);
            this.navigate();
          }, (err: any) => {
            localStorage.clear();
            this.logging = false;
            this.showError(err.msg);
          });
      }, (err: any) => {
        this.logging = false;
        if (err.error.message === "Unauthorized") {
          swal('warning', "User does not exists", 'warning')
        }
        else if (err.error.status == 400) {
          swal('warning', "You have entered invalid Password", 'warning')
        }
      });
  }

  navigate() {
    swal({
      title: 'Login Success',
      text: 'Redirecting...',
      icon: 'success',
      timer: 500,
      buttons: [false],
    }).then(() => {
      this.router.navigate(['/home']);
    })
  }
  // hide show login form
  hideShowForm() {
    this.hideForm = !this.hideForm
  }
}
