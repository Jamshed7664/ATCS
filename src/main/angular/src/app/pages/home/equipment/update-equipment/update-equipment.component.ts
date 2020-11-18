import { FormGroup, FormBuilder, Validators, FormArray, FormControl } from '@angular/forms';
import { EquipmentService } from './../../../../services/equipment/equipment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import swal from 'sweetalert';
@Component({
  selector: 'app-update-equipment',
  templateUrl: './update-equipment.component.html',
  styleUrls: ['./update-equipment.component.scss']
})
export class UpdateEquipmentComponent implements OnInit {
  selectedEquipmentType: any;
  equipmentTypes: any[];
  equipmentForm: FormGroup;
  submit = false;
  readOnly: boolean = true;
  loader: boolean = true
  canDeactivates: boolean = true;
  constructor(private fb: FormBuilder, private activayeRoute: ActivatedRoute,
    private router: Router, private equipmentService: EquipmentService,
    private _equipmentService: EquipmentService,
    private _location: Location) { }
  ngOnInit() {
    this.equipmentForm = this.initEquipmentForm();
    this.getEquipmentTypes();
    this.queryParams()

  }

  queryParams() {
    this.loader = true
    this.activayeRoute.params.subscribe((params => {
      this.equipmentService.getEquipmentById(params.id).subscribe(res => {
        this.selectedEquipmentType = res;
        this.loader = false
        this.initEquipmentForm(res)
      })
    }))
  }
  editable() {
    this.initEquipmentForm(this.selectedEquipmentType)
  }
  getEquipmentTypes() {
    this._equipmentService.getEquipmentTypes().subscribe((response: any[]) => {
      this.equipmentTypes = response;
    })
  }
  onEquipmentTypeChange(typeId) {
    this.selectedEquipmentType = this.equipmentTypes.find(e => e.id == typeId)
    this.addAttributesValue(this.selectedEquipmentType.equipmentTypeAttributes);
  }

  initEquipmentForm(item?: any) {
    const fg = this.fb.group({
      "typeId": [{ value: null, disabled: true }, Validators.required],
      "attributesValue": this.fb.array([]),
      'availableCount': []
    })
    if (item) {
      this.equipmentForm.patchValue({
        "typeId": item.equipmentTypeId,
        'availableCount': item.availableCount || 0
      })
    }
    if (item && item.attributeValues) {
      this.addAttributesValue(item.attributeValues)
    }
    return fg;
  }

  addAttributesValue(attributes: any[]) {
    this.equipmentForm.removeControl('attributesValue');
    this.equipmentForm.addControl('attributesValue', this.fb.array([]))
    const attributesValue = <FormArray>this.equipmentForm.controls['attributesValue'];
    attributes.forEach(attr => {
      const attrsValue = this.fb.group({
        name: attr.attribute.name,
        availableCount: attr.attribute.availableCount,
        dataType: attr.attribute.dataType,
        htmlInputType: attr.attribute.htmlInputType,
        attributeId: attr.attribute.id,
        isSpecific: attr.attribute.isSpecific
      })
      if (attr.attribute.options) {
        attrsValue.addControl('options', this.fb.array(attr.attribute.options));
      }
      if (attr.attribute.isSpecific) {
        attrsValue.addControl('attributeValues', this.fb.array([]))
        const attributeValues = <FormArray>attrsValue.controls['attributeValues'];
        if (attr.attributeValues) {
          attr.attributeValues.forEach(v => {
            attributeValues.push(this.fb.group({
              value: [v.value, Validators.required],
              equipmentId: v.equipmentId,
              equipmentName: v.equipmentName
            }))
          });
        } else {
          const quantityFormControl = attributesValue.controls.find(c => c.value.name == 'Quantity');
          // this.createSpecificCountOfAttributesValue(quantityFormControl.value.value);
          for (let i = attributeValues.controls.length; i < quantityFormControl.value.value; i++) {
            attributeValues.push(
              this.fb.group({
                value: ['']
              })
            )
          }
        }
      } else {
        if (attr.attribute.name == 'Quantity') {
          attrsValue.addControl('value', new FormControl(attr.value, [Validators.required, Validators.min(1)]))
        } else {
          attrsValue.addControl('value', new FormControl((attr.attribute.htmlInputType == 'checkbox') ? attr.value == true : attr.value, Validators.required))
        }

      }
      attributesValue.push(attrsValue);
    });
  }

  createSpecificCountOfAttributesValue(count: number) {
    const attributesValue = (<FormArray>this.equipmentForm.controls['attributesValue']);
    attributesValue.controls.forEach((attr: FormGroup) => {
      const attributeValues = (<FormArray>attr.controls['attributeValues']);
      if (attr.controls['isSpecific'].value) {
        if (attributeValues.controls.length < count) {
          for (let i = attributeValues.controls.length; i < count; i++) {
            attributeValues.push(
              this.fb.group({
                value: ['', Validators.required]
              })
            )
          }
        } else {
          for (let i = attributeValues.controls.length; i >= count; i--) {
            attributeValues.removeAt(i);
          }
        }
      }
    });
    return;
  }

  initAttributesValue() {
    this.fb.group({
      "attributeId": "1",
      "value": "iOS",
      "equipmentId": "EQMST000001",
      "attributeValues": [
        null
      ]
    })
  }
  canDeactivate() {
    if (this.equipmentForm.dirty && this.canDeactivates) {
      var confirmMsg = confirm('Do you want to discard changes');
      if (confirmMsg) {
        return true;
      } else {
        return false;
      }
    }
    return true
  }

  onSubmit() {
    console.log(this.equipmentForm.value);
    if (this.equipmentForm.invalid) {
      const attributeValues = <FormArray>this.equipmentForm.controls['attributesValue'];
      attributeValues.controls.forEach(c => {
        if (c.value.attributeValues) {
          for (let index = 0; index < c.value.attributeValues.length; index++) {
            if (!c.value.attributeValues[index].value) {
              swal('Error', c.value.name + ' must not be empty', 'warning')
            }
          }
        } else if (!c.value.value) {
          swal('Error', c.value.name + ' must not be empty', 'warning')
        }
      });
    } else
      swal({
        text: "Are you sure you want to Update?",
        icon: "warning",
        closeOnClickOutside: false,
        buttons: ['Yes', 'No'],
        dangerMode: true,
      }).then(willupdate => {
        if (!willupdate) {
          this.submit = true
          this.canDeactivates = false;
          this._equipmentService.updateEquipment(this.selectedEquipmentType.groupId, this.equipmentForm.getRawValue()).subscribe(res => {
            this.submit = false;
            swal({
              icon: 'success',
              text: 'Equipment updated successfully',
              timer: 1000,
              buttons: [false]
            }).then(willRoute => {
              if (!willRoute) {
                this._location.back();
              }
            })
          }, error => {
            this.submit = false;
          })
        } else {
          this.submit = false
        }
      })

    // }
  }

  back() {
    this._location.back();
  }

}

