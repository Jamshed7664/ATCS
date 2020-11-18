import { MapServiceService } from './../../../../services/mapService/map-service.service';
import { RoleService } from 'src/app/services/api/role.service';
import { CanActivate, ActivatedRoute, Router } from '@angular/router';
import { ApiService } from 'src/app/services/api/api.service';
import { FormGroup, FormBuilder, FormArray, FormControl, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { Location } from '@angular/common';
import swal from 'sweetalert';
import { MAPBOX_TOKEN } from '../../../../services/app.constant';

declare const L: any;
declare let $: any;
const MARKER_URLS = {
  JUN: 'assets/svg/junction.svg',
  CHN: 'assets/svg/highway.svg',
  DTC: 'assets/svg/datacentre.svg',
  CTR: 'assets/svg/control_room.svg'
}

@Component({
  selector: 'app-update-location',
  templateUrl: './update-location.component.html',
  styleUrls: ['./../location.component.scss', './update-location.component.scss']
})
export class UpdateLocationComponent implements OnInit, AfterViewInit {

  marker: any;
  bounds: any[][];
  centerLoc: { lat: any; lng: any; };
  map: any;
  @ViewChild('mapRef') mapRef: ElementRef;

  selectedLocationType: any;
  locationType;
  updateLocationForm: FormGroup;
  directions: any;
  id: any;
  readOnly = true;
  update: boolean
  loader = false;
  constructor(private authService: AuthService, private mapService: MapServiceService,
    private fb: FormBuilder,
    private apiService: ApiService,
    private router: Router,
    private _location: Location,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.updateLocationForm = this.fb.group({
      locationTypeId: [{ value: null, disabled: true }, Validators.required],
      code: [""]
    });
    this.queryParms()
    this.getDirection();
    this.getLocation();
  }

  ngAfterViewInit() {
    this.getCenterLatLng();
    $('#exampleModalCenter').on('shown.bs.modal', () => {
      this.map.invalidateSize();
    });
  }

  queryParms() {
    this.route.params.subscribe((params: any) => {
      this.id = params.id;
      this.loader = true;
      this.authService.getLocationById(params.id).subscribe(res => {
        this.selectedLocationType = res;
        this.loader = false;
        this.updateLocationForm.controls['locationTypeId'].patchValue(res.locationTypeId);
        if (res.attributeValues) {
          this.addAttributes(res.attributeValues);
          console.log(this.updateLocationForm.value);

        }
        if (res.arms && res.arms.length) {
          this.updateLocationForm.addControl('arms', this.fb.array([]));
          this.addArms(res.arms.length, res.arms)
        }
      })
    });
  }

  editable() {
    if (this.readOnly) {
      if (this.selectedLocationType.attributeValues) {
        this.addAttributes(this.selectedLocationType.attributeValues);
      }
      if (this.selectedLocationType.arms && this.selectedLocationType.arms.length) {
        this.updateLocationForm.removeControl('arms');
        this.updateLocationForm.addControl('arms', this.fb.array([]));
        this.addArms(this.selectedLocationType.arms.length, this.selectedLocationType.arms)
      }
    }
  }
  addArms(count, arms?: any) {
    let currentVal = (<FormArray>this.updateLocationForm.controls['arms']).length;
    if (currentVal < count) {
      let countOfArms = count - currentVal
      const a = <FormArray>this.updateLocationForm.controls['arms'];
      for (let i = 0; i < countOfArms; i++) {
        a.push(this.createArms(arms[i]))
      }
    } else {
      const a = <FormArray>this.updateLocationForm.controls['arms'];
      for (let i = currentVal - count; i > 0; i--) {
        a.removeAt(i)
      }
    }
  }

  // create Arms
  createArms(arm) {
    let fg = this.fb.group({
      'id': arm.id,
      'direction': [arm.direction, Validators.required],
      'lanes': this.fb.array([])
    });
    const lanes = <FormArray>fg.controls['lanes'];
    arm.lanes.forEach(lane => {
      lanes.push(this.createLane(lane))
    });
    return fg;
  }

  // Add Lane from dropdown
  addLanes(count: number, arm: FormGroup) {
    let lanes = <FormArray>arm.controls['lanes'];
    if (lanes.length < count) {
      for (let i = lanes.length; i < count; i++) {
        lanes.push(this.createLane())
      }
    } else {
      for (let i = lanes.length; i >= count; i--) {
        lanes.removeAt(i)
      }
    }
  }
  // create Lane
  createLane(lane?: any) {
    let fg = this.fb.group({
      'id': (null),
      'directions': [],
      'directionIds': [null, Validators.required]
    });
    if (lane) {
      fg.patchValue(lane);
      const directionIds = []
      if (lane.directions)
        lane.directions.forEach(d => {
          directionIds.push(d.id);
        });
      fg.controls['directionIds'].patchValue(directionIds);
    }
    return fg;
  }

  // get direction
  getDirection() {
    this.authService.getDirection().subscribe(res => {
      this.directions = res;
    });
  }

  getLocation() {
    this.authService.getLocationType().subscribe(res => {
      this.locationType = res;
    })
  }


  createAttribute(attribute?: any) {
    const attr = this.fb.group({
      'name': [],
      'htmlInputType': [],
      'locationTypeAttributeId': (null),
      'value': ['', Validators.required]
    })
    if (attribute) {
      attr.patchValue({
        'htmlInputType': attribute.locationTypeAttributeResponse.htmlInputType,
        'name': attribute.locationTypeAttributeResponse.name,
        'locationTypeAttributeId': attribute.locationTypeAttributeResponse.id,
      });
      if (attribute.locationTypeAttributeResponse.htmlInputType == 'checkbox') {
        attr.controls['value'].patchValue(attribute.value == 'true')
      } else {
        attr.controls['value'].patchValue(attribute.value)
      }
      if (attribute.locationTypeAttributeResponse.name.name === 'Latitude' || attribute.locationTypeAttributeResponse.name.name === 'Longitude') {
        attr.controls['value'].setValidators([
          Validators.required,
          Validators.maxLength(32),
          Validators.min(-90),
          Validators.max(90),
          Validators.pattern(/\-?\d*\.?\d{1,2}/)])
      }
      if (attribute.locationTypeAttributeResponse.options) {
        attr.addControl('options', this.fb.array(attribute.locationTypeAttributeResponse.options));
      }
    }
    return attr;
  }

  // create Attribute
  addAttributes(locationTypeAttributes) {
    this.updateLocationForm.removeControl('attributeValues');
    this.updateLocationForm.addControl('attributeValues', new FormArray([]));
    let attributeValues = <FormArray>this.updateLocationForm.controls['attributeValues'];
    locationTypeAttributes.forEach(element => {
      attributeValues.push(this.createAttribute(element));
    });
  }
  canDeactivates: boolean = true;
  canDeactivate() {
    if (this.updateLocationForm.dirty && this.canDeactivates) {
      var confirmMsg = confirm('Do you want to discard changes ?');
      if (confirmMsg) {
        return true;
      } else {
        return false;
      }
    }
    return true
  }

  getLatLng() {
    const attributeValues = <FormArray>this.updateLocationForm.controls['attributeValues'];
    const latitude = attributeValues.controls.find(c => c.value.name == 'Latitude')
    const longitude = attributeValues.controls.find(c => c.value.name == 'Longitude')
    const address = attributeValues.controls.find(c => c.value.name == 'Address')
    if (latitude.value.value && longitude.value.value) {
      this.mapService.getAddress(latitude.value.value, longitude.value.value).subscribe((res: any) => {
        address.patchValue({ value: res.results[0].formatted_address });
      }, err => {
        swal('Error', 'You have entered wrong Latitude/Longitude Value', 'warning')
      })
    } else {
      address.patchValue({ value: null });
    }
  }

  attributeVals: any
  onSubmit() {
    if (this.updateLocationForm.invalid) {
      const attributeValues = <FormArray>this.updateLocationForm.controls['attributeValues'];
      attributeValues.controls.forEach(c => {
        console.log(c.value.value);
        c.markAsTouched();
        if (!c.value.value) {
          this.attributeVals = c.value.name
          swal('Error !!', this.attributeVals + ' must not be empty ?', 'warning')
        }
      });
      const a = <FormArray>this.updateLocationForm.controls['arms'];
      a.controls.forEach(e => {
        for (let index = 0; index < e.value.lanes.length; index++) {
          if ((e.value.lanes[index].directionIds).length == 0) {
            swal('Error !!', 'Lane must not be empty ?', 'warning')
          }
        }
        if (!e.value.direction) {
          swal('Error !!', 'Arm direction must not be empty ?', 'warning')
        }
      })
    } else {
      this.canDeactivates = false;
      this.update = false;
      swal({
        text: "Are you sure you want to Update?",
        icon: "warning",
        closeOnClickOutside: false,
        buttons: ['Yes', 'No'],
        dangerMode: true,
      }).then((willDelete) => {
        if (willDelete) {
        } else {
          this.update = true;

          this.authService.updateLocation(this.id, this.updateLocationForm.value).subscribe(res => {
            this.update = false;
            swal({
              title: 'Location updated Successfully!!',
              text: 'Redirecting...',
              icon: 'success',
              timer: 2000,
              buttons: [false],
            }).then(() => {
              this.router.navigate(['home/locations']);
            })
          }, err => {
            this.update = false; if (err) {
              // this.router.navigate(['home/location/' + this.id])
            } else {
              this.update = false;
            }
          });
        }
      });
    }
  }

  back() {
    this._location.back();
  }

  setLatLng() {
    $('#exampleModalCenter').modal('hide')
    const attributeValues = <FormArray>this.updateLocationForm.controls['attributeValues'];
    const latitude = attributeValues.controls.find(c => c.value.name == 'Latitude');
    const longitude = attributeValues.controls.find(c => c.value.name == 'Longitude');
    const pos = this.marker._marker._latlng
    latitude.patchValue({ value: pos.lat })
    longitude.patchValue({ value: pos.lng })
    this.getLatLng();

  }

  getCenterLatLng() {
    const organization = JSON.parse(localStorage.getItem('userInfo')).organization;
    this.centerLoc = { lat: organization.centreLatitude, lng: organization.centreLongitude }
    this.bounds = [
      [organization.pointA, organization.pointB],
      [organization.pointC, organization.pointD]
    ]
    const centerLoc = this.centerLoc;
    const bounds = this.bounds;
    this.map = L.map(this.mapRef.nativeElement, {
      center: centerLoc,
    }).setView([centerLoc.lat, centerLoc.lng], 19);
    L.tileLayer(`https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=${MAPBOX_TOKEN}`, {
      attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
      id: 'mapbox.light'
    }).addTo(this.map);
    this.marker = L.marker([centerLoc.lat, centerLoc.lng])
      .setIcon(this.giveIcon(this.giveIconUrl('JUN')))
      // .bindPopup('Current location.<br> Easily customizable.')
      // .openPopup()
      .addTo(this.map)
      .dragging.enable();
    this.map.fitBounds(bounds);
    this.map.setMaxBounds(bounds);
  }

  initMapPreview() {
    const attributeValues = <FormArray>this.updateLocationForm.controls['attributeValues'];
    const latitude = attributeValues.controls.find(c => c.value.name == 'Latitude');
    const longitude = attributeValues.controls.find(c => c.value.name == 'Longitude');
    $('#exampleModalCenter').modal('show')

    if (latitude.value.value && longitude.value.value) {

      this.marker._marker.setLatLng([latitude.value.value, longitude.value.value])
      this.map.setView([latitude.value.value, longitude.value.value], 14);
    }

  }

  giveIcon(iconUrl: string, locationName?: String, location?: any) {
    const locIdId = location && location.id;
    const html = `
    <div class="junction_showcase">
    <img  src="${iconUrl}"/>
    </div>
    `;
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
}
