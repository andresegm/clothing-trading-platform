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

        // Save the token, userId, and roles in localStorage
        if (response && response.token && response.userId && response.roles) {
          localStorage.setItem('authToken', response.token);
          localStorage.setItem('userId', response.userId.toString());
          localStorage.setItem('roles', JSON.stringify(response.roles));
          alert('Login successful!');
          // Redirect to the dashboard
          this.router.navigate(['/dashboard']);
        } else {
          console.error('Missing token, userId, or roles in response:', response);
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
