import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OtherIncidentComponent } from './other-incident.component';

describe('OtherIncidentComponent', () => {
  let component: OtherIncidentComponent;
  let fixture: ComponentFixture<OtherIncidentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OtherIncidentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OtherIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
