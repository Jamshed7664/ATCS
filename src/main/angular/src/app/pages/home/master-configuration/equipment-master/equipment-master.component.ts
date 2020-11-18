import { Component, OnInit } from '@angular/core';
import { FormGroup, FormArray, FormBuilder, Validators, FormControl } from '@angular/forms';
import { AuthService } from 'src/app/services/auth/auth.service';
import swal from 'sweetalert';

@Component({
  selector: 'app-equipment-master',
  templateUrl: './equipment-master.component.html',
  styleUrls: ['./equipment-master.component.scss']
})
export class EquipmentMasterComponent implements OnInit {
  equipmentType: any;
  equipmentTypefields: any[] = [];
  selectedEquipmentType: any = {};
  equipmentMasterForm: FormGroup;
  control: FormArray;
  mode: boolean;
  loader = true
  dataType = [
    { id: 1, name: 'Checkbox' },
    { id: 2, name: 'Text' },
    { id: 3, name: 'Number' },
    { id: 4, name: 'Option' },
    { id: 5, name: 'Range' },
    { id: 6, name: 'IPADDRESS' }
  ];

  constructor(
    private fb: FormBuilder,
    private authServices: AuthService) { }

  ngOnInit(): void {
    this.equipmentMasterForm = this.fb.group({
      list: this.fb.array([])
    });
    this.getAllEquipment();
  }

  // get location type
  getAllEquipment() {
    this.loader = true;
    this.authServices.getAllEquipmentType().subscribe(res => {
      this.equipmentType = res;
      this.setEquipmentType(res[0]);
      this.loader = false;

    });
  }
  // equipmentTypeAttributes
  setEquipmentType(item) {
    this.selectedEquipmentType = item;
    this.equipmentTypefields = item.equipmentTypeAttributes;
    this.equipmentMasterForm = this.fb.group({
      list: this.fb.array([])
    });
    let list = <FormArray>this.equipmentMasterForm.controls['list'];
    item.equipmentTypeAttributes.forEach(field => {
      if (!field.fixed) {
        list.push(this.createList(field));
      }
    });

  }
  cancelUpdate() {
    this.setEquipmentType(this.selectedEquipmentType);

  }

  createList(object?: any): FormGroup {
    const fg = this.fb.group({
      id: null,
      name: ['', Validators.required],
      dataType: ['', Validators.required],
      fixed: [false],
      isSpecific: [false]
    });
    if (object && object.hasOwnProperty('options')) {
      object.options = object.options.toString();
      fg.addControl('options', new FormControl(object.options, Validators.required));
    }
    if (object)
      fg.patchValue(object)
    return fg;
  }

  addRow() {
    window.scrollTo(0, 2000);
    const list = <FormArray>this.equipmentMasterForm.controls['list'];
    list.push(this.createList());
  }

  deleteRow(index: number) {
    const list = <FormArray>this.equipmentMasterForm.controls['list'];
    if (list.controls[index].value.id) {
      this.deletItem(index);
    } else {
      list.removeAt(index);
    }
  }

  onChangeDataType(item: FormGroup, event) {
    if (event == "Option" || event == "Range") {
      item.addControl('options', new FormControl(null, Validators.required))
      if (item.value.id) {
        const field = this.equipmentTypefields.find(f => f.id === item.value.id)
        item.controls['options'].patchValue(field.options.toString())
      }
    } else {
      item.removeControl('options')
    }
  }

  // function for delete item....
  deletItem(index) {
    const list = <FormArray>this.equipmentMasterForm.controls['list'];
    let item = list.controls[index].value;
    swal({
      text: "Are you sure you want to Delete?",
      icon: "warning",
      closeOnClickOutside: false,
      buttons: ['Yes', 'No'],
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
      } else {
        this.authServices.deleteEquipmentType(this.selectedEquipmentType.id, item.id).subscribe(res => {
          list.removeAt(index);
          console.log('index is', index);

          this.getAllEquipment();
          swal({
            title: 'Successful',
            text: 'Field Deleted Successfully!!',
            icon: 'success',
            timer: 2000,
            buttons: [false],
          }).then(() => {
          })
        });
      }
    })
  }
  canDeactivate() {
    if (this.equipmentMasterForm.dirty) {
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
    if (this.equipmentMasterForm.invalid) {
      let contorlvalue = <FormArray>this.equipmentMasterForm.controls['list']
      contorlvalue.controls.forEach(element => {
        if (!element.value.name) {
          swal('Error', 'Name should not be empty !!', 'warning')
        }
        else if (!element.value.dataType) {
          swal('Error', 'DataType should not be empty !!', 'warning')
        }
        else if (!element.value.options) {
          swal('Error', 'Options should not be empty !!', 'warning')
        }
      });
    } else
      swal({
        text: "Are you sure you want to submit?",
        icon: "warning",
        closeOnClickOutside: false,
        buttons: ['Yes', 'No'],
        dangerMode: true,
      }).then(willSubmit => {
        if (!willSubmit) {
          this.authServices.onsaveEquipmentType(this.selectedEquipmentType.id, this.equipmentMasterForm.value).subscribe(res => {
            this.equipmentMasterForm.markAsPristine();
            this.selectedEquipmentType.equipmentTypeAttributes = res;
            this.setEquipmentType(this.selectedEquipmentType);
            this.equipmentTypefields = res;
            swal({
              title: 'Successful',
              text: 'Field created successfully',
              icon: 'success',
              timer: 1000
            })
          })
        }
      })

  }

}
