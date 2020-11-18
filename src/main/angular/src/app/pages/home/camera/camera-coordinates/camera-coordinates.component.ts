import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CameraService } from '../../../../services/camera/camera.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../../../services/api/api.service';
import { DomSanitizer } from '@angular/platform-browser';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { EquipmentService } from '../../../../services/equipment/equipment.service';
import { forkJoin } from 'rxjs';
import { Location } from '@angular/common';
@Component({
  selector: 'app-camera-coordinates',
  templateUrl: './camera-coordinates.component.html',
  styleUrls: ['./camera-coordinates.component.scss']
})
export class CameraCoordinatesComponent implements OnInit {
  drawing: boolean;
  disableResetButton: boolean;
  @ViewChild('canvasWindow') canvasWindow: ElementRef;
  coordinateLoading: boolean;
  imageLoading: boolean;
  coordinateResponse: any;
  equipmentDetails: any = {};
  paramsId: any;
  cameraImageCoordinateForm: FormGroup;
  sanitizedImgUrl: string;
  imgUrl: any;
  selectedImg: File;
  // canvas related nodes
  canvas: HTMLCanvasElement;
  ctx: CanvasRenderingContext2D;
  regions = {
    RED_LAMP: "Red Light Signal",
    INCIDENT_DETECTION: "Incident Detection",
    ROAD_ALIGNMENT: "Road Alignment"
  }

  boundaryCordinates = {
    RED_LAMP: { // RED_LAMP region
      1: [], // [0] is x-cordinate & [1] is y-cordinate
      2: [],
      3: [],
      4: [],
      id: null,
      parent: null,
      isPatched: false,
      imageCoordinateType: "RED_LAMP",
      question: "Do you want to detect Red Light Signal?",
      checked: false
    },
    INCIDENT_DETECTION: { // INCIDENT_DETECTION region
      1: [], // [0] is x-cordinate & [1] is y-cordinate
      2: [],
      3: [],
      4: [],
      id: null,
      parent: null,
      isPatched: false,
      imageCoordinateType: "INCIDENT_DETECTION",
      question: "Do you want to detect Incident Detection?",
      checked: false
    },
    ROAD_ALIGNMENT: { // ROAD_ALIGNMENT region
      1: [], // [0] is x-cordinate & [1] is y-cordinate
      2: [],
      3: [],
      4: [],
      id: null,
      parent: 'INCIDENT_DETECTION',
      isPatched: false,
      imageCoordinateType: "ROAD_ALIGNMENT",
      question: "Do you want to add Road Alignment Points?",
      checked: false
    },
    nextPoint: 1, // stores next point number for which cordinates will be selected for currently selected region 
    nextRegion: '' //  stores currently selected region
  };

  boundaryColors = {
    RED_LAMP: 'red',
    INCIDENT_DETECTION: 'green',
    ROAD_ALIGNMENT: 'orange'
  };
  originalToCanvas = {
    widthRatio: 1, // initial values (these default values are not used)
    heightRatio: 1
  };
  constructor(private activateroute: ActivatedRoute,
    private api: CameraService,
    private sanitizer: DomSanitizer,
    private fb: FormBuilder,
    private _location: Location,
    private _equipmentService: EquipmentService) { }

  ngOnInit() {
    this.cameraImageCoordinateForm = this.initCameraImageCoordinateListRequestCameraImageCoordinateForm();
    this.cameraImageCoordinateForm.valueChanges.subscribe(val => {
      const coordinateResponse = this.coordinateResponse;
      coordinateResponse.imageCoordinates.forEach(element => {
        delete element.createdAt;
        delete element.createdById;
      });
      console.log(val.imageCoordinates,
        coordinateResponse.imageCoordinates);

      this.disableResetButton = JSON.stringify(val.imageCoordinates) == JSON.stringify(coordinateResponse.imageCoordinates);
    })
    this.createCanvas();
    this.activateroute.params.subscribe(params => {
      this.paramsId = params.id;
      this.getCameraDetails(params.id);
      const imageRequest = this.api.getImage(params.id);
      const coordinateRequest = this.api.getCameraCoordinate(params.id);
      this.imageLoading = true;
      this.coordinateLoading = true;
      imageRequest.subscribe((imageResponse) => {
        this.imageLoading = false;
        if (imageResponse) {
          this.selectedImg = <File>this.convertDataUrltoFile(imageResponse.imageData, imageResponse.imageType);
          this.showSelectedImg();
        }
        coordinateRequest.subscribe(coordinateResponse => {
          this.coordinateLoading = false;
          if (coordinateResponse) {
            this.coordinateResponse = coordinateResponse;
            this.populateRegionCoordinates(coordinateResponse.imageCoordinates)
          }
        })
      })
    })
  }

