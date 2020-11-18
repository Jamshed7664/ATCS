import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, FormControl, Validators, FormArray } from '@angular/forms';
import { CameraService } from 'src/app/services/camera/camera.service';
import swal from 'sweetalert';

@Component({
  selector: 'app-zone-coordinator',
  templateUrl: './zone-coordinator.component.html',
  styleUrls: ['./zone-coordinator.component.scss']
})
export class ZoneCoordinatorComponent implements OnInit {
  zoneCoordinatorForm: FormGroup;
  paramsId: any
  submit: boolean = true;
  newZone: boolean = true;
  myIndex:any;
  constructor(private activateRoute: ActivatedRoute,
    private fb: FormBuilder,
    private api: CameraService,
    private route: Router
  ) { }

  ngOnInit() {
    this.queryParams()
    // this.zoneCoordinatorForm = this.inititaeZoneForm()
  }
  queryParams() {
    this.activateRoute.params.subscribe((params) => {
      this.paramsId = params.id
      console.log(params.id);
      this.api.getZonecoordinate(this.paramsId).subscribe(res => {
        console.log(res);
        console.log('length is', res.length);
        if (res.length) {
          this.zoneCoordinatorForm = this.inititaeZoneForm(res)
        } else {
          this.initialForm()
        }
      })
    })
  }
  initialForm() {
    this.zoneCoordinatorForm = this.inititaeZoneForm()
  }
  inititaeZoneForm(coordinates?: any) {
    if (coordinates) {
    let fg = this.fb.group({
      'list': this.fb.array([])
    })
      const list = <FormArray>fg.controls['list']
      coordinates.forEach(e => {
        list.push(this.createCoordinate(e))
      })
      return fg;
    }
    else{
      let fg = this.fb.group({
        'list': this.fb.array([this.createCoordinate()])
      })
      return fg;
    }
  }

  createCoordinate(coordinates?: any) {
    console.log(coordinates);
    let fg = this.fb.group({
      "id": new FormControl(null),
      "aPointXCoordinate": new FormControl(null, Validators.required),
      "aPointYCoordinate": new FormControl(null, Validators.required),
      "bPointXCoordinate": new FormControl(null, Validators.required),
      "bPointYCoordinate": new FormControl(null, Validators.required),
      "cPointXCoordinate": new FormControl(null, Validators.required),
      "cPointYCoordinate": new FormControl(null, Validators.required),
      "dPointXCoordinate": new FormControl(null, Validators.required),
      "dPointYCoordinate": new FormControl(null, Validators.required),
    })
    if (coordinates) {
      console.log('in if', coordinates);
      fg.patchValue(coordinates)
    }
    return fg
  }

  addNewRow() {
    let list = <FormArray>this.zoneCoordinatorForm.controls['list'];
    list.push(this.createCoordinate())
  }
  removerow(index,id:any) {
    console.log(id.value.id);
 if(id.value.id){
     this.api.deleteCoordinate(this.paramsId,id.value.id).subscribe(res =>{
       console.log(res);

     })
 }else{
  let list = <FormArray>this.zoneCoordinatorForm.controls['list'];
  list.removeAt(index)
 }

  }
  onSubmit(item,index) {
    console.log(item,index);

    let payload = item;
  this.myIndex=index;
    console.log(this.zoneCoordinatorForm.controls['list'].value);
    let a = <FormArray>this.zoneCoordinatorForm.controls['list'];
    a.controls.forEach(e => {
      console.log(e.value.aPointXCoordinate);
      if (!e.value.aPointXCoordinate) {
        swal('warning', "aPointXCoordinate can't be empty", 'warning')
      } else if (!e.value.aPointYCoordinate) {
        swal('warning', "aPointYCoordinate can't be empty", 'warning')
      }
      else if (!e.value.bPointXCoordinate) {
        swal('warning', "bPointXCoordinate can't be empty", 'warning')
      }
      else if (!e.value.bPointYCoordinate) {
        swal('warning', "bPointYCoordinate can't be empty", 'warning')
      }
      else if (!e.value.cPointXCoordinate) {
        swal('warning', "cPointXCoordinate can't be empty", 'warning')
      }
      else if (!e.value.cPointYCoordinate) {
        swal('warning', "cPointYCoordinate can't be empty", 'warning')
      }
      else if (!e.value.dPointXCoordinate) {
        swal('warning', "dPointXCoordinate can't be empty", 'warning')
      }
      else if (!e.value.dPointYCoordinate) {
        swal('warning', "dPointYCoordinate can't be empty", 'warning')
      } else if (this.zoneCoordinatorForm.valid) {
        this.submit = false
        this.newZone = true;
        this.api.saveZoneCoordinate(this.paramsId, payload).subscribe(res => {
          console.log(res);
          this.newZone = false;
          this.myIndex=null;
          this.submit = true
          swal({
            title: 'Saved successfully',
            text: 'Redirecting',
            icon: 'success',
            timer: 2000,
            buttons: [false],
          })
        }, err => {
          if (err) {
            this.submit = true;
          this.myIndex=null;
          }
        })
      }
    })

    // }
  }
}
