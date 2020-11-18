import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  savedToken: string;
  myuserInfo: any;
  constructor(
    private api: ApiService,
  ) { }

  getClients() {
    return this.api.get("clients");
  }

  login(data: any) {
    this.api.setBasicToken(data.client);
    return this.api.post(`oauth/token?grant_type=password&username=${data.username}&password=${data.password}`, {});
  }

  isLoggedIn() {
    return localStorage.getItem('access_token') ? true : false;
  }

  saveToken(token: string) {
    localStorage.setItem('access_token', token);

  }

  fetchUserDetails(useGlobalErrorHandler: boolean) {
    return this.api.get('api/me');
  }
  logout() {
    return this.api.get('api/me/logout');
  }
  saveUserDetails(userInfo: any) {
    localStorage.setItem('userInfo', JSON.stringify(userInfo));
  }

  changePassword(newPaswordValue: any) {
    return this.api.put('api/me/password', newPaswordValue)
  }


  getAuthority() {
    return this.api.get('api/authorities');
  }
  // create new Role
  onCreateRole(name: string, authorityIds: any) {
    return this.api.post('api/role', { name, authorityIds })
  }
  // get Role
  getRole() {
    return this.api.get('api/roles')
  }
  //get All user
  getUSers() {
    return this.api.get('api/users');
  }
  // Save user
  saveuser(userData: any) {
    return this.api.post('api/user', userData)
  }
  // Get single user value
  getSingleUserValue(id: any) {
    return this.api.get('api/me')
  }
  // Update User
  updateUser(id, mymodel) {
    return this.api.put('api/user/' + id, mymodel)
  }
  // logged in user
  loogedInUser() {
    return this.api.get('api/me');
  }
  // Update Role
  updateRole(id: any, itemvalue: any) {
    return this.api.put('api/role/' + id, itemvalue)
  }
  // delete Single USer
  deleteSingleUser(id: any) {
    return this.api.delete('api/user/' + id)
  }
  // delete Single Role
  deleteSingleRole(id: any) {
    return this.api.delete('api/role/' + id)
  }
  // Authority descriptiom
  getAuthorityDescription(id: any, name: any) {
    return this.api.get('api/role/' + id + '/' + name)
  }
  // Activate User
  ativateUser(id: any) {
    return this.api.put('api/user/' + id + '/activate', {})
  }
  // Activate Authorities
  ativateRole(id: any) {
    return this.api.put('api/role/' + id + '/activate', {})
  }
  // get location type
  getLocation() {
    return this.api.get('api/location/types');
  }
  // SaveLocation TypeField
  List: any;
  onSaveLocation(id: any, value: any) {
    this.List = value;
    return this.api.post('api/location/type/' + id + '/attributes', value);
  }
  // find all equipments type
  getAllEquipmentType() {
    return this.api.get('api/equipment/types')
  }
  // Save  Equipment type field
  onsaveEquipmentType(id: any, value) {
    return this.api.post('api/equipment/type/' + id + '/attributes', value)
  }
  // Find all the equipment type fields by equipment type id
  getAllEquipments(id: any) {
    return this.api.get('api/equipment/type/' + id + '/fields')
  }
  // delete location type
  deletLocationType(locationTypeId, locationTypeFieldId) {
    return this.api.delete('api/location/type/' + locationTypeId + '/attribute/' + locationTypeFieldId)
  }
  // delete equipment type
  deleteEquipmentType(equipmentTypeId, equipmentTypeFieldId) {
    return this.api.delete('api/equipment/type/' + equipmentTypeId + '/attribute/' + equipmentTypeFieldId)
  }
  // get location type for add location
  getLocationType(){
    return this.api.get('api/location/types')
  }
  // service to save Location
  saveLocation(object:any){
    return this.api.post('api/location',object)
  }
  // get Direction-
  getDirection(){
    return this.api.get('api/directions')
  }
  // get Vehical Type
  getVehicalType(){
    return this.api.get('api/vehicle/types')
  }
  // Find by location id
  getLocationById(id:any){
    return this.api.get('api/location/'+id)
  }
  // get all locations
  getAllLocations(){
    return this.api.get('api/locations')
  }
  // update locations
  updateLocation(id,value:any){
    return this.api.put('api/location/'+id,value)

  }
  // delete Location
  deleteLocation(id:any){
    return this.api.delete('api/location/'+id)
  }
}
