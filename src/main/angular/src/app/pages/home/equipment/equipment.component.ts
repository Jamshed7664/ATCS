import { Router, RouterEvent } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { EquipmentService } from '../../../services/equipment/equipment.service';
import swal from 'sweetalert';
import { FormGroup, FormControl } from '@angular/forms';
import * as $ from 'jquery'
import { ApiService } from 'src/app/services/api/api.service';
@Component({
  selector: 'app-equipment',
  templateUrl: './equipment.component.html',
  styleUrls: ['./equipment.component.scss']
})
export class EquipmentComponent implements OnInit {
  loader = false;
  equipments: any = [];
  myForm: FormGroup
  term: any;
  authorities: any;
  givenAuthorities: any = [];
  equipmentGroup: any = [];
  constructor(private _equipmentService: EquipmentService,
    private router: Router,
    private equipmentService: EquipmentService) { }

  ngOnInit() {
    this.getAllAuthorities()
    this.getAllEquipments();
  }

  getAllEquipments() {
    this.loader = true
    this._equipmentService.getAllEquipments().subscribe((response: any[]) => {
      this.equipments = response;
      this.router.navigate(['home/equipments/group', this.equipments[0].id])
      this.checkRouterUrl();
      this.loader = false
    })
  }

  checkRouterUrl() {
    this.router.events.subscribe((event: RouterEvent) => {
      if (event.url == '/home/equipments') {
        this.router.navigate(['home/equipments/group', this.equipments[0].id])
      }
    })
  }

  getAllAuthorities() {
    let userInfo = JSON.parse(window.localStorage.getItem('userInfo'));
    this.authorities = userInfo.authorities;
    this.authorities.forEach(element => {
      this.givenAuthorities.push(element.name)
    });
  }

  getArray(array: any[]) {
    const copy = array.filter(a => a.attribute.isSpecific);
    return copy;
  }

}
