import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WheatherIncidentComponent } from './wheather-incident.component';

describe('WheatherIncidentComponent', () => {
  let component: WheatherIncidentComponent;
  let fixture: ComponentFixture<WheatherIncidentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WheatherIncidentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WheatherIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
