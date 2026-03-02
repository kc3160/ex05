import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TagMenuComponent } from './tag-menu.component';

describe('TagMenuComponent', () => {
  let component: TagMenuComponent;
  let fixture: ComponentFixture<TagMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TagMenuComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TagMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
