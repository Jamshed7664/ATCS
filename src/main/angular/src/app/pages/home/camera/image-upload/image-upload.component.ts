import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CameraService } from 'src/app/services/camera/camera.service';
import swal from 'sweetalert';
import { Location } from '@angular/common';
import { ApiService } from 'src/app/services/api/api.service';
import { DomSanitizer } from '@angular/platform-browser';
import { EquipmentService } from '../../../../services/equipment/equipment.service';
@Component({
  selector: 'app-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.scss']
})
export class ImageUploadComponent implements OnInit {
  equipmentDetails: any = {};
  imageData: any;
  sanitizedImgUrl: string;
  imgUrl: any;
  fileToUpload: File = null;
  upload: boolean;
  paramsId: any;
  mainCordinateForm: FormGroup;
  loader = true;
  camDetail;
  selectedImg: File;
  imageSrc: any;
  constructor(private fb: FormBuilder,
    private activateroute: ActivatedRoute,
    private api: CameraService,
    private _equipmentService: EquipmentService,
    private route: Router,
    private _location: Location,
    private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.queryParams();
    this.getImage()
  }

  queryParams() {
    this.activateroute.params.subscribe(params => {
      this.paramsId = params.id
      this.getEquipmentDetails(params.id);
    })
  }

  getEquipmentDetails(id) {
    this._equipmentService.getEquipmentDetail(id).subscribe(response => {
      this.equipmentDetails = response;
    })
  }

  onMainCoordinate() {
    if (this.imageData)
      this.route.navigate(['/home/mainCoordinate/' + this.paramsId])
    else {
      swal('Error', 'Please upload image first', 'warning')
    }

  }

  getImage() {
    this.loader = true;
    this.api.getImage(this.paramsId).subscribe(res => {
      this.imageData = res;
      this.selectedImg = <File>this.convertDataUrltoFile(res.imageData, res.imageType);
      this.showSelectedImg();
      this.loader = false;
    }, err => {
      this.imageData = null;
      this.loader = false;
    })
  }

  handleFileInput(event) {
    this.fileToUpload = <File>event.target.files[0];
    if (this.fileToUpload.size / 1024 / 1024 > 3) {
      let size = (this.fileToUpload.size / 1024 / 1024)
      swal('Error ! ', 'Image size should be less than 5 MB, current image size is ' + size.toFixed(1) + ' MB', 'warning')
    } else {
      const files = event.target.files[0];
      const reader = new FileReader();
      reader.onload = e => this.imageSrc = reader.result;
      reader.readAsDataURL(files);
    }
  }

  uploadFile() {
    if (!this.fileToUpload) {
      swal('Error', 'Please select file first', 'warning')
    } else {
      this.upload = true;
      this.api.uploadimage(this.paramsId, this.fileToUpload).subscribe(res => {
        this.imageData = res;
        this.upload = false;
        this.fileToUpload = null
        swal({
          title: 'Image uploaded',
          text: 'Image uploaded successfully !!',
          icon: 'success',
          timer: 1000,
          buttons: [false],
        })
      }, err => {
        this.upload = true;
      })
    }
  }

  showSelectedImg() {
    if (this.imgUrl) {
      window.URL.revokeObjectURL(this.imgUrl);
    }
    this.imgUrl = window.URL.createObjectURL(this.selectedImg);
    this.imageSrc = <string>this.sanitizer.bypassSecurityTrustUrl(window.URL.createObjectURL(this.selectedImg));
  }

  convertDataUrltoFile(dataURI: string, type: string) {
    // dataURI is the actual data part of base64 string found part after ','
    // convert dataURI to raw binary data held in a string

    const byteString = atob(dataURI);

    // write the bytes of the string to a typed array
    const ia = new Uint8Array(byteString.length);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }

    return new Blob([ia], { type: type });
  }

  back() {
    this._location.back();
  }

}
