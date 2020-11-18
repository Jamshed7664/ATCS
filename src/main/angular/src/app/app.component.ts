import { ApiService } from 'src/app/services/api/api.service';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  loggedInUserValue: any;
  log: any;
  authorityName = [];
  Access_token;
  title = 'enforcement-configurator';
  constructor(private authServices: AuthService, private myServices: ApiService, private route: Router) {
    if (!window.localStorage.getItem('access_token')) {
      this.route.navigate(['/login'])
    }
  }

  ngOnInit() {

  }


}
