import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MapServiceService {

  constructor(private https: HttpClient) { }
  getAddress(lat: any, lon: any) {
    return this.https.get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
      lat +
      "," +
      lon +
      "&key=AIzaSyCaNwEauxusdcGJMGNvzcRdCSVo9zBWt-M")

  }
}
