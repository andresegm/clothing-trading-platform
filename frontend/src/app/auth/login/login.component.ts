import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(private apiService: ApiService) {}

  onSubmit() {
    const loginData = { username: this.username, password: this.password };
    this.apiService.login(loginData).subscribe({
      next: (response) => {
        console.log('Login successful:', response);
      },
      error: (err) => {
        console.error('Login failed:', err);
      }
    });
  }
}
