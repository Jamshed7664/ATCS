import { Component, OnInit, AfterViewInit } from '@angular/core';
import { LocationService } from '../../../services/location/location.service';
import { EquipmentService } from '../../../services/equipment/equipment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormArray, FormBuilder, FormControl, Validators } from '@angular/forms';
declare let $: any;
@Component({
  selector: 'app-equipment-mapping',
  templateUrl: './equipment-mapping.component.html',
  styleUrls: ['./equipment-mapping.component.scss']
})
export class EquipmentMappingComponent implements OnInit, AfterViewInit {
  loader: boolean;
  countGroupValue: any[];
  process: boolean;
  selectedEquipmentIds: string[] = [];
  backupResponse: any;
  mapping: any = {};
  itemToEdit: any;
  equipmentIds: any = {};
  count: number;
  dropppedEvent: any;
  dropppedLocation: any;
  draggedItemGroup: any;
  mappedLocation: any = {};
  mappingForm: FormGroup;
  countForm: FormGroup = this.fb.group({ count: [] });
  equipmentMapping: any[] = [];
  location: any = {};
  draggedItem: any = {};
  editable: boolean;
  constructor(private _locationService: LocationService,
    private _equipmentService: EquipmentService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder) { }

  ngOnInit() {
    this.mappingForm = this.fb.group({
      list: this.fb.array([])
    });
    this.route.params.subscribe(params => {
      console.log(params);
      if (params['id']) {
        // this.getLocationById(params.id);
        this.getEquipmentMappingByLocationId(params.id)
      }
    })
  }

  ngAfterViewInit() {
    $('#myModal').on('hidden.bs.modal', (e) => {
      if (this.dropppedEvent && this.dropppedEvent.srcElement.className === 'droppable') {
        this.dropppedEvent.srcElement.parentNode.style.backgroundColor = "rgba(0,0,0,0)";
        this.dropppedEvent.srcElement.parentNode.style.border = '1px solid #dee2e6';



      }
    })
  }

  getEquipmentMappingByLocationId(id) {
    this.loader = true;
    this._equipmentService.getEquipmentMappingByLocationId(id).subscribe(response => {
      this.backupResponse = JSON.parse(JSON.stringify(response));
      this.patchInitialyMappedEquipments(response);
    })
  }

  patchInitialyMappedEquipments(response) {
    this.mappingForm = this.fb.group({
      list: this.fb.array([])
    });
    this.location = response.location;
    this.equipmentMapping = response.equipments;
    this.equipmentMapping.forEach(eqpType => {
      eqpType.list.forEach(l => this.mapping[l.groupId] = l);
    });
    if (this.location.equipments)
      this.location.equipments.forEach(e => {
        e.list.forEach(m => {
          m.locationId = this.location.id
          this.patchMapping(m);
        });
      });
    this.location.arms.forEach(arm => {
      if (arm.equipments)
        arm.equipments.forEach(e => {
          e.list.forEach(m => {
            m.locationId = this.location.id
            m.armId = arm.id
            this.patchMapping(m);
          });
        });
      arm.lanes.forEach(lane => {
        if (lane.equipments)
          lane.equipments.forEach(e => {
            e.list.forEach(m => {
              m.locationId = this.location.id
              m.armId = arm.id
              m.laneId = lane.id
              this.patchMapping(m);
            });
          });
      });
    });
    this.loader = false;
    console.log(this.mappingForm.value);
  }

