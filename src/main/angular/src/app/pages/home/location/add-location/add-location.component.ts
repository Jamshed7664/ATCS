import { MapServiceService } from './../../../../services/mapService/map-service.service';
import { Router } from '@angular/router';
import { AuthService } from './../../../../services/auth/auth.service';
import { Component, OnInit, TemplateRef, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder, FormArray } from '@angular/forms';
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
  selector: 'app-add-location',
  templateUrl: './add-location.component.html',
  styleUrls: ['./../location.component.scss', './add-location.component.scss']
})
export class AddLocationComponent implements OnInit, AfterViewInit {
  marker: any;
  bounds: any[][];
  centerLoc: { lat: any; lng: any; };
  map: any;
  @ViewChild('mapRef') mapRef: ElementRef;

  selectedLocationType: any;
  locationType: any;
  submit: boolean = false;
  form: FormGroup;
  locationtypeAttribute: any;
  locationAttribute: any;
  vehicalTypes: any;
  directions;
  address;
  latitude;
  canDeactivates: boolean = true;
  constructor(private fb: FormBuilder, private route: Router, private mapService: MapServiceService,
    private authService: AuthService) {

  }

  ngOnInit() {
    this.form = this.fb.group({
      locationTypeId: [null, Validators.required],
      code: [""]
    });
    this.getLocation();
    this.getDirection();
    this.getVehicalType();

  }

  ngAfterViewInit() {
    this.getCenterLatLng();
    $('#exampleModalCenter').on('shown.bs.modal', () => {
      this.map.invalidateSize();
    });
  }

  getLocation() {
    this.authService.getLocationType().subscribe(res => {
      this.locationType = res;
    })
  }

