import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root'
})
export class CameraService {
  http: any;
  httpRequestUrl: string;

  constructor(private api: ApiService) { }
  saveCamera(id, object: any) {
    return this.api.post('api/camera/' + id, object)
  }

  cameraDetail(id) {
    return this.api.get('api/camera/' + id)
  }
  uploadimage(cameraId, object) {
    const fd = new FormData();
    fd.append('image', object, object.name)
    return this.api.post('api/camera/' + cameraId + '/image', fd)
  }
  getImage(id: any) {
    return this.api.get('api/camera/' + id + '/image')
  }
  getCameradetail(cameraId: any) {
    return this.api.get('api/camera/' + cameraId)
  }
  saveMainCoordinate(cameraId, object) {
    return this.api.post('api/camera/' + cameraId + '/image/' + cameraId + '/coordinates/main', object)
  }

  saveZoneCoordinate(cameraId, object) {
    return this.api.post('api/camera/' + cameraId + '/image/' + cameraId + '/coordinates/zone', object)
  }

  getMaincoordinate(cameraId: any) {
    return this.api.get('api/camera/' + cameraId + '/image/' + cameraId + '/coordinates/main')
  }

  getZonecoordinate(cameraId: any) {
    return this.api.get('api/camera/' + cameraId + '/image/' + cameraId + '/coordinates/zones')
  }

  deleteCoordinate(cameraId: any, id: any) {
    return this.api.delete('api/camera/' + cameraId + '/image/' + cameraId + '/coordinate/' + id)
  }

  getCameraCoordinate(cameraId: any) {
    return this.api.get('api/camera/' + cameraId + '/image/' + cameraId + '/coordinates')

  }

  saveCameraCoordinate(cameraId: any, object) {
    return this.api.post('api/camera/' + cameraId + '/image/' + cameraId + '/coordinates', object)

  }
}