  patchMapping(m) {
    let index = -1;
    const list = <FormArray>this.mappingForm.controls['list'];
    if (m.laneId) {
      index = list.controls.findIndex((c: FormGroup) => c.value.equipmentGroupId == m.groupId && c.value.laneId == m.laneId);
    } else if (m.armId) {
      index = list.controls.findIndex((c: FormGroup) => c.value.equipmentGroupId == m.groupId && c.value.laneId == null && c.value.armId == m.armId);
    } else if (m.locationId) {
      index = list.controls.findIndex((c: FormGroup) => c.value.equipmentGroupId == m.groupId && c.value.laneId == null && c.value.armId == null && c.value.locationId == m.locationId);
    }
    if (index >= 0) {
      if (m.count == 0) {
        list.removeAt(index);
        return;
      }
      if (m.equipmentIds && m.equipmentIds.length) {
        const fg = <FormGroup>list.controls[index];
        fg.removeControl('equipmentIds');
        fg.addControl('equipmentIds', this.fb.array(m.equipmentIds));
        fg.patchValue({ count: m.count });
      } else {
        list.controls[index].patchValue({ count: m.count })
      }
    } else {
      const fg = this.fb.group({
        locationId: m.locationId,
        armId: m.armId,
        laneId: m.laneId,
        equipmentGroupId: m.groupId,
        count: m.count
      })
      if (m.attributeValues) {
        if (m.attributeValues[0].attributeValues) {
          fg.addControl('equipmentIds', this.fb.array([]))
          const equipmentIds = <FormArray>fg.controls['equipmentIds']
          equipmentIds.push(new FormControl(m.attributeValues[0].attributeValues[0].equipmentId))
        }
      }
      if (m.equipmentIds && m.equipmentIds.length) {
        fg.addControl('equipmentIds', this.fb.array(m.equipmentIds));
      }
      list.push(fg);
    }
  }

  allowDrop(ev) {
    ev.preventDefault();
  }

  drag(ev, eqp, eqpGrp) {
    this.draggedItem = this.mapping[eqp.groupId];
    this.draggedItemGroup = eqpGrp;
    setTimeout(() => {
      if (eqp.count > 1) {
        ev.srcElement.style.display = 'block';
      } else {
        ev.srcElement.style.display = 'none';
      }
    }, 0);
  }

  dragenter(ev) {
    if (ev.srcElement.className === 'droppable') {
      ev.srcElement.parentNode.style.backgroundColor = "rgba(0,0,0,0.2)";
      ev.srcElement.parentNode.style.border = '3px dotted';
    }
  }

  dragleave(ev) {
    if (ev.srcElement.className === 'droppable') {
      ev.srcElement.parentNode.style.backgroundColor = "rgba(0,0,0,0)";
      ev.srcElement.parentNode.style.border = '1px solid #dee2e6';
    }
  }

  dragend(e, eqp) {
    if (e.srcElement.className !== 'droppable') {
      setTimeout(() => {
        e.srcElement.style.display = 'block';
      }, 0);
    }
  }


  drop(e, obj: any) {
    e.preventDefault();
    this.itemToEdit = null;
    this.dropppedLocation = obj;
    this.dropppedEvent = e;
    const locationId = e.srcElement.attributes['locationid'];
    const armId = e.srcElement.attributes['armid'];
    const laneId = e.srcElement.attributes['laneid'];
    if (e.srcElement.className === 'droppable') {
      const mappedId = laneId != null ? laneId.nodeValue : (armId != null ? armId.nodeValue : (locationId != null ? locationId.nodeValue : null));
      this.countForm = this.fb.group({ count: [this.draggedItem.availableCount, [Validators.required, Validators.max(this.draggedItem.availableCount), Validators.min(0)]] })
      this.hasSpecificAttribute(this.draggedItem, mappedId);

      $('#myModal').modal('show')
    }
  }


  hasSpecificAttribute(map, mappedId) {
    const specifiAttributeValues = map.attributeValues.filter(av => av.attribute.isSpecific && av.attributeValues);
    map.equipments = [];
    const equipmentIdAndName = {};
    const equipmentIdAndValues = {};
    const checkedOrNot = {};
    const equipments = [];
    this.equipmentIds = {};
    specifiAttributeValues.forEach(element => {
      element.attributeValues.forEach((element1: any) => {
        if (element1.mappedId) {
          if (element1.mappedId == mappedId) {
            this.equipmentIds[element1.equipmentId] = true;
            equipmentIdAndName[element1.equipmentId] = element1.equipmentName;
          }
        } else {
          equipmentIdAndName[element1.equipmentId] = element1.equipmentName;
        }
        if (equipmentIdAndValues[element1.equipmentId]) {
          equipmentIdAndValues[element1.equipmentId].push({
            "attributeName": element.attribute.name,
            "value": element1.value
          });
        } else {
          equipmentIdAndValues[element1.equipmentId] = [{
            "attributeName": element.attribute.name,
            "value": element1.value
          }];
        }
      });
    });
    this.selectedEquipmentIds = Object.keys(this.equipmentIds).filter(key => this.equipmentIds[key]).map(key => key);
    Object.keys(equipmentIdAndName).forEach(key => {
      map.equipments.push({
        "equipmentId": key,
        "equipmentName": equipmentIdAndName[key],
        "checked": true,
        "attributeValues": equipmentIdAndValues[key]
      });
    });
    return map.equipments.length;
  }

