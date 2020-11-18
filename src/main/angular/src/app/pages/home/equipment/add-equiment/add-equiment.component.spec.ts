import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddEquimentComponent } from './add-equiment.component';

describe('AddEquimentComponent', () => {
  let component: AddEquimentComponent;
  let fixture: ComponentFixture<AddEquimentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddEquimentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddEquimentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
