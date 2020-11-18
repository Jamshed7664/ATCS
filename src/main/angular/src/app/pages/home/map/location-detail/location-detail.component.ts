import { Component, OnInit } from '@angular/core';
import { LocationService } from '../../../../services/location/location.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-location-detail',
  templateUrl: './location-detail.component.html',
  styleUrls: ['./location-detail.component.scss']
})
export class LocationDetailComponent implements OnInit {

  loader: boolean;
  location: any = {};
  constructor(private _locationService: LocationService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      console.log(params);
      if (params['id']) {
        this.getLocationById(params.id);
      }
    })
  }

  getLocationById(id) {
    this.loader = true;
    this._locationService.getLocationById(id).subscribe(response => {
      this.location = response;
      this.loader = false;
    })
  }

  editLocation(id: any) {
    this.router.navigate(['/home/locations/' + id])
  }

  mapEquipments(id) {
    this.router.navigateByUrl('home/location/' + id + '/equipment-mapping')
  }

}
