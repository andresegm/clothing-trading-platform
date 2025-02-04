import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (this.username.length < 3 || this.username.length > 20) {
      alert('Username must be between 3 and 20 characters.');
      return;
    }

    if (this.password.length < 8) {
      alert('Password must be at least 8 characters long.');
      return;
    }

    const loginData = { username: this.username, password: this.password };

    this.authService.login(loginData).subscribe({
      next: (response) => {
        console.log('Login successful:', response);
        if (response?.token) {
          localStorage.setItem('jwtToken', response.token);
          if (response.userId) {
            localStorage.setItem('userId', response.userId.toString());
          }
          if (response.roles) {
            localStorage.setItem('roles', JSON.stringify(response.roles));
          }
          alert('Login successful!');
          this.router.navigate(['/dashboard']);
        } else {
          alert('Login failed. Please try again.');
        }
      },
      error: (err) => {
        console.error('Login failed:', err);
        alert('Invalid username or password. Please try again.');
      },
    });
  }
}
