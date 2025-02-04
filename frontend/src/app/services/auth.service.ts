import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private authUrl = `${environment.apiUrl}/api/auth`; // Standardized

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post<{ token: string; userId: number; roles: string[] }>(
      `${this.authUrl}/login`,
      credentials
    ).pipe(
      tap(response => {
        console.log("Login Response:", response); // Debug login response

        if (response.token && response.userId) {
          localStorage.setItem('authToken', response.token);
          localStorage.setItem('userId', response.userId.toString());
          localStorage.setItem('roles', JSON.stringify(response.roles));
        } else {
          console.error("Invalid login response structure:", response);
        }
      }),
      catchError(error => {
        console.error('Login error:', error);
        return throwError(() => new Error('Login failed. Please check your credentials.'));
      })
    );
  }

  register(data: { username: string; email: string; password: string }): Observable<any> {
    return this.http.post(`${this.authUrl}/register`, data).pipe(
      catchError(error => {
        console.error('Registration error:', error);
        return throwError(() => new Error('Registration failed. Please try again.'));
      })
    );
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('authToken');
  }

  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('roles');
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  getRoles(): string[] {
    return JSON.parse(localStorage.getItem('roles') || '[]');
  }
}
