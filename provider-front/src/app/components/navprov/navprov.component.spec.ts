import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavprovComponent } from './navprov.component';

describe('NavprovComponent', () => {
  let component: NavprovComponent;
  let fixture: ComponentFixture<NavprovComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavprovComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NavprovComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
