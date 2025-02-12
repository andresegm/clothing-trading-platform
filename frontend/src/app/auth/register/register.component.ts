import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
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

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (this.username.length < 3 || this.username.length > 20) {
      alert('Username must be between 3 and 20 characters.');
      return;
    }

    if (!this.isValidEmail(this.email)) {
      alert('Please enter a valid email address.');
      return;
    }

    if (this.password.length < 8) {
      alert('Password must be at least 8 characters long.');
      return;
    }

    const registerData = {
      username: this.username,
      email: this.email,
      password: this.password,
    };

    this.authService.register(registerData).subscribe({
      next: () => {
        console.log('Registration successful');
        alert('Registration successful! You can now log in.');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Registration failed:', err);
        alert(err?.error?.message || 'Registration failed. Please try again.');
      },
    });
  }

  private isValidEmail(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }
}
