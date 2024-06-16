import { Component, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {
  user: any;

  constructor(private route: ActivatedRoute, private userService: UserService) { }

  ngOnInit(): void {
    const username = this.route.snapshot.paramMap.get('username');
    if (username) {
      this.userService.obtenerPerfilUsuario(username).subscribe(
        (response: any) => {
          this.user = response;
        },
        (error) => {
          console.error('Error al obtener el perfil del usuario:', error);
        }
      );
    }
  }
}