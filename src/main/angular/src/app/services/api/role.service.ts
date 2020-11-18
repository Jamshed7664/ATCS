import { ApiService } from 'src/app/services/api/api.service';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  url: string = 'http://localhost:8081';
  constructor(private http:ApiService,private https:HttpClient) { }


  getRole()
  {
    return this.http.get(this.url+'/api/authorities')
  }
 
}
