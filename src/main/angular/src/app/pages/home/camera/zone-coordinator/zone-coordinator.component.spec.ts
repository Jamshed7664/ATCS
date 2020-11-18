import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ZoneCoordinatorComponent } from './zone-coordinator.component';

describe('ZoneCoordinatorComponent', () => {
  let component: ZoneCoordinatorComponent;
  let fixture: ComponentFixture<ZoneCoordinatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ZoneCoordinatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ZoneCoordinatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
