import { Component, OnInit, AfterViewInit } from '@angular/core';
import { RouterEvent, Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { IncidentService } from '../../../../services/incident/incident.service';
import { EquipmentService } from '../../../../services/equipment/equipment.service';
declare let $: any;
@Component({
  selector: 'app-incident-definition',
  templateUrl: './incident-definition.component.html',
  styleUrls: ['./incident-definition.component.scss']
})
export class IncidentDefinitionComponent implements OnInit, AfterViewInit {
  siblingCameras: any[] = [];
  selectedSiblingCamera: any;
  equipmentDetails: any = {};
  selectedIncidentType: any = {};
  incidentForm: FormGroup;
  incidentTypes = [
    { name: 'Define Vehicle Incident Paramter(s)', type: 'VEHICLE_INCIDENT' },
    { name: 'Define Traffic Incident Paramter(s)', type: 'TRAFFIC_INCIDENT' },
    { name: 'Define Other Incident Paramter(s)', type: 'OTHER' },
    { name: 'Define Weather Coordinate(s)', type: 'WEATHER_INCIDENT' }
  ];
  incidents: any[];
  cameraDetails: any;
  cameraId: string;
  constructor(private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private _incidentService: IncidentService,
    private _equipmentService: EquipmentService) { }

  ngOnInit() {
    this.route.params.subscribe(param => {
      this.cameraId = param.id;
      this.getCameraDetails(param.id);
      this.getEquipmentDetails(param.id);

    })
  }

  ngAfterViewInit() {
    $('#myModal').on('hidden.bs.modal', (e) => {
      this.incidentForm.reset()
    })
  }

  getEquipmentDetails(id) {
    this._equipmentService.getEquipmentDetail(id).subscribe(response => {
      this.equipmentDetails = response;
    })
  }

  getCameraDetails(id) {
    this._equipmentService.getEquipmentDetail(id).subscribe(response => {
      this.cameraDetails = response;
      this.getCameraNames(response.equipmentMapping.locationId, id);
    })
  }

  getCameraNames(locationId, cameraId) {
    this._incidentService.getSiblingCameras(locationId, cameraId).subscribe((response: any[]) => {
      this.siblingCameras = response;
    })
  }

  initIncident(incidentType) {
    this.selectedIncidentType = incidentType;
    this._incidentService.getIncidentsByType(this.cameraDetails.equipmentMapping.locationId, this.cameraId, incidentType.type).subscribe((response: any[]) => {
      this.incidents = response;
      this.incidentForm = this.initDefinitionForm();
      this.incidentForm.patchValue({
        incidentType: incidentType.type,
        cameraId: this.selectedSiblingCamera
      })
      const incidents = this.incidentForm.get('incidents') as FormArray;
      this.incidents.forEach(incident => {
        const fg = this.getIncident(incident, incidentType.type)
        const attributes = fg.get('attributes') as FormArray;
        const attributeValues = fg.get('attributeValues') as FormArray;

        incident.attributeValues.forEach(attribute => {
          attributes.push(this.fb.group({
            "name": attribute.attribute.name,
            "dataType": attribute.attribute.dataType,
            "unit": attribute.attribute.unit
          }))
          attributeValues.push(this.fb.group({
            value: [attribute.value || '', [Validators.required]],
            attributeId: attribute.incidentAttributeId
          }))
        });
        if (!incident.checked) {
          attributeValues.disable();
        }
        incidents.push(fg)
      });
      $('#incidentModel').modal('show');
    })
  }

  initDefinitionForm() {
    return this.fb.group({
      "cameraId": '',
      "incidentType": ['', [Validators.required]],
      "incidents": this.fb.array([]),
      "save": false
    })
  }

  getIncident(incident?: any, incidentType?: any) {
    return this.fb.group({
      "name": [incident.incidentName || '', [Validators.required]],
      "checked": [incident.checked || false, [Validators.required]],
      "type": [incidentType, [Validators.required]],
      "locationRelated": [0, [Validators.required]],
      "locationTypeRelated": [0, [Validators.required]],
      "attributes": this.fb.array([]),
      "attributeValues": this.fb.array([])
    })
  }

  onSubmit(apply?: boolean) {

    if (apply) {
      this._incidentService.saveCameraIncidents(this.cameraDetails.equipmentMapping.locationId, this.cameraId, { cameraId: this.selectedSiblingCamera }).subscribe(response => {
        console.log("Applied");
        this.selectedSiblingCamera = null;
        swal({
          title: 'Saved',
          text: 'Applied same definitions for this camera too.',
          icon: 'success',
          timer: 1000,
          buttons: [false]
        })
      }, error => {

      })
    } else {
      const data = this.incidentForm.value;
      delete data['cameraId'];
      if (this.incidentForm.dirty) {
        this.incidentForm.get('save').patchValue(true);
        this._incidentService.saveCameraIncidents(this.cameraDetails.equipmentMapping.locationId, this.cameraId, this.incidentForm.value).subscribe(response => {
          this.incidentForm.markAsPristine();
          swal({
            title: 'Saved',
            text: this.selectedIncidentType.name + ' saved successfully',
            icon: 'success',
            timer: 1000,
            buttons: [false]
          })
          this.incidentForm.get('save').patchValue(false);
        }, error => {
          this.incidentForm.get('save').patchValue(false);
        })
      } else {
        swal({
          icon: 'warning',
          text: 'You didn\'t make any changes.',
          buttons: [null, 'OK'],
          closeOnClickOutside: false,
        })
      }
    }
  }

  setControls(checked: boolean, incident: FormGroup) {
    const attributeValues = incident.controls.attributeValues as FormArray;
    if (checked) {
      attributeValues.enable();
    } else {
      attributeValues.disable();
    }
  }

}
