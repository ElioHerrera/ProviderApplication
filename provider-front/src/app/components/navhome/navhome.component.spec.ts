import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NavhomeComponent } from './navhome.component';

describe('NavhomeComponent', () => {
  let component: NavhomeComponent;
  let fixture: ComponentFixture<NavhomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavhomeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NavhomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
