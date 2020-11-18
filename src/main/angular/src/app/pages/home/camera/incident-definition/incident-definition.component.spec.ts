import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IncidentDefinitionComponent } from './incident-definition.component';

describe('IncidentDefinitionComponent', () => {
  let component: IncidentDefinitionComponent;
  let fixture: ComponentFixture<IncidentDefinitionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IncidentDefinitionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IncidentDefinitionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
