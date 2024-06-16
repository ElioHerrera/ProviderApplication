import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { NavbarComponent } from './components/navbar/navbar.component';
import { SignupComponent } from './pages/signup/signup.component';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { HomeComponent } from './pages/home/home.component';



@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
     RouterOutlet,
     NavbarComponent,
     SignupComponent,
     MatButtonModule,
     MatFormFieldModule,
     HomeComponent
     
    ],
    

  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'provider-front';
 
}
