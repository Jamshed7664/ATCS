import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';
@Injectable({
  providedIn: 'root'
})
export class IncidentService {

  constructor(private api: ApiService) { }

  getIncidentsByType(locationId, cameraId, type) {
    return this.api.get(`api/location/${locationId}/camera/${cameraId}/incidents?incidentType=${type}`);
  }

  getSiblingCameras(locationId, cameraId) {
    return this.api.get(`api/location/${locationId}/camera/${cameraId}/incident-cameras`)
  }

  saveCameraIncidents(locationId, cameraId, data) {
    return this.api.post(`api/location/${locationId}/camera/${cameraId}/incidents`, data)
  }
}
