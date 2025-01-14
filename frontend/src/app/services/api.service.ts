import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // Login method
  login(data: { username: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/login`, data);
  }

  // Register method
  register(data: { username: string; email: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/register`, data);
  }

  // Fetch dashboard data
  getDashboardData(): Observable<any> {
    const rawToken = localStorage.getItem('authToken'); // Get the raw token from localStorage
    const token = typeof rawToken === 'string' ? rawToken : ''; // Ensures it's a string
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get(`${this.baseUrl}/dashboard/data`, { headers });
  }
}
