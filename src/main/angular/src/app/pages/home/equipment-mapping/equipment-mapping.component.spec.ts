import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EquipmentMappingComponent } from './equipment-mapping.component';

describe('EquipmentMappingComponent', () => {
  let component: EquipmentMappingComponent;
  let fixture: ComponentFixture<EquipmentMappingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EquipmentMappingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EquipmentMappingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
