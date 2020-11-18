
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormArray, FormBuilder, Validators, FormControl } from '@angular/forms';
import { AuthService } from 'src/app/services/auth/auth.service';
import swal from 'sweetalert';
@Component({
  selector: 'app-location-master',
  templateUrl: './location-master.component.html',
  styleUrls: ['./location-master.component.scss']
})
export class LocationMasterComponent implements OnInit {
  locationType: any[] = [];
  errMessage: any
  locationTypefields: any[] = [];
  selectedLocationType: any = {};
  locationForm: FormGroup;
  control: FormArray;
  mode: boolean;
  loader: boolean = false;
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
    this.locationForm = this.fb.group({
      list: this.fb.array([])
    });
    this.getLocation()

  }
  // get location type
  getLocation() {
    this.loader = true;
    this.authServices.getLocation().subscribe(res => {
      this.locationType = res;
      this.loader = false;
      this.setLocationType(res[0]);

    });
  }

  setLocationType(item) {
    this.selectedLocationType = item;
    this.locationTypefields = item.locationTypeAttributes;
    this.locationForm = this.fb.group({
      list: this.fb.array([])
    });
    let list = <FormArray>this.locationForm.controls['list'];
    item.locationTypeAttributes.forEach(field => {
      if (!field.fixed) {
        list.push(this.createList(field));
      }
    });
  }
  cancelUpdate() {
    this.setLocationType(this.selectedLocationType)
  }
  // editTypeField(field: any) {
  //   console.log('value is :', field);
  //   const list = <FormArray>this.locationForm.controls['list'];
  //   for (let i = 0; i <= field.length; i++) {
  //     list.controls[i].patchValue({
  //       id: field.id,
  //       name: field.name,
  //       dataType: field.dataType,
  //     });
  //   }
  // }

  createList(object?: any): FormGroup {
    const fg = this.fb.group({
      id: object ? object.id : null,
      name: new FormControl((object ? object.name : ''), Validators.required),
      dataType: new FormControl((object ? object.dataType : ''), Validators.required),
      fixed: new FormControl(object ? object.fixed : false)
    });
    if (object && object.hasOwnProperty('options')) {
      object.options = object.options.toString();
      fg.addControl('options', new FormControl(object.options, Validators.required));
    }
    if (object && object.dataType == 'Range') {
      fg.addControl('min', new FormControl(Math.min(...object.options.split(','))))
      fg.addControl('max', new FormControl(Math.max(...object.options.split(','))))
    }
    return fg;
  }

  addRow() {
    window.scrollTo(0, 2000);
    const list = <FormArray>this.locationForm.controls['list'];
    list.push(this.createList());
  }

  deleteRow(index: number) {
    const list = <FormArray>this.locationForm.controls['list'];
    if (list.controls[index].value.id) {
      console.log(list.controls[index].value.id);
      this.deletItem(index);
    } else {
      list.removeAt(index);
    }
  }

  onChangeDataType(item: FormGroup, event) {
    if (event == "Option" || event == "Range") {
      item.addControl('options', new FormControl(null, Validators.required))
      if (item.value.id) {
        const field = this.locationTypefields.find(f => f.id === item.value.id)
        item.controls['options'].patchValue(field.options.toString())
      }
      if (event == "Range") {
        item.addControl('min', new FormControl(null, Validators.required))
        item.addControl('max', new FormControl(null, Validators.required))
      }
    } else {
      item.removeControl('options')
      item.removeControl('min')
      item.removeControl('max')
    }
  }

  // function for delete item....
  deletItem(index) {
    const list = <FormArray>this.locationForm.controls['list'];
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
        this.authServices.deletLocationType(this.selectedLocationType.id, item.id).subscribe(res => {
          console.log(res);
          this.getLocation()
          list.removeAt(index);
          swal({
            title: 'Successful',
            text: 'Field Deleted Successfully!!',
            icon: 'success',
            timer: 1500,
            buttons: [false],
          }).then(() => {
          })
        });
      }
    })
  }

  onSubmit() {
    if (this.locationForm.invalid) {
      let contorlvalue = <FormArray>this.locationForm.controls['list']
      contorlvalue.controls.forEach(element => {
        if (!element.value.name) {
          swal('Error', 'Name must not be empty !!', 'warning')
        }
        else if (!element.value.dataType) {
          swal('Error', 'DataType must not be empty !!', 'warning')
        }
        else if (!element.value.options) {
          swal('Error', 'Options/Range value must not be empty !!', 'warning')
        }
      });
    } else
      swal({
        text: "Are you sure you want to Submit?",
        icon: "warning",
        closeOnClickOutside: false,
        buttons: ['Yes', 'No'],
        dangerMode: true,
      }).then((willDelete) => {
        if (!willDelete) {
          this.authServices.onSaveLocation(this.selectedLocationType.id, this.locationForm.value).subscribe(res => {
            this.locationForm.markAsPristine();
            this.selectedLocationType.locationTypeAttributes = res;
            this.setLocationType(this.selectedLocationType);
            this.locationTypefields = res;
            swal({
              title: 'Success',
              text: 'Field created successfully',
              icon: 'success',
              timer: 500
            })
          });
        }
      })
  }
  canDeactivate() {
    if (this.locationForm.dirty) {
      var confirmMsg = confirm('Do you want to discard changes');
      if (confirmMsg) {
        return true;
      } else {
        return false;
      }
    }
    return true
  }

  calculateOptions(form: FormGroup) {
    const min = form.get('min').value;
    const max = form.get('max').value;
    if (min <= max) {
      const array = [];
      for (let val = min; val <= max; val++) {
        array.push(val);
      }
      form.controls['options'].patchValue(array.toString())
    } else {
      form.controls['options'].patchValue('')
    }

  }
}
