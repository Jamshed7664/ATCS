import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/services/api/api.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Component, OnInit } from '@angular/core';
import swal from 'sweetalert';

@Component({
  selector: 'app-location',
  templateUrl: './location.component.html',
  styleUrls: ['./location.component.scss']
})
export class LocationComponent {
  locatiosCopy: any[] = [];
  myForm: FormGroup;
  loader: boolean = true;
  givenAuthorities: any = [];
  authorities: any;
  constructor(private authService: AuthService, private apiService: ApiService, private route: Router) { }
  allLocations: any[] = [];
  ngOnInit() {
    this.getAllLocations();
    this.myForm = new FormGroup({
      'search': new FormControl()
    })
    this.getAllAuthorities()
  }

  search = (key) => {
    console.log(key);

    if (key) {
      this.allLocations = this.locatiosCopy.filter(
        (item) =>
          item.name.toLowerCase().indexOf(key) > -1 ||
          item.locationTypeName.toLowerCase().indexOf(key) > -1 ||
          item.latitude.toLowerCase().indexOf(key) > -1 ||
          item.longitude.toLowerCase().indexOf(key) > -1 ||
          item.address.toLowerCase().indexOf(key) > -1
      )
    } else {
      this.allLocations = JSON.parse(JSON.stringify(this.locatiosCopy));
    }

  }

  getAllAuthorities() {
    let userInfo = JSON.parse(window.localStorage.getItem('userInfo'));
    this.authorities = userInfo.authorities;
    this.authorities.forEach(element => {
      this.givenAuthorities.push(element.name)
    });
  }

  getAllLocations() {
    this.authService.getAllLocations().subscribe(res => {
      this.allLocations = res;
      this.locatiosCopy = JSON.parse(JSON.stringify(res));
      this.loader = false;
    })
  }

  updateLocationValue;
  updateLocation(id: any) {
    this.route.navigate(['/home/locations/' + id])
  }
  deleteLocation(item: any, i) {
    swal({
      text: "Are you sure you want to Delete location?",
      icon: "warning",
      closeOnClickOutside: false,
      buttons: ['Yes', 'No'],
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
      } else {
        this.authService.deleteLocation(item.id).subscribe(res => {
          this.allLocations.splice(i, 1)
          swal({
            text: "Location deleted Successfully !",
            icon: "success",
            closeOnClickOutside: false,
            buttons: [false],
            timer: 1000
          })

        })
      }
    });

  }
}
