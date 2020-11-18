import { MasterConfigurationComponent } from './master-configuration.component';
import { Router, RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LocationMasterComponent } from './location-master/location-master.component';
import { EquipmentMasterComponent } from './equipment-master/equipment-master.component';

@NgModule({
  imports: [
    CommonModule,
  ],

  declarations: [LocationMasterComponent, EquipmentMasterComponent]
})
export class MasterConfigurationModule { }