  submitMappingForm() {
    this.count = this.countForm.value.count;
    const locationId = this.dropppedEvent ? this.dropppedEvent.srcElement.attributes['locationid'] : null;
    const armId = this.dropppedEvent ? this.dropppedEvent.srcElement.attributes['armid'] : null;
    const laneId = this.dropppedEvent ? this.dropppedEvent.srcElement.attributes['laneid'] : null;
    const equipmentIds = Object.keys(this.equipmentIds).filter(key => {
      this.changeAttributeValue(key, this.equipmentIds[key])
      return this.equipmentIds[key];
    }).map(key => key);

    if (this.draggedItem.equipments.length && equipmentIds.every(e => this.selectedEquipmentIds.includes(e)) && this.selectedEquipmentIds.every(e => equipmentIds.includes(e))) {
      this.draggedItem.validationError = "You have not made any changes"
      setTimeout(() => {
        this.draggedItem.validationError = undefined;
      }, 2000);
      return;
    }
    const newEquipment = {
      availableCount: equipmentIds.length ? equipmentIds.length : this.count,
      count: equipmentIds.length ? equipmentIds.length : this.count,
      equipmentTypeCode: this.draggedItem.equipmentTypeCode,
      equipmentTypeId: this.draggedItem.equipmentTypeId,
      equipmentTypeName: this.draggedItem.equipmentTypeName,
      list: [{
        armId: armId ? armId.nodeValue : null,
        laneId: laneId ? laneId.nodeValue : null,
        locationId: locationId ? locationId.nodeValue : null,
        availableCount: equipmentIds.length ? equipmentIds.length : this.count,
        equipmentIds: equipmentIds,
        count: Object.keys(this.equipmentIds).length ? equipmentIds.length : this.count,
        equipmentTypeCode: this.draggedItem.equipmentTypeCode,
        equipmentTypeId: this.draggedItem.equipmentTypeId,
        equipmentTypeName: this.draggedItem.equipmentTypeName,
        groupId: this.draggedItem.groupId,
        attributeValues: this.draggedItem.attributeValues
      }]
    }
    if (this.dropppedLocation.equipments) {
      const equipment = this.dropppedLocation.equipments.find(e => {
        if (e.equipmentTypeId == this.draggedItem.equipmentTypeId) {
          const group = e.list.find(l => {
            if (l.groupId == this.draggedItem.groupId) {
              if (Object.keys(this.equipmentIds).length) {
                // l.availableCount = l.availableCount + l.count - equipmentIds.length;
                this.draggedItem.availableCount += l.count - equipmentIds.length;
                this.draggedItemGroup.availableCount += l.count - equipmentIds.length;
                e.count -= (l.count - equipmentIds.length);
                l.count = equipmentIds.length;
              } else {
                this.draggedItem.availableCount += l.count - this.count;
                this.draggedItemGroup.availableCount += l.count - this.count;
                e.count += (this.count - l.count);
                l.count = this.count;
              }
              return true;
            }
          });
          if (!group) {
            e.list.push(newEquipment.list[0])
            if (Object.keys(this.equipmentIds).length) {
              this.draggedItem.availableCount -= equipmentIds.length;
              this.draggedItemGroup.availableCount -= equipmentIds.length;
              e.count += equipmentIds.length;
            } else {
              this.draggedItem.availableCount -= this.count;
              this.draggedItemGroup.availableCount -= this.count;
              e.count += this.count;
            }
          }
          return true;
        }
      });
      if (!equipment) {
        this.dropppedLocation.equipments.push(newEquipment);
        if (Object.keys(this.equipmentIds).length) {
          this.draggedItem.availableCount -= equipmentIds.length;
          this.draggedItemGroup.availableCount -= equipmentIds.length;
        } else {
          this.draggedItem.availableCount -= this.count;
          this.draggedItemGroup.availableCount -= this.count;
        }
      }
    } else {
      this.dropppedLocation.equipments = [];
      this.dropppedLocation.equipments.push(newEquipment);
      if (Object.keys(this.equipmentIds).length) {
        this.draggedItem.availableCount -= equipmentIds.length;
        this.draggedItemGroup.availableCount -= equipmentIds.length;
      } else {
        this.draggedItem.availableCount -= this.count;
        this.draggedItemGroup.availableCount -= this.count;
      }
    }
    if (this.itemToEdit) {
      this.patchMapping({
        armId: this.itemToEdit.armId,
        laneId: this.itemToEdit.laneId,
        locationId: this.itemToEdit.locationId,
        count: newEquipment.list[0].count,
        equipmentIds: newEquipment.list[0].equipmentIds,
        groupId: this.draggedItem.groupId,
      })
    } else {
      this.patchMapping({
        armId: armId ? armId.nodeValue : null,
        laneId: laneId ? laneId.nodeValue : null,
        locationId: locationId ? locationId.nodeValue : null,
        count: newEquipment.list[0].count,
        equipmentIds: newEquipment.list[0].equipmentIds,
        groupId: this.draggedItem.groupId,
      })
    }
    console.log(this.mappingForm.value);

    $('#myModal').modal('hide')
  }

