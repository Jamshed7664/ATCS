import { ApiService } from 'src/app/services/api/api.service';
import { Router } from '@angular/router';
import { AuthService } from './../../services/auth/auth.service';
import { Component, OnInit, AfterViewInit, NgModule } from '@angular/core';
import swal from 'sweetalert';
declare let $: any;
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, AfterViewInit {
  currentUserName: any;
  authorities: any;
  givenAuthorities = [];
  curentUser = []
  downArrow = true
  myMenu: boolean = true;
  constructor(
    private autServices: AuthService,
    private myservices: ApiService,
    private router: Router) { }
  ngOnInit() {
    let userInfo = JSON.parse(window.localStorage.getItem('userInfo'));
    var username = userInfo.name;
    this.authorities = userInfo.authorities;

    this.currentUserName = username; //to show profile name
    this.authorities.forEach(element => {
      this.givenAuthorities.push(element.name)
    });
    this.getCurrentUserAuthorities()
  }

  // get Authority to hide and show functionality
  getCurrentUserAuthorities() {
    this.curentUser = this.myservices.curentUserAuth
  }
  ngAfterViewInit() {
    $("#menu-toggle").click(function (e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });
  }
  // Open/Close Menu
  menu() {
    this.myMenu = !this.myMenu
    console.log(this.givenAuthorities);
  }
  // logout
  logout() {
    swal({
      title :'Are you sure?',
      text: "Do you want to logout?",
      icon: "warning",
      closeOnClickOutside: false,
      buttons: ['Yes', 'No'],
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
      } else {
        this.autServices.logout().subscribe(res => {
          window.localStorage.clear();
          swal({
            title: 'Logged out Successfully!!',
            text: 'Redirecting...',
            icon: 'success',
            timer: 2000,
            buttons: [false],
          }).then(() => {
            this.router.navigate(['/login']);
          })
        })

      }
    });

  }
  clearModel() {
    this.myservices.resetMyFormVAlue()
  }
  arrowPosition() {
    this.downArrow = !this.downArrow
  }
}
