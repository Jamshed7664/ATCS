import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicalIncidentComponent } from './vehical-incident.component';

describe('VehicalIncidentComponent', () => {
  let component: VehicalIncidentComponent;
  let fixture: ComponentFixture<VehicalIncidentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VehicalIncidentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VehicalIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
