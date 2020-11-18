import { AuthService } from 'src/app/services/auth/auth.service';
import { Component, OnInit } from '@angular/core';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  authorityObj: any = [];
  organization: any;
  roles;
  authority: any = [];
  loader;
  profileData: any
  constructor(private authservices: AuthService) { }
  ngOnInit() {
    this.loader = true;
    this.authservices.loogedInUser().subscribe(res => {
      this.authorityObj = res;
      console.log(this.authorityObj);
      this.roles = this.authorityObj.roles;
      this.authority = this.authorityObj.authorities;
      this.loader = false;
    })
  }
}
