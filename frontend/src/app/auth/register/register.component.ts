import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';

  constructor(private apiService: ApiService) {}

  onSubmit() {
    const registerData = { username: this.username, email: this.email, password: this.password };
    this.apiService.register(registerData).subscribe({
      next: (response) => {
        console.log('Registration successful:', response);
        // Redirect user to the login page
      },
      error: (err) => {
        console.error('Registration failed:', err);
        // Show an error message to the user
      }
    });
  }
}
