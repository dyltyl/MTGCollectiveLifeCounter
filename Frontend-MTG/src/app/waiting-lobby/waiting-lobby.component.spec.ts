import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WaitingLobbyComponent } from './waiting-lobby.component';

describe('WaitingLobbyComponent', () => {
  let component: WaitingLobbyComponent;
  let fixture: ComponentFixture<WaitingLobbyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WaitingLobbyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WaitingLobbyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
