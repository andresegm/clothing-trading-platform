import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private router: Router) {}

  // Logout method
  logout(): void {
    localStorage.removeItem('authToken'); // Clear the token
    this.router.navigate(['/login']); // Redirect to login page
  }

  // Check if the user is logged in
  isLoggedIn(): boolean {
    const token = localStorage.getItem('authToken'); // Check for token
    return !!token; // Return true if token exists
  }
}
