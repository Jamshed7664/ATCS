import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  constructor(private api: ApiService) { }

  getLocations() {
    return this.api.get('api/locations');
  }

  getLocationById(locationId) {
    return this.api.get(`api/location/${locationId}`)
  }


}
