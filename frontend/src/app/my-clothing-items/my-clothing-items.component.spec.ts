import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyClothingItemsComponent } from './my-clothing-items.component';

describe('MyClothingItemsComponent', () => {
  let component: MyClothingItemsComponent;
  let fixture: ComponentFixture<MyClothingItemsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyClothingItemsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyClothingItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