  setCanvasBackground(imgUrl: string, clbk?: Function) {

    let img = new Image();
    img.src = imgUrl;
    img.onload = () => {
      // code that keeps canvas width static and calculates height based on aspect ratio
      const aspectRatio = Number((img.naturalHeight / img.naturalWidth).toFixed(2));
      this.canvas.width = this.canvasWindow.nativeElement.clientWidth;
      this.canvas.height = this.canvas.width * aspectRatio;

      // below ratios will be used to convert the cordinates from mouse cordinates to real image cordinates and vice-versa
      this.originalToCanvas.widthRatio = Number((img.naturalWidth / this.canvas.width).toFixed(2));
      this.originalToCanvas.heightRatio = Number((img.naturalHeight / this.canvas.height).toFixed(2));

      this.ctx.drawImage(img, 0, 0, this.canvas.width, this.canvas.height);
      if (clbk) { clbk(); }

    };
    if (img) {
      this.canvas.style.zIndex = '4';
    }
  }

  createCanvas() {
    this.canvas = document.getElementById('canvas') as HTMLCanvasElement;
    this.canvas.style.position = 'relative';
    this.ctx = this.canvas.getContext('2d');

    this.canvas.addEventListener('click', (ev: MouseEvent) => {

      if (this.isClickNotAllowedOnCanvas()) { return; }
      this.canvasMouseClickHandler(ev, this.boundaryCordinates.nextRegion);
      // call below method to change the cursor to 'not-allowed' when 4 points of a region are selected
      this.canvasMouseOverHandler(undefined);

    });

    this.canvas.addEventListener('mouseover', this.canvasMouseOverHandler.bind(this));
    this.canvas.addEventListener('mouseleave', (ev: MouseEvent) => {
      document.body.style.cursor = 'default';
    });
  }

  isClickNotAllowedOnCanvas() {
    return this.boundaryCordinates.nextPoint === 5 ||
      !this.boundaryCordinates.nextRegion ||
      this.isAlreadyCordinatesPresent(this.boundaryCordinates.nextRegion) ||
      !this.imgUrl;
  }

  /**To show the region list (rows) in table with their cordinates */
  get regionNames() { return Object.keys(this.boundaryCordinates); }

  /**To show the point list (coloumns) in table */
  // getpointNames(region: string) { return Object.keys(this.boundaryCordinates[region]); }
  getpointNames(region: string) { return [1, 2, 3, 4]; }

  isAlreadyCordinatesPresent(region: string) {
    const reg = this.boundaryCordinates[region];
    return reg[1].length && reg[2].length && reg[3].length && reg[4].length;
  }

  onChangeRegion(checked: boolean, region: string) {
    if (!checked) {
      this.removeImageCoordinate(region);
      this.onRetake(region, false);
      if (region === 'INCIDENT_DETECTION') {
        this.onRetake('ROAD_ALIGNMENT', false);
        this.removeImageCoordinate('ROAD_ALIGNMENT');
        this.boundaryCordinates['ROAD_ALIGNMENT'].checked = false;
      }
    } else {
      const imageCoordinates = this.cameraImageCoordinateForm.get('imageCoordinates') as FormArray;
      imageCoordinates.push(this.initImageCoordinate(region))
    }
  }