  onSubmit() {
    swal({
      text: "Are you sure you want to Submit?",
      icon: "warning",
      closeOnClickOutside: false,
      buttons: ['Yes', 'No'],
      dangerMode: true,
    }).then((willDelete) => {
      if (!willDelete) {
        this._equipmentService.saveEquipmentMapping(this.location.id, this.mappingForm.value).subscribe(response => {
          console.log(response);
          this.editable = false;
          swal({
            title: 'Success',
            text: 'Equipment mapped successfully',
            icon: 'success',
            timer: 500
          })
        })
      }
    })
  }

  editMapping(map, obj) {

    console.log(map, obj);
    this.itemToEdit = map;
    this.draggedItem = this.mapping[map.groupId];
    this.draggedItemGroup = this.equipmentMapping.find(e => e.equipmentTypeId == map.equipmentTypeId);

    this.countForm = this.fb.group({ count: [map.count, [Validators.required, Validators.max(this.draggedItem.availableCount + map.count), Validators.min(0)]] })
    // this.draggedItem = this.draggedItemGroup.list.find(l => l.groupId == map.groupId);
    this.dropppedLocation = obj;
    const mappedId = obj.id;
    this.hasSpecificAttribute(this.draggedItem, mappedId);
    $('#myModal').modal('show')
  }

  changeAttributeValue(equipmentId, event) {
    const mappedId = this.dropppedLocation.id;
    this.draggedItem.attributeValues.forEach(element => {
      element.attributeValues && element.attributeValues.forEach(e => {
        if (event && e.equipmentId == equipmentId) {
          e['mappedId'] = mappedId;
        } else if (e.equipmentId == equipmentId) {
          delete e.mappedId
        }
      });
    });

  }

  onConfigure(map, obj) {
    console.log(map, obj);
    this.getMappedEquipmentByGroupId(map, map.groupId);
    this.draggedItem = this.mapping[map.groupId];
    this.draggedItem['cameraConfiguration'] = (this.draggedItem.equipmentTypeName == 'Camera' && !obj.hasOwnProperty('locationTypeName'))
  }

  reset() {
    this.editable = false; this.patchInitialyMappedEquipments(this.backupResponse);

  }

  negativeToConfiguration(id) {
    $('#equipmentDetailModal').modal('hide')
    this.router.navigate(['/home/camera/' + id])
  }

  getMappedEquipmentByGroupId(map, groupId) {
    this.process = true;
    $('#equipmentDetailModal').modal('show');
    this._equipmentService.getMappedEquipmentByGroupId(map, groupId).subscribe((response: any[]) => {
      console.log(response);
      this.countGroupValue = response;
      this.process = false;

    })
  }


}
