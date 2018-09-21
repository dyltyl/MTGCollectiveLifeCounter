import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GuestCreationComponent } from './guest-creation.component';

describe('GuestCreationComponent', () => {
  let component: GuestCreationComponent;
  let fixture: ComponentFixture<GuestCreationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GuestCreationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GuestCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
