import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(private apiService: ApiService, private router: Router) {}

  onSubmit() {
    const loginData = { username: this.username, password: this.password };
    this.apiService.login(loginData).subscribe({
      next: (response) => {
        console.log('Login successful:', response);
        alert('Login successful!');
        // Save the token in localStorage
        localStorage.setItem('authToken', response);
        // Redirect to a protected page or dashboard
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Login failed:', err);
        alert('Invalid username or password. Please try again.');
      },
    });
  }
}
