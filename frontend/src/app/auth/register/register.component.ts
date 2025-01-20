import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';

  constructor(private apiService: ApiService, private router: Router) {}

  onSubmit() {
    if (!this.username || !this.email || !this.password) {
      alert('Please fill out all fields.');
      return;
    }

    const registerData = {
      username: this.username,
      email: this.email,
      password: this.password,
    };

    this.apiService.register(registerData).subscribe({
      next: (response) => {
        console.log('Registration successful:', response);
        alert('Registration successful! You can now log in.');
        this.router.navigate(['/login']); // Redirect to login page
      },
      error: (err) => {
        console.error('Registration failed:', err);
        alert('Registration failed. Please try again.');
      },
    });
  }
}
