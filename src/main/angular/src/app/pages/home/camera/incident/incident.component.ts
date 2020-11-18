import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-incident',
  templateUrl: './incident.component.html',
  styleUrls: ['./incident.component.scss']
})
export class IncidentComponent implements OnInit {
  paramsId

  constructor(private route: Router,private activateroute:ActivatedRoute) { }
  ngOnInit() {
    this.queryParams();

  }

  queryParams() {
    this.activateroute.params.subscribe(params => {
      this.paramsId = params.id
 console.log(this.paramsId);

    })

  }
  gotoVehicalIncident(){
  this.route.navigate(['/home/camera/'+this.paramsId+'/incident/vehicalIncident'])
  }
  gototrafficIncident(){
  this.route.navigate(['/home/camera/'+this.paramsId+'/incident/trafficIncident'])
  }
  gotoOtherIncident(){
  this.route.navigate(['/home/camera/'+this.paramsId+'/incident/otherIncident'])
  }
  gotowheatherIncident(){
  this.route.navigate(['/home/camera/'+this.paramsId+'/incident/wheatherIncident'])
  }
  getAllVaoues(){
    console.log('This value is');

  }
}
