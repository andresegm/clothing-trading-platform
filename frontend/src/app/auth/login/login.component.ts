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

        // Save the token in localStorage (assuming the token is in `response.token`)
        if (response && response.token) {
          localStorage.setItem('authToken', response.token);
          alert('Login successful!');
          // Redirect to the dashboard
          this.router.navigate(['/dashboard']);
        } else {
          console.error('No token received from backend:', response);
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
