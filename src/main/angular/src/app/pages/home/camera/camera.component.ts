import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { CameraService } from 'src/app/services/camera/camera.service';
import swal from 'sweetalert';
import { timer } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from 'src/app/services/api/api.service';
import { Location } from '@angular/common';
import { EquipmentService } from '../../../services/equipment/equipment.service';

@Component({
  selector: 'app-camera',
  templateUrl: './camera.component.html',
  styleUrls: ['./camera.component.scss']
})
export class CameraComponent implements OnInit {
  equipmentDetails: any = {};
  cameraForm: FormGroup;
  cameraId: any;
  cameraDetail: any = [];
  saveContinue: boolean = false;
  process: boolean = true;
  constructor(private fb: FormBuilder,
    private api: CameraService,
    private camApi: ApiService,
    private _equipmentService: EquipmentService,
    private _location: Location,
    private activateRoute: ActivatedRoute,
    private route: Router) { }

  ngOnInit() {
    this.cameraForm = this.cameraInitiateForm();
    this.activateRoute.params.subscribe(params => {
      this.cameraId = params.id;
      this.getCameradetail(this.cameraId)
    })
  }

  formRtspUrl = (object) => {
    const username = this.cameraForm.get('username').value;
    const password = this.cameraForm.get('password').value;
    const ipAddress = this.cameraForm.get('ipAddress').value;
    const rtsp = this.cameraForm.get('rtsp').value;
    if (username && password && ipAddress) {
      if (rtsp.localeCompare(`rtsp://${username}:${password}@${ipAddress}`) == 0)
        this.cameraForm.get('rtsp').patchValue(`rtsp://${username}:${password}@${ipAddress}`)
      else {
        this.cameraForm.get('otherRtsp').patchValue(rtsp);
        this.cameraForm.get('other').patchValue(true);
      }
    } else {
      this.cameraForm.get('rtsp').patchValue('')
      return;
    }
  }

  enableOtherRtsp(event) {
    if (event)
      this.cameraForm.get('otherRtsp').enable();
    else
      this.cameraForm.get('otherRtsp').disable();
  }

  cameraInitiateForm(object?: any) {
    const fg = this.fb.group({
      "username": [null, [Validators.required, Validators.pattern('^[@A-Za-z0-9_]{3,20}$')]],
      "password": [null, [Validators.required, Validators.minLength(5)]],
      "ipAddress": [null, [Validators.required, Validators.pattern('((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\.|$)){4}')]],
      "cameraType": [null, Validators.required],
      "rtsp": [''],
      "other": [false],
      "otherRtsp": [{ value: '', disabled: true }],
      "fps": [null, Validators.required],
      "frameWidth": [null, Validators.required],
      "frameHeight": [null, Validators.required],
      "scaleFactor": [null, Validators.required],
      "codecs": [null, Validators.required]
    });
    return fg;
  }


  getCameradetail = (id: any) => {
    this.process = true;
    this.api.cameraDetail(id).subscribe(res => {
      this.cameraDetail = res;
      this.process = false;
      console.log('camera detail is', res);
      this.cameraForm.patchValue(res);
      this.formRtspUrl(res);
      this.getEquipmentDetails(id);
    })
  }

  getEquipmentDetails(id) {
    this._equipmentService.getEquipmentDetail(id).subscribe(response => {
      this.equipmentDetails = response;
    })
  }


  saveAndContinue(item) {
    if (this.cameraForm.dirty) {
      this.onSubmit().then(() => {
        this.route.navigate(['/home/imageUpload/' + this.cameraId])
      })
    } else {
      this.route.navigate(['/home/imageUpload/' + this.cameraId])
    }
  }
  onSubmit() {
    return new Promise((resolve, reject) => {
      if (this.cameraForm.valid) {
        swal({
          title: 'Are you sure ?',
          icon: 'warning',
          text: 'Do you want to submit!!',
          closeOnClickOutside: false,
          buttons: ['YES', 'NO'],
          dangerMode: true
        }).then((willSubmit) => {
          if (!willSubmit) {
            const data = this.cameraForm.value;
            delete data.other;
            delete data.otherRtsp;
            if (this.cameraForm.get('other').value) {
              data.rtsp = this.cameraForm.get('otherRtsp').value;
            }
            this.api.saveCamera(this.cameraId, data).subscribe(res => {
              console.log(res);
              // this.cameraForm.reset();
              swal('Success', 'Camera detail saved Successfully', 'success')
              resolve()
            }, err => {
              reject()
            })
          }
        })
      } else {
        this.cameraForm.controls['username'].markAsTouched();
        this.cameraForm.controls['password'].markAsTouched();
        this.cameraForm.controls['ipAddress'].markAsTouched();
        this.cameraForm.controls['cameraType'].markAsTouched();
        this.cameraForm.controls['fps'].markAsTouched();
        this.cameraForm.controls['frameWidth'].markAsTouched();
        this.cameraForm.controls['frameHeight'].markAsTouched();
        this.cameraForm.controls['scaleFactor'].markAsTouched();
        this.cameraForm.controls['codecs'].markAsTouched();
        reject()
      }

    })
  }

  reset() {
    this.cameraForm.patchValue(this.cameraDetail);
  }
  back() {
    this._location.back();
  }
}
