import swal from 'sweetalert';
import { Router } from '@angular/router';
import { LoginComponent } from './../../pages/login/login.component';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { throwError } from "rxjs";
import { map, catchError } from "rxjs/operators";
declare const controlRoomWeb;
@Injectable({
  providedIn: 'root'
})
export class ApiService {
  curentUserAuth = []
  rolesItem: any;
  basicToken: string = '';
  url: string = String(controlRoomWeb.ipAddress) + ':' + String(controlRoomWeb.port);

  constructor(public http: HttpClient, private router: Router) { }

  private getAccessToken() {
    const basicToken = 'Aligarh:nxtlife';
    return !localStorage.getItem('access_token') ? 'Basic ' + btoa(this.basicToken + ":nxtlife") : 'Bearer ' + localStorage.getItem('access_token');
  }

  setBasicToken(token) {
    this.basicToken = token;
  }

  private addHeaders(optionalHeaders?: HttpHeaders) {

    let requestHeaders = new HttpHeaders()
      .set('Authorization', this.getAccessToken());

    if (optionalHeaders) {
      for (const header of optionalHeaders.keys()) {
        requestHeaders = requestHeaders.append(header, optionalHeaders.get(header));
      }
    }
    return requestHeaders;
  }

  get(endpoint: string, params?: any, reqOpts?: any) {
    const headers = this.addHeaders(reqOpts);
    return this.http.get(this.url + '/' + endpoint, { headers: headers, observe: 'response' })
      .pipe(
        map(this.extractData),
        catchError(this.handleError)
      );
  }

  post(endpoint: string, body: any, reqOpts?: any) {
    const headers = this.addHeaders(reqOpts);
    return this.http.post(this.url + '/' + endpoint, body, { headers: headers, observe: 'response' })
      .pipe(
        map(this.extractData),
        catchError(this.handleError)
      );
  }

  put(endpoint: string, body: any, reqOpts?: any) {
    const headers = this.addHeaders(reqOpts);
    return this.http.put(this.url + '/' + endpoint, body, { headers: headers, observe: 'response' })
      .pipe(
        map(this.extractData),
        catchError(this.handleError)
      );
  }

  delete(endpoint: string, reqOpts?: any) {
    const headers = this.addHeaders(reqOpts);
    return this.http.delete(this.url + '/' + endpoint, { headers: headers, observe: 'response' })
      .pipe(
        map(this.extractData),
        catchError(this.handleError)
      );
  }

  patch(endpoint: string, body: any, reqOpts?: any) {
    return this.http.patch(this.url + '/' + endpoint, body, reqOpts)
  }

  extractData = (response: HttpResponse<any>) => {
    return response.body || response.status;
  };
  // Generate Password
  getGeneratedPassword(data: any): Observable<any> {
    let requestHeaders = new HttpHeaders().set('clientId', data.client)
    return this.http.get(this.url + '/forgot-password/' + data.fpuser, { headers: requestHeaders, observe: 'response' })
      .pipe(
        map(this.extractData),
        catchError(this.handleError)
      );
  }

  forgotPassword(formValue: any) {
    let requestHeaders = new HttpHeaders().set('clientId', formValue.client)
    return this.http.post(this.url + '/forgot-password', formValue, { headers: requestHeaders, observe: 'response' })
  }

  handleError = (errorResponse: HttpErrorResponse) => {
    if (errorResponse.status === 401) {
      localStorage.clear();
      this.showError("Invalid Credential or Token Expired")
      this.router.navigate(['/login']);
    } else if (errorResponse.status === 0) {
      this.showError('You have not an active internet Connection or Server not Responding')
    } else {
      this.showError((errorResponse.error.message || errorResponse.error.status || errorResponse.error.error) || "Something went wrong");
    }
    return throwError(errorResponse);
  };

  async showError(message) {
    swal("Error", message, "warning");

  }

  // Get value of Array;
  myFormValue: any = [];
  afterAddValue(item: any) {
    this.myFormValue = item;
  }

  // get Role value to to store in array
  updateRole(items: any) {
    this.rolesItem = items;
  }

  // Clear arry Value
  resetMyFormVAlue() {
    this.myFormValue = null;
  }

  // Claer Role object value
  resetRoleModel() {
    this.rolesItem = null;
  }
  currentUserAuthorities(item) {
    this.curentUserAuth = item
  }

  updateLocation: any;
  // Update Location Value
  updateLocationFormValue(res) {
    this.updateLocation = res;
    console.log('in service');
    console.log(res);
  }
  cameraDetail: any;
  getSingleCameraDetail(object) {
    this.cameraDetail = object;
  }
}
