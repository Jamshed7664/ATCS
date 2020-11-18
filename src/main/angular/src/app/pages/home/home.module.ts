import { MapServiceService } from './../../services/mapService/map-service.service';
import { EquipmentMasterComponent } from './master-configuration/equipment-master/equipment-master.component';
import { LocationMasterComponent } from './master-configuration/location-master/location-master.component';
import { CanDeactivateGuard } from '../../services/can-DeactivateGuard-service';
import { AddUserComponent } from './add-user/add-user.component';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { RouterModule } from '@angular/router';
import { MapComponent } from './map/map.component';
import { UsersComponent } from './users/users.component';
import { RolesComponent } from './roles/roles.component';
import { AddRoleComponent } from './add-role/add-role.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RoleService } from 'src/app/services/api/role.service';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { AuthoritiesComponent } from './authorities/authorities.component';
import { ProfileComponent } from './profile/profile.component';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { MasterConfigurationComponent } from './master-configuration/master-configuration.component';
import { AddLocationComponent } from './location/add-location/add-location.component';
import { LocationComponent } from './location/location.component';
import { UpdateLocationComponent } from './location/update-location/update-location.component';
import { EquipmentComponent } from './equipment/equipment.component';
import { AddEquimentComponent } from './equipment/add-equiment/add-equiment.component';
import { UpdateEquipmentComponent } from './equipment/update-equipment/update-equipment.component';
import { CameraComponent } from './camera/camera.component';
import { LocationDetailComponent } from './map/location-detail/location-detail.component';
import { ImageUploadComponent } from './camera/image-upload/image-upload.component';
import { EquipmentMappingComponent } from './equipment-mapping/equipment-mapping.component';
import { ZoneCoordinatorComponent } from './camera/zone-coordinator/zone-coordinator.component';
import { EquipmentGroupComponent } from './equipment/equipment-group/equipment-group.component';
import { CameraCoordinatesComponent } from './camera/camera-coordinates/camera-coordinates.component';
import { IncidentDefinitionComponent } from './camera/incident-definition/incident-definition.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    Ng2SearchPipeModule,

    RouterModule.forChild([
      {
        path: '', component: HomeComponent,
        children: [
          { path: '', redirectTo: 'map' },
          {
            path: 'map', component: MapComponent,
            children: [{ path: 'location/:id', component: LocationDetailComponent }]
          },
          { path: 'location/:id/equipment-mapping', component: EquipmentMappingComponent },
          { path: 'roles', component: RolesComponent },
          { path: 'users', component: UsersComponent, pathMatch: 'full' },
          { path: 'addrole/:id', canDeactivate: [CanDeactivateGuard], component: AddRoleComponent },
          { path: 'addrole', canDeactivate: [CanDeactivateGuard], component: AddRoleComponent },
          { path: 'changesPassword', canDeactivate: [CanDeactivateGuard], component: ChangePasswordComponent },
          { path: 'user/:id', canDeactivate: [CanDeactivateGuard], component: AddUserComponent },
          { path: 'user', canDeactivate: [CanDeactivateGuard], component: AddUserComponent },
          { path: 'profile', component: ProfileComponent },
          { path: 'locations', component: LocationComponent },
          { path: 'locations/add', canDeactivate: [CanDeactivateGuard], component: AddLocationComponent },
          { path: 'locations/:id', canDeactivate: [CanDeactivateGuard], component: UpdateLocationComponent },
          {
            path: 'equipments', component: EquipmentComponent,
            children: [
              { path: 'group/:id', component: EquipmentGroupComponent }
            ]
          },
          { path: 'equipments/add/:id', canDeactivate: [CanDeactivateGuard], component: AddEquimentComponent },
          { path: 'equipments/:id', canDeactivate: [CanDeactivateGuard], component: UpdateEquipmentComponent },
          { path: 'imageUpload/:id', component: ImageUploadComponent },
          { path: 'zoneCoordinator/:id', component: ZoneCoordinatorComponent },
          { path: 'mainCoordinate/:id', component: CameraCoordinatesComponent },
          { path: 'incident-definition/:id', component: IncidentDefinitionComponent },
          {
            path: 'masterConfiguration', children: [
              { path: '', component: MasterConfigurationComponent },
              { path: 'locationmaster', canDeactivate: [CanDeactivateGuard], component: LocationMasterComponent },
              { path: 'equipmentmaster', canDeactivate: [CanDeactivateGuard], component: EquipmentMasterComponent },
            ]
          },
        ]
      }
    ])
  ],
  providers: [RoleService, MapServiceService],
  declarations: [
    HomeComponent,
    MapComponent,
    UsersComponent,
    RolesComponent,
    AddRoleComponent,
    ChangePasswordComponent,
    AddUserComponent,
    AuthoritiesComponent,
    ProfileComponent,
    EquipmentMasterComponent,
    LocationMasterComponent,
    MasterConfigurationComponent,
    AddLocationComponent,
    LocationComponent,
    UpdateLocationComponent,
    EquipmentComponent,
    AddEquimentComponent,
    UpdateEquipmentComponent,
    CameraComponent,
    LocationDetailComponent,
    EquipmentMappingComponent,
    ImageUploadComponent,
    ZoneCoordinatorComponent,
    EquipmentGroupComponent,
    CameraCoordinatesComponent,
    IncidentDefinitionComponent
  ]
})
export class HomeModule { }
