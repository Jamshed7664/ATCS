import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { MAPBOX_TOKEN } from '../../../services/app.constant';
import { LocationService } from '../../../services/location/location.service';
import { Router, RouterEvent, ActivatedRoute } from '@angular/router';
const MARKER_URLS = {
  JUN: 'assets/svg/junction.svg',
  CHN: 'assets/svg/highway.svg',
  DTC: 'assets/svg/datacentre.svg',
  CTR: 'assets/svg/control_room.svg'
}
/**For accessing global leaflet.js variable */
declare const L: any;
@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
  selectedLocationId: string;
  selectedMarker: any;
  bounds: any[][];
  centerLoc: { lat: any; lng: any; };
  locationsMarkers: Array<any>;
  locations: any[];
  /**To access the native html  div element which hosts map */
  @ViewChild('map') mapRef: ElementRef;
  /**To access the native html  div element which hosts add loction control group */
  @ViewChild('bottomRightControl') bottomRightControl: ElementRef;
  @ViewChild('topRightControl') topRightControl: ElementRef;
  @ViewChild('satellite') satellite: ElementRef;

  /**Stores map object containing various info about map*/
  map: any;
  /**Holds classes for adding control center: null,*/
  customControlsClass = {
    center: null,
    right: null,
    top: null
  };

  /**Holds the instances created using customControlClass */
  customControls = {
    center: null,
    right: null,
    top: null
  };

  constructor(private _locationService: LocationService,
    private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    if (this.route.children.length)
      this.route.firstChild.params.subscribe(params => {
        if (params.id)
          this.selectedLocationId = params.id;
      })
    this.router.events.subscribe((event: RouterEvent) => {
      if (event.url == '/home/map') {
        this.removeHightlightClasses(document.querySelectorAll('.highlightJunction'))
        // this.selectedMarker.setIcon(this.giveIcon(this.giveIconUrl(this.selectedMarker), this.selectedMarker.name, this.selectedMarker))
      }
    })
    this.getCenterLatLng();
    this.initMap();
    this.getLocations();
  }

  getLocations(): any {
    this._locationService.getLocations().subscribe((response: any[]) => {
      this.locations = response;
      this.addLocationsOnMap(response);
    })
  }

  /**Creates Map instance initialized with given location and settings */
  initMap() {
    // The center location of map(varanasi)
    // const data = require('../../../assets/config.json');
    const centerLoc = this.centerLoc;
    const bounds = this.bounds;
    this.map = L.map(this.mapRef.nativeElement, {
      center: centerLoc,
      zoom: 13,
      zoomControl: false
    });

    L.tileLayer(
      `https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=${MAPBOX_TOKEN}`,
      {
        attribution:
          // tslint:disable-next-line: max-line-length
          'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        maxZoom: 18,
        minZoom: 12,
        // id: 'mapbox.streets',
        // id:'mapbox.outdoors'
        id: 'mapbox.light'
      }
    ).addTo(this.map);

    L.control.zoom({
      position: 'bottomright'
    }).addTo(this.map);

    this.map.fitBounds(bounds);
    this.map.setMaxBounds(bounds);
    this.intializeControls();
    this.addTopControlsOnMap();
    // if (this.authService.hasJunctionWritePermission()) {
    //   this.addRightControlsOnMap();
    // }
    // center controls to be added after fetching loction list
  }

  /**Creates classes for adding controls on map */
  intializeControls() {

    this.customControlsClass.right = L.Control.extend({
      options: {
        position: 'bottomright'
        // other position options : 'topleft', 'topright', 'bottomleft', 'bottomright'
      },
      onAdd: () => {
        return this.bottomRightControl.nativeElement;
      },
      onRemove: () => {
        /** do nothing*/
      }
    });

    this.customControlsClass.top = L.Control.extend({
      options: {
        position: 'topright'
        // other position options : 'topleft', 'topright', 'bottomleft', 'bottomright'
      },
      onAdd: () => {
        return this.topRightControl.nativeElement;
      },
      onRemove: () => {
        /** do nothing*/
      }
    });
  }

  /**Add controls for adding locitions on map by creating instance of @class customControlsClass.right*/
  addRightControlsOnMap() {
    this.customControls.right = new this.customControlsClass.right();
    this.map.addControl(this.customControls.right);
  }

  /**Add controls for help menu on map by creating instance of @class customControlsClass.top*/
  addTopControlsOnMap() {
    this.customControls.top = new this.customControlsClass.top();
    this.map.addControl(this.customControls.top);
  }

  /**Displays and stores locations' data on map */
  addLocationsOnMap(locations: Array<any>) {
    this.locationsMarkers = locations.map(loc => {
      const marker = this.addMarker(
        { lat: loc.latitude, lng: loc.longitude }
        , this.giveIcon(this.giveIconUrl(loc.locationTypeCode), loc.name, loc)
      );

      // BELOW CODE IS REQUIRED EVERY TIME AFTER ADDING ANY NEW MARKER. IT IS REQUIRED TO CORRECT POPUP ANCHOR OF MARKER SO THAT
      // MARKER'S POPUP WINDOW OPENS EXACTLY AT TOP OF ICON
      // setTimeout(() => {
      //   marker.setIcon(this.giveIcon(this.giveIconUrl(loc.status), loc.name, loc));
      // }, 100);

      marker.info = {
        name: loc.name,
        lat: loc.latitude,
        lng: loc.longitude,
        id: loc.id,
        type: loc.type,
      };
      if (this.selectedLocationId == loc.id)
        this.selectedMarker = marker;
      return marker;
    });
    this.locationsMarkers.forEach(m => {
      this.attachClickEventToMarker(m);
      this.setMarkerTooltip(m);
    });
  }

  /**Opens the pop-up on marker's click and sets its content */
  attachClickEventToMarker(marker: any) {

    marker.addEventListener('click', e => {
      this.removeHightlightClasses(document.querySelectorAll('.highlightJunction'));
      const d = document.getElementById(marker.info.id.toString());
      if (d) {
        d.classList.add('highlightJunction');
      } else {
        const de = document.getElementById(marker.info.name);
        de.classList.add('highlightJunction');
      }

      //marker.setIcon(this.giveIcon(this.getHighLightIconUrl(), marker.info.name, marker.info))
      this.selectedMarker = marker;
      this.router.navigateByUrl('home/map/location/' + marker.info.id)
    });
  }

  removeHightlightClasses(ele) {
    ele.forEach((element) => element.classList.remove('highlightJunction'));
  }

  /**Returns a new Marker instance to add on map at given position and given icon*/
  addMarker(pos: any, icon?: any, draggable?: boolean) {
    return L.marker(pos, {
      // marker options here
      icon: icon,
      draggable: draggable
    }).addTo(this.map);
  }

  /**returns an marker icon instance*/
  giveIcon(iconUrl: string, locationName: String, location?: any) {
    const locIdId = location && location.id;
    const html = `
    <div class="junction_showcase${(this.selectedLocationId == location.id) ? ' highlightJunction' : ''}" id="${location.id ? location.id : location.name}">
    <img  src="${iconUrl}"/>
    </div>
    `;

    // set popup anchor based on the width of the popup
    // document.getElementById(locIdId) will be available when this method runs second time for 
    // same location after 100ms time
    // This has been done in order to set the correct popup anchor which depends on width of popup and
    // width of popup is only available at second run of this method

    return L.divIcon({
      html: html,
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [0, -34],
      tooltipAnchor: [16, -28]
    });

  }

  giveIconUrl(locationTypeCode: string) {
    return MARKER_URLS[locationTypeCode];
  }

  getHighLightIconUrl() {
    return 'assets/svg/highlight_green_pin.svg'
  }

  /**Sets the junction's tooltip by checking the given marker's status*/
  setMarkerTooltip(marker: any) {
    marker.bindTooltip('Click to see info');
  }

  getCenterLatLng() {
    const organization = JSON.parse(localStorage.getItem('userInfo')).organization;
    this.centerLoc = { lat: organization.centreLatitude, lng: organization.centreLongitude }
    this.bounds = [
      [organization.pointA, organization.pointB],
      [organization.pointC, organization.pointD]
    ]
  }

}
