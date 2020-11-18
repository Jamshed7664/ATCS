import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EquipmentService } from '../../../../services/equipment/equipment.service';

@Component({
  selector: 'app-equipment-group',
  templateUrl: './equipment-group.component.html',
  styleUrls: ['../equipment.component.scss', './equipment-group.component.scss']
})
export class EquipmentGroupComponent implements OnInit {

  selectedEquipment: any;
  typeName: any;
  process: boolean;
  countGroupValue: any[] = [];
  loader1: boolean;
  equipmentGroup: any[] = [];
  constructor(private route: ActivatedRoute,
    private router: Router,
    private _equipmentService: EquipmentService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      console.log(params);
      this.getEquipentType(params.id)
    })
  }

  getEquipentType(id) {
    this.loader1 = true;
    this._equipmentService.getEquipmentsGroup(id).subscribe(res => {
      this.equipmentGroup = res;
      this.selectedEquipment = res[0];
      this.loader1 = false;
    })
  }

  hasSpecificAttribute(attributeValues: any[]) {
    let hasSpecific = false;
    attributeValues.forEach(element => {
      if (element.attribute.isSpecific) {
        hasSpecific = true;
        return;
      }
    });
    return hasSpecific;
  }

  // onCamera(item) {
  //   console.log(item);
  //   // this.camApi.getSingleCameraDetail(item);
  //   this.router.navigate(['/home/camera/' + item.id])
  // }
  getEquipmentCount(item) {
    this.process = true;
    this.typeName = item.equipmentTypeCode
    this._equipmentService.getEquipmentCountitem(item.groupId).subscribe(res => {
      this.process = false;
      this.countGroupValue = res;

    })
  }

  deleteEquipment(item, index) {
    swal({
      text: "Are you sure you want to delete equipment?",
      icon: "warning",
      closeOnClickOutside: false,
      buttons: ['Yes', 'No'],
      dangerMode: true,
    }).then((willDelete => {
      if (!willDelete) {
        this._equipmentService.deleteEquipment(item.groupId).subscribe(res => {
          this.equipmentGroup.splice(index, 1)
          swal({
            text: "Equipment deleted successfully !",
            icon: "success",
            timer: 1000,
            buttons: [false],
          })
        })
      }
    }))

  }

  routeCreatepage() {
    this.router.navigate(['/home/equipments/add', this.selectedEquipment.equipmentTypeId])
  }

}
