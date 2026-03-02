import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserFormManagerComponent } from './user-form-manager.component';

describe('UserFormManagerComponent', () => {
  let component: UserFormManagerComponent;
  let fixture: ComponentFixture<UserFormManagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserFormManagerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserFormManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
