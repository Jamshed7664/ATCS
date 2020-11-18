import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, FormControl, Validators } from '@angular/forms';
import { EquipmentService } from '../../../../services/equipment/equipment.service';
import { Router, CanActivate, ActivatedRoute } from '@angular/router';
import swal from 'sweetalert';

@Component({
  selector: 'app-add-equiment',
  templateUrl: './add-equiment.component.html',
  styleUrls: ['./add-equiment.component.scss']
})
export class AddEquimentComponent implements OnInit {

  equipmentType: any = {};
  equipmentTypes: any[];
  equipmentForm: FormGroup;
  submit = false;
  canDeactivates: boolean = true;
  loader: boolean;
  constructor(private fb: FormBuilder,
    private router: Router,
    private canActivate: ActivatedRoute,
    private eqpService: EquipmentService,
    private _equipmentService: EquipmentService) { }

  ngOnInit() {
    this.getEquipmentTypes();
    this.equipmentForm = this.initEquipmentForm();

  }
  queryParams() {
    this.canActivate.params.subscribe(params => {
      if (params.id) {
        this.onEquipmentTypeChange(params.id);
        this.initEquipmentForm(params.id)
      }
    })

  }
  getEquipmentTypes() {
    this.loader = true
    this._equipmentService.getEquipmentTypes().subscribe((response: any[]) => {
      this.equipmentTypes = response;
      this.loader = true
      this.queryParams();
    })
  }

  onEquipmentTypeChange(typeId) {
    this.equipmentType = this.equipmentTypes.find(e => e.id == typeId)
    this.addAttributesValue(this.equipmentType.equipmentTypeAttributes);
  }

  resetForm() {
    this.equipmentForm.controls['attributesValue'].reset()
  }


  initEquipmentForm(obj?: any) {
    const fg = this.fb.group({
      "typeId": [null, Validators.required],
      "attributesValue": this.fb.array([])
    })
    if (obj) {
      console.log('obj is', obj);
      this.equipmentForm.patchValue({
        'typeId': obj
      })

    }
    return fg
  }

  addAttributesValue(attributes: any[]) {
    this.equipmentForm.removeControl('attributesValue');
    this.equipmentForm.addControl('attributesValue', this.fb.array([]))
    const attributesValue = <FormArray>this.equipmentForm.controls['attributesValue'];
    attributes.forEach(attr => {
      const attrsValue = this.fb.group({
        name: attr.name,
        dataType: attr.dataType,
        htmlInputType: attr.htmlInputType,
        attributeId: [attr.id, Validators.required],
        isSpecific: attr.isSpecific
      })
      if (attr.options) {
        attrsValue.addControl('options', this.fb.array(attr.options));
      }
      if (attr.isSpecific) {
        attrsValue.addControl('attributeValues', this.fb.array([
          this.fb.group({
            value: ['', Validators.required]
          })]))
      } else {
        if (attr.name == 'Quantity') {
          attrsValue.addControl('value', new FormControl(1, [Validators.required, Validators.min(1)]))
        } else {
          attrsValue.addControl('value', new FormControl((attr.htmlInputType == 'checkbox') ? attr.value == true : attr.value, Validators.required))
        }

      }
      attributesValue.push(attrsValue);
    });
    const quantityFormControl = attributesValue.controls.find(c => c.value.name == 'Quantity');
    this.createSpecificCountOfAttributesValue(quantityFormControl.value);
  }

  createSpecificCountOfAttributesValue(count: number) {
    if (!count) count = 1;
    const attributesValue = (<FormArray>this.equipmentForm.controls['attributesValue']);
    attributesValue.controls.forEach((attr: FormGroup) => {
      const attributeValues = (<FormArray>attr.controls['attributeValues']);
      if (attr.controls['isSpecific'].value) {
        if (attributeValues.controls.length < count) {
          for (let i = attributeValues.controls.length; i < count; i++) {
            attributeValues.push(
              this.fb.group({
                value: ['']
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
      if (this.equipmentForm.invalid) {
        const attributeValues = <FormArray>this.equipmentForm.controls['attributesValue'];
        attributeValues.controls.forEach(c => {
          console.log(c.value);
          c.markAsTouched()
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
      }
    } else {
      this.canDeactivates = false;
      this._equipmentService.addEquipment(this.equipmentForm.value).subscribe(response => {
        console.log(response);
        swal({
          text: 'Equipment added successfully',
          icon: 'success',
          timer: 1000,

        }).then((willcreate => {
          if (!willcreate) {
            this.router.navigate(['/home/equipments'])
          }
        }))
      })
    }

  }

}
