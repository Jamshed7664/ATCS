import { async, ComponentFixture, TestBed } from '@angular/core/testing';

<<<<<<< HEAD:src/main/angular/src/app/pages/home/camera/incident/traffic-incident/traffic-incident.component.spec.ts
import { TrafficIncidentComponent } from './traffic-incident.component';

describe('TrafficIncidentComponent', () => {
  let component: TrafficIncidentComponent;
  let fixture: ComponentFixture<TrafficIncidentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TrafficIncidentComponent ]
=======
import { CameraCoordinatesComponent } from './camera-coordinates.component';

describe('CameraCoordinatesComponent', () => {
  let component: CameraCoordinatesComponent;
  let fixture: ComponentFixture<CameraCoordinatesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CameraCoordinatesComponent ]
>>>>>>> db9b8d414c5539923cd6e4a173cc57e39e7726af:src/main/angular/src/app/pages/home/camera/camera-coordinates/camera-coordinates.component.spec.ts
    })
    .compileComponents();
  }));

  beforeEach(() => {
<<<<<<< HEAD:src/main/angular/src/app/pages/home/camera/incident/traffic-incident/traffic-incident.component.spec.ts
    fixture = TestBed.createComponent(TrafficIncidentComponent);
=======
    fixture = TestBed.createComponent(CameraCoordinatesComponent);
>>>>>>> db9b8d414c5539923cd6e4a173cc57e39e7726af:src/main/angular/src/app/pages/home/camera/camera-coordinates/camera-coordinates.component.spec.ts
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