  getVehicalType() {
    this.authService.getVehicalType().subscribe(res => {
      this.vehicalTypes = res;
    })
  }
  // get direction
  getDirection() {
    this.authService.getDirection().subscribe(res => {
      this.directions = res;
      console.log(this.directions);
    });
  }
  // create Arms
  createArms() {
    let fg = this.fb.group({
      'id': (null),
      'direction': [null, Validators.required],
      'lanes': this.fb.array([]),
    });
    return fg;
  }
  // create Lane
  createLane() {
    let fg = this.fb.group({
      'id': (null),
      'directionIds': [null, Validators.required],
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


  // myValue;
  addArms(count) {
    let currentVal = (<FormArray>this.form.controls['arms']).length;
    if (currentVal < count) {
      let countOfArms = count - currentVal
      const a = <FormArray>this.form.controls['arms'];
      for (let i = 0; i < countOfArms; i++) {
        a.push(this.createArms())
      }
    } else {
      const a = <FormArray>this.form.controls['arms'];
      for (let i = currentVal - count; i > 0; i--) {
        a.removeAt(i)
      }
    }
  }


  onChangeLocationType(locationTypeId) {
    this.selectedLocationType = this.locationType.find(l => l.id === locationTypeId);
    if (this.selectedLocationType && this.selectedLocationType.name == "Junction") {
      this.form.removeControl('arms');
      this.form.addControl('arms', this.fb.array([]));
      this.addArms(3);
    } else if (this.selectedLocationType && this.selectedLocationType.name == "Highway") {
      this.form.addControl('arms', this.fb.array([]));
      this.addArms(2);
    }
    else {
      if (this.form.contains('arms')) {
        this.form.removeControl('arms');
      }
    }
    if (this.selectedLocationType)
      this.addAttributes(this.selectedLocationType.locationTypeAttributes);

  }

  // create attribute ;
  createAttribute(attribute?: any) {
    const attr = this.fb.group({
      'name': [],
      'htmlInputType': [],
      'locationTypeAttributeId': (null),
      'value': ['', Validators.required]
    })
    if (attribute) {
      attr.patchValue({
        'htmlInputType': attribute.htmlInputType,
        'name': attribute.name,
        'locationTypeAttributeId': attribute.id,
        'value': (attribute.htmlInputType == 'checkbox') ? attribute.value == true : attribute.value
      });
      if (attribute.name === 'Latitude' || attribute.name === 'Longitude') {
        attr.controls['value'].setValidators([
          Validators.required,
          Validators.maxLength(32),
          Validators.min(-90),
          Validators.max(90),
          Validators.pattern(/\-?\d*\.?\d{1,2}/)])
      }
      if (attribute.dataType === 'Boolean') {
        attr.controls['value'].patchValue(false);
      }
      if (attribute.options) {
        attr.addControl('options', this.fb.array(attribute.options));
      }

    }
    return attr;
  }

  // create Attribute
  addAttributes(locationTypeAttributes) {
    this.form.removeControl('attributeValues');
    if (locationTypeAttributes) {
      this.form.addControl('attributeValues', new FormArray([]));
      let attributeValues = <FormArray>this.form.controls['attributeValues'];
      locationTypeAttributes.forEach(element => {
        attributeValues.push(this.createAttribute(element));
      });
    }
  }

  getLatLng() {
    const attributeValues = <FormArray>this.form.controls['attributeValues'];
    const latitude = attributeValues.controls.find(c => c.value.name == 'Latitude')
    const longitude = attributeValues.controls.find(c => c.value.name == 'Longitude')
    const address = attributeValues.controls.find(c => c.value.name == 'Address')
    if (latitude.value.value && longitude.value.value) {

      this.mapService.getAddress(latitude.value.value, longitude.value.value).subscribe((res: any) => {
        if (res.results.length) {
          address.patchValue({ value: res.results[0].formatted_address });
        }
      }, err => {
        console.log('err is', err);
        swal('Error', 'you have entered wrong Latitude/Longitude values', 'warning')
      })
    } else {
      address.patchValue({ value: null });
    }
  }

  onReset() {
    this.onChangeLocationType(null);
    this.addAttributes(null)
    this.form.reset();
  }

  canDeactivate() {
    if (this.form.dirty && this.canDeactivates) {
      var confirmMsg = confirm('Do you want to discard changes ?');
      if (confirmMsg) {
        return true;
      } else {
        return false;
      }
    }
    return true
  }

  onSubmit() {
    console.log(this.form.value);
    if (this.form.invalid) {
      const attributeValues = <FormArray>this.form.controls['attributeValues'];
      attributeValues.controls.forEach(c => {
        c.markAsTouched();
        if (!c.value.value) {
          swal('Error !!', c.value.name + ' must not be empty ?', 'warning');
          return;
        }
      });
      const a = <FormArray>this.form.controls['arms'];
      if (a) {
        a.controls.forEach(e => {
          console.log(e.value.lanes.length);
          if (e.value.lanes.length) {
            for (let index = 0; index < e.value.lanes.length; index++) {
              if ((!e.value.lanes[index].directionIds)) {
                swal('Error !!', 'Lane must not be empty ?', 'warning')
              }
            }
          }
          if (!e.value.direction) {
            swal('Error !!', 'Arm direction must not be empty ?', 'warning')
          }
        })
      }
      this.submit = false;
    } else {
      this.canDeactivates = false
      this.submit = true;
      this.authService.saveLocation(this.form.value).subscribe(res => {
        this.submit = false;
        swal({
          title: 'Successful',
          text: 'Location created successfully',
          timer: 500,
          icon: 'success',
          buttons: [false]
        })
        this.route.navigate(['home/locations']);
      }, error => {
        this.submit = false;
      })
    }
  }

  setLatLng() {
    $('#exampleModalCenter').modal('hide')
    const attributeValues = <FormArray>this.form.controls['attributeValues'];
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
      .addTo(this.map)
      // .bindPopup('Current location.<br> Easily customizable.')
      // .openPopup()
      .dragging.enable();
    this.map.fitBounds(bounds);
    this.map.setMaxBounds(bounds);
  }

  initMapPreview() {
    const attributeValues = <FormArray>this.form.controls['attributeValues'];
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
