import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private authUrl = `${environment.apiUrl}/api/auth`;

  constructor(private http: HttpClient, private router: Router) {}

  getAuthenticatedUser(): Observable<any> {
    return this.http.get(`${this.authUrl}/user`, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }


  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post(`${this.authUrl}/login`, credentials, { withCredentials: true }).pipe(
      tap(() => console.log('User logged in successfully')),
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

  refreshToken(): Observable<any> {
    return this.http.post(`${this.authUrl}/refresh`, {}, { withCredentials: true }).pipe(
      tap(() => console.log('Token refreshed successfully')),
      catchError(error => {
        console.error('Token refresh failed:', error);
        this.logout();
        return throwError(() => new Error('Session expired. Please log in again.'));
      })
    );
  }

  logout(): void {
    this.http.post(`${this.authUrl}/logout`, {}, { withCredentials: true }).subscribe({
      next: () => {
        console.log('User logged out successfully');
        this.router.navigate(['/login']);
      },
      error: error => console.error('Logout error:', error),
    });
  }

  isLoggedIn(): Observable<boolean> {
    return this.http.get<boolean>(`${this.authUrl}/validate-session`, { withCredentials: true }).pipe(
      catchError(() => {
        return throwError(() => new Error('Session validation failed.'));
      })
    );
  }

  private handleAuthError(error: HttpErrorResponse): Observable<any> {
    if (error.status === 401) {
      return throwError(() => new Error('Session expired. Please log in again.'));
    }
    return throwError(() => error);
  }
}
