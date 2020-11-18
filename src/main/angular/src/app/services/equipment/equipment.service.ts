import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root'
})
export class EquipmentService {

  saveEquipmentMapping(locationId: any, data: any): any {
    return this.api.post(`api/location/${locationId}/equipment`, data)
  }
  constructor(private api: ApiService) { }

  getEquipmentCountitem(id) {
    return this.api.get('api/equipments/' + id + '/equipments');
  }
  getAllEquipments() {
    return this.api.get('api/equipment/types-count');
  }
  getAllEquipments1() {
    return this.api.get('api/equipment/types-count');
  }
  getEquipmentsGroup(id) {
    return this.api.get('api/equipments?equipmentTypeId=' + id);
  }
  getEquipmentTypes() {
    return this.api.get('api/equipment/types');
  }

  addEquipment(data) {
    return this.api.post('api/equipment', data)
  }
  deleteEquipment(groupId) {
    return this.api.delete('api/equipment/' + groupId)
  }
  getEquipmentById(id: any) {
    return this.api.get('api/equipments/' + id)
  }
  updateEquipment(groupId, object) {
    return this.api.put('api/equipment/' + groupId, object)
  }
  getEquipmentMappingByLocationId(locationId) {
    return this.api.get(`api/location/${locationId}/equipment-mapping`)
  }

  getEquipmentDetail(id) {
    return this.api.get('api/equipment/' + id)
  }

  getMappedEquipmentByGroupId(map, groupId) {
    return this.api.get(`api/location/${map.locationId}/equipments/${groupId}${map.armId ? '?armId=' + map.armId : ''}${map.laneId ? '&laneId=' + map.laneId : ''}`);
  }
}