  onRegionSelect(regionName: string) {
    // if all 4 points are not available for regionName, clear the partial available cordinates i.e treat this case same as 
    // retaking coordinates for this region
    if (regionName === 'ROAD_ALIGNMENT') {
      if (!this.isAlreadyCordinatesPresent('INCIDENT_DETECTION')) {
        swal({
          icon: 'warning',
          text: `${this.regions['ROAD_ALIGNMENT']} region can not draw without ${this.regions['INCIDENT_DETECTION']} region.`,
          buttons: [null, 'OK'],
          closeOnClickOutside: false,
        })
      }
    }
    if (!this.isAlreadyCordinatesPresent(regionName)) {
      this.onRetake(regionName, true);
    }
    this.boundaryCordinates.nextRegion = regionName;
    this.boundaryCordinates.nextPoint = 1;
    //this.openModal();
  }

  onRetake(reg: string, startDrawing?: boolean) {
    this.drawing = true;
    if (reg === 'INCIDENT_DETECTION') {
      this.clearRegionCordinates('ROAD_ALIGNMENT');
      this.removeImageCoordinate('ROAD_ALIGNMENT');
    }
    this.removeImageCoordinate(reg);
    this.clearRegionCordinates(reg);
    this.clearCanvas();
    if (reg) this.boundaryCordinates[reg].checked = startDrawing;

    // perform below operation after image is loaded in canvas backgroud, pass it as a callback to setCanvasBackground() method
    const clbk = () => {

      for (const region in this.boundaryCordinates) {
        if (region !== 'nextPoint' && region !== 'nextRegion'
          && this.boundaryCordinates.hasOwnProperty(region) && region !== reg) {
          const r = this.boundaryCordinates[region];
          let pointNo = 1;
          while (pointNo < 5) {
            if (r[pointNo].length) {
              // send cordinates accordign to canvas to render 
              const ev = {
                offsetX: r[pointNo][0] / this.originalToCanvas.widthRatio,
                offsetY: r[pointNo][1] / this.originalToCanvas.heightRatio
              };
              this.canvasMouseClickHandler(ev, region);
            }
            pointNo++;
          }
        }
      }
      // assign nextRegion which has been cleared by above code in callback
      if (startDrawing) {
        this.boundaryCordinates.nextRegion = reg;
        this.clearNextPoint();
      }
      //this.openModal();
    };
    this.setCanvasBackground(this.imgUrl, clbk);

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

  showSelectedImg() {
    if (this.imgUrl) {
      window.URL.revokeObjectURL(this.imgUrl);
    }
    this.imgUrl = window.URL.createObjectURL(this.selectedImg);
    this.sanitizedImgUrl = <string>this.sanitizer.bypassSecurityTrustUrl(window.URL.createObjectURL(this.selectedImg));
    this.onRetake('', true);

  }

  canvasMouseClickHandler(ev: MouseEvent | any, region: string) {
    this.ctx.beginPath();
    this.ctx.arc(ev.offsetX, ev.offsetY, 5, 0, Math.PI * 2, true);
    this.ctx.strokeStyle = this.giveNextColor(region);
    this.ctx.stroke();
    this.ctx.font = '10px serif';
    this.ctx.fillStyle = 'black';
    this.ctx.fillText(this.giveNextPointName(region), ev.offsetX + 5, ev.offsetY + 5);


    // store points which are the cordinates of real image
    if (!this.boundaryCordinates[region][this.boundaryCordinates.nextPoint].length)
      this.boundaryCordinates[region][this.boundaryCordinates.nextPoint] = [
        Math.round(Number(ev.offsetX * this.originalToCanvas.widthRatio)),
        Math.round(Number(ev.offsetY * this.originalToCanvas.heightRatio))
      ];
    console.log(this.boundaryCordinates[region][this.boundaryCordinates.nextPoint], this.boundaryCordinates.nextPoint);

    if (region == 'ROAD_ALIGNMENT') {
      this.checkRegionInsideTheParent('INCIDENT_DETECTION', region)
    }
    this.boundaryCordinates.nextPoint++;
    // draw path when 4 points have been selected and after that clear the next point and region
    if (this.boundaryCordinates.nextPoint === 5) {
      this.drawPath(region);
      console.log('on4');
      if (this.checkClockWise(this.boundaryCordinates[region], region)) {
        this.patchCoordinatesToForm(region);
        this.drawing = false;
      }
      this.clearNextPoint();
      this.clearNextRegion();
      // find center point within the region to write the name of region
      const regionCenterPoint = this.giveCenterPointOfRegion(region);
      this.ctx.font = '15px serif';
      this.ctx.fillStyle = this.giveNextColor(region);
      this.ctx.fillText(region.toUpperCase(), regionCenterPoint.x - 10, regionCenterPoint.y);
    }
  }

  checkRegionInsideTheParent(parent, region) {
    const x1 = this.boundaryCordinates[parent][1][0];
    const y1 = this.boundaryCordinates[parent][1][1];
    const x2 = this.boundaryCordinates[parent][2][0];
    const y2 = this.boundaryCordinates[parent][2][1];
    const x3 = this.boundaryCordinates[parent][3][0];
    const y3 = this.boundaryCordinates[parent][3][1];
    const x4 = this.boundaryCordinates[parent][4][0];
    const y4 = this.boundaryCordinates[parent][4][1];
    const x = this.boundaryCordinates[region][this.boundaryCordinates.nextPoint][0];
    const y = this.boundaryCordinates[region][this.boundaryCordinates.nextPoint][1];
    if (!this.checkRegionLiesInParentRegion(x1, y1, x2, y2, x3, y3, x4, y4, x, y)) {
      return swal({
        icon: 'warning',
        text: `${region.toUpperCase()} region is not inside ${parent} region.`,
        buttons: [null, 'Redraw'],
        closeOnClickOutside: false,
      }).then((btn) => {
        // if (btn.value) {
        this.onRetake(region, true);
        // }
      });
    }
  }

  checkClockWise(allBounds, region) {
    console.log(allBounds);
    if (allBounds[2][0] > allBounds[1][0]) {
      if (allBounds[3][1] > allBounds[2][1]) {
        if (allBounds[3][0] > allBounds[4][0]) {
          if (allBounds[4][1] > allBounds[1][1] && allBounds[4][1] > allBounds[2][1]) {
            console.log('CASE 1');
            return true;
          }
        }
      }
    }
    if (allBounds[2][1] > allBounds[1][1]) {
      if (allBounds[2][0] > allBounds[3][0]) {
        if (allBounds[3][1] > allBounds[4][1]) {
          if (allBounds[4][0] < allBounds[1][0] && allBounds[4][0] < allBounds[2][0]) {
            console.log('CASE 2');
            return true;
          }
        }
      }
    }
    if (allBounds[1][0] > allBounds[2][0]) {
      if (allBounds[2][1] > allBounds[3][1]) {
        if (allBounds[4][0] > allBounds[3][0]) {
          if (allBounds[4][1] < allBounds[1][1] && allBounds[4][1] < allBounds[2][1]) {
            console.log('CASE 3');
            return true;
          }
        }
      }
    }
    if (allBounds[1][1] > allBounds[2][1]) {
      if (allBounds[3][0] > allBounds[2][0]) {
        if (allBounds[4][1] > allBounds[3][1]) {
          if (allBounds[4][0] > allBounds[1][0] || allBounds[4][0] > allBounds[2][0]) {
            console.log('CASE 4');
            return true;
          }
        }
      }
    }
    return swal({
      icon: 'warning',
      text: `${region.toUpperCase()} region is not selected in clock wise.`,
      buttons: [null, 'Redraw'],
      closeOnClickOutside: false,
    }).then((btn) => {
      // if (btn.value) {
      this.onRetake(region, true);
      // }
    });
  }

  drawPath(region: string) {
    const reg = this.boundaryCordinates[region];
    this.ctx.beginPath();
    // render cordinates accrording to canvas resolution 
    this.ctx.moveTo(reg[1][0] / this.originalToCanvas.widthRatio, reg[1][1] / this.originalToCanvas.heightRatio);
    this.ctx.lineTo(reg[2][0] / this.originalToCanvas.widthRatio, reg[2][1] / this.originalToCanvas.heightRatio);
    this.ctx.lineTo(reg[3][0] / this.originalToCanvas.widthRatio, reg[3][1] / this.originalToCanvas.heightRatio);
    this.ctx.lineTo(reg[4][0] / this.originalToCanvas.widthRatio, reg[4][1] / this.originalToCanvas.heightRatio);
    this.ctx.closePath();
    this.ctx.strokeStyle = this.giveNextColor(region);
    this.ctx.stroke();
  }

  /**
 * Algo used to find region's center point :
 * Step1 : Find the midpoints of each side of quadrilateral
 * step2 : Center point will be the intersection point of the lines formed by
 * joining the midpoints of oppsosite sides of quadrilateral
 */
  giveCenterPointOfRegion(region: string) {

    const reg = this.boundaryCordinates[region];
    const x1 = Math.round(reg[1][0] / this.originalToCanvas.widthRatio),
      x2 = Math.round(reg[2][0] / this.originalToCanvas.widthRatio),
      x3 = Math.round(reg[3][0] / this.originalToCanvas.widthRatio),
      x4 = Math.round(reg[4][0] / this.originalToCanvas.widthRatio),
      y1 = Math.round(reg[1][1] / this.originalToCanvas.heightRatio),
      y2 = Math.round(reg[2][1] / this.originalToCanvas.heightRatio),
      y3 = Math.round(reg[3][1] / this.originalToCanvas.heightRatio),
      y4 = Math.round(reg[4][1] / this.originalToCanvas.heightRatio);

    const midPoint1 = this.giveMedianPoint({ x: x1, y: y1 }, { x: x2, y: y2 }),
      midPoint2 = this.giveMedianPoint({ x: x2, y: y2 }, { x: x3, y: y3 }),
      midPoint3 = this.giveMedianPoint({ x: x3, y: y3 }, { x: x4, y: y4 }),
      midPoint4 = this.giveMedianPoint({ x: x4, y: y4 }, { x: x1, y: y1 });

    return this.giveIntersectionPointOfTwoLines(midPoint1, midPoint2, midPoint3, midPoint4);
  }

  giveMedianPoint(a: { x: number, y: number }, b: { x: number, y: number }) {
    return {
      x: Number(((a.x + b.x) / 2).toFixed(2)),
      y: Number(((a.y + b.y) / 2).toFixed(2))
    };
  }

  giveIntersectionPointOfTwoLines(m1, m2, m3, m4) {
    // following code calculates the intersection point of two lines obtained by four coordinates
    // Line 1 is formed by joining points m1 and m3
    // Line 2 is formed by joining points m2 and m4
    const slope1 = (m3.y - m1.y) / (m3.x - m1.x),
      slope2 = (m4.y - m2.y) / (m4.x - m2.x);

    const xPoint = (m2.y - m1.y + slope1 * m1.x - slope2 * m2.x) / (slope1 - slope2);
    const yPoint = slope1 * (xPoint - m1.x) + m1.y;
    return { x: xPoint, y: yPoint };
  }


  canvasMouseOverHandler(ev: MouseEvent) {
    if (this.isClickNotAllowedOnCanvas()) {
      document.body.style.cursor = 'not-allowed';
    } else {
      document.body.style.cursor = 'pointer';
    }
  }

  clearCanvas() {
    this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
  }

  clearBoundaryCordinates() {
    for (const region in this.boundaryCordinates) {
      if (region !== 'nextPoint' && region !== 'nextRegion' && this.boundaryCordinates.hasOwnProperty(region)) {
        this.clearRegionCordinates(region);
        this.removeImageCoordinate(region);
      }
    }
  }

  clearRegionCordinates(region: string) {
    if (region) this.boundaryCordinates[region].checked = false;
    const regObject: any = this.boundaryCordinates[region];
    for (const point in regObject) {
      if (regObject.hasOwnProperty(point)) {
        if (point === 'imageCoordinateType' || point === 'isPatched' || point === 'question' || point === 'checked' || point === 'parent') {
          continue;
        }
        if (point === 'id') {
          regObject[point] = null;
        } else {
          regObject[point] = [];
        }
      }
    }
  }

  clearNextPoint() { this.boundaryCordinates.nextPoint = 1; }

  clearNextRegion() { this.boundaryCordinates.nextRegion = ''; }

  resetRegionsInBoudaryCordinates() {
    for (const x in this.boundaryCordinates) {
      if (x !== 'nextPoint' && x !== 'nextRegion' && this.boundaryCordinates.hasOwnProperty(x)) {
        if (x !== 'INCIDENT_DETECTION') {
          delete this.boundaryCordinates[x];
        }
      }
    }
  }

  giveNextColor(region: string) {
    // if (region === 'INCIDENT_DETECTION') { return this.boundaryColors.INCIDENT_DETECTION; }
    if (region.startsWith('RED_LAMP')) { return this.boundaryColors.RED_LAMP; }
    if (region.startsWith('INCIDENT_DETECTION')) { return this.boundaryColors.INCIDENT_DETECTION; }
    if (region.startsWith('ROAD_ALIGNMENT')) { return this.boundaryColors.ROAD_ALIGNMENT; }
  }

  /**@returns pointName e.g for INCIDENT_DETECTION region M_A,M_B, for left3 region L3_A,L3_B and so on */
  giveNextPointName(region: string) {
    if (region.startsWith('I')) {
      return `I${region[region.length - 1]}_${this.giveNextCharacter()}`;
    }
    if (region.startsWith('R')) {
      return `R${region[region.length - 1]}_${this.giveNextCharacter()}`;
    }
  }

  /**return one of A,B,C,D */
  giveNextCharacter() {
    return String.fromCharCode(this.boundaryCordinates.nextPoint + 64);
  }

  checkRegionLiesInParentRegion(x1, y1, x2, y2, x3, y3, x4, y4, x, y) {

    const A = (this.area(x1, y1, x2, y2, x3, y3) + this.area(x1, y1, x4, y4, x3, y3));

    const A1 = this.area(x, y, x1, y1, x2, y2);

    const A2 = this.area(x, y, x2, y2, x3, y3);

    const A3 = this.area(x, y, x3, y3, x4, y4);
    const A4 = this.area(x, y, x1, y1, x4, y4);
    console.log(A, (A1 + A2 + A3 + A4));

    return (A === A1 + A2 + A3 + A4);
  }

  area(x1, y1, x2, y2, x3, y3) {
    console.log(x1, y1, x2, y2, x3, y3);
    return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
  }

  populateRegionCoordinates(regionCoordinates: any[]) {
    // populate regionCordinates
    const imageCoordinates = this.cameraImageCoordinateForm.get('imageCoordinates') as FormArray;
    regionCoordinates.forEach(reg => {
      const region = reg.imageCoordinateType;
      this.boundaryCordinates[region][1][0] = reg.aPointXCoordinate;
      this.boundaryCordinates[region][1][1] = reg.aPointYCoordinate;
      this.boundaryCordinates[region][2][0] = reg.bPointXCoordinate;
      this.boundaryCordinates[region][2][1] = reg.bPointYCoordinate;
      this.boundaryCordinates[region][3][0] = reg.cPointXCoordinate;
      this.boundaryCordinates[region][3][1] = reg.cPointYCoordinate;
      this.boundaryCordinates[region][4][0] = reg.dPointXCoordinate;
      this.boundaryCordinates[region][4][1] = reg.dPointYCoordinate;
      this.boundaryCordinates[region]['id'] = reg.id;
      this.boundaryCordinates[region]['checked'] = true;
      const index = imageCoordinates.controls.findIndex((f: FormGroup) => f.controls['imageCoordinateType'].value == region)
      if (index < 0)
        imageCoordinates.push(this.initImageCoordinate(region))
      else
        this.patchCoordinatesToForm(region);
    });
    this.onRetake('', true);
  }

  patchCoordinatesToForm(region) {
    const imageCoordinates = this.cameraImageCoordinateForm.get('imageCoordinates') as FormArray;
    const index = imageCoordinates.controls.findIndex((f: FormGroup) => f.controls['imageCoordinateType'].value == region)
    if (index >= 0) {
      imageCoordinates.controls[index].patchValue({
        "id": this.boundaryCordinates[region].id,
        "aPointXCoordinate": this.boundaryCordinates[region][1][0],
        "aPointYCoordinate": this.boundaryCordinates[region][1][1],
        "bPointXCoordinate": this.boundaryCordinates[region][2][0],
        "bPointYCoordinate": this.boundaryCordinates[region][2][1],
        "cPointXCoordinate": this.boundaryCordinates[region][3][0],
        "cPointYCoordinate": this.boundaryCordinates[region][3][1],
        "dPointXCoordinate": this.boundaryCordinates[region][4][0],
        "dPointYCoordinate": this.boundaryCordinates[region][4][1],
        "imageCoordinateType": this.boundaryCordinates[region].imageCoordinateType
      })
    } else {
      imageCoordinates.push(this.initImageCoordinate(region))
    }
    this.boundaryCordinates[region].isPatched = true;
  }

  isRoadAlignmentValidate() {
    const imageCoordinates = this.cameraImageCoordinateForm.get('imageCoordinates') as FormArray;
    return imageCoordinates.controls.find((f: FormGroup) => f.controls['imageCoordinateType'].value == 'ROAD_ALIGNMENT')
  }


  initCameraImageCoordinateListRequestCameraImageCoordinateForm() {
    return this.fb.group({
      "imageCoordinates": this.fb.array([
      ]),
      "blinkingTimeSlots": this.fb.array([
        this.fb.group({
          "id": null,
          "startTime": "11:00:00",
          "endTime": "02:00:00"
        })
      ]),
      "roadAlignmentProperties": this.fb.group({
        "roadLength": 111.11,
        "roadBreadth": 234.1,
        "maxDistance": 234232
      })
    })
  }

  initImageCoordinate(region) {
    return this.fb.group({
      "id": this.boundaryCordinates[region].id,
      "aPointXCoordinate": [this.boundaryCordinates[region][1][0], [Validators.required]],
      "aPointYCoordinate": [this.boundaryCordinates[region][1][1], [Validators.required]],
      "bPointXCoordinate": [this.boundaryCordinates[region][2][0], [Validators.required]],
      "bPointYCoordinate": [this.boundaryCordinates[region][2][1], [Validators.required]],
      "cPointXCoordinate": [this.boundaryCordinates[region][3][0], [Validators.required]],
      "cPointYCoordinate": [this.boundaryCordinates[region][3][1], [Validators.required]],
      "dPointXCoordinate": [this.boundaryCordinates[region][4][0], [Validators.required]],
      "dPointYCoordinate": [this.boundaryCordinates[region][4][1], [Validators.required]],
      "imageCoordinateType": [this.boundaryCordinates[region].imageCoordinateType, [Validators.required]]
    });
  }

  removeImageCoordinate(region) {
    const imageCoordinates = this.cameraImageCoordinateForm.get('imageCoordinates') as FormArray;
    const index = imageCoordinates.controls.findIndex((f: FormGroup) => f.controls['imageCoordinateType'].value == region)
    if (index >= 0) imageCoordinates.removeAt(index);
    if (this.boundaryCordinates[region] && this.boundaryCordinates[region].isPatched) {
      this.boundaryCordinates[region].isPatched = false;
    } else {
      console.log(region)
    }
  }

  onSubmit() {
    console.log(this.cameraImageCoordinateForm.value);

    this.api.saveCameraCoordinate(this.paramsId, this.cameraImageCoordinateForm.value).subscribe(res => {
      this.coordinateResponse = this.cameraImageCoordinateForm.value;
      // this.reset();
      swal({
        title: 'Saved',
        text: 'Coordinate saved successfully',
        icon: 'success',
        timer: 1000,
        buttons: [false]
      })
    })
  }

  getCameraDetails(id) {
    this._equipmentService.getEquipmentDetail(id).subscribe(response => {
      this.equipmentDetails = response;
    })
  }

  reset() {
    this.clearBoundaryCordinates();
    this.populateRegionCoordinates(this.coordinateResponse.imageCoordinates)
  }

  back() {
    this._location.back();
  }

}
