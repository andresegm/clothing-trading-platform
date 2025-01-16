import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {catchError, Observable, tap} from 'rxjs';

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

  searchClothingItems(filters: {
    title?: string;
    brand?: string;
    size?: string;
    condition?: string;
    minPrice?: number | null;
    maxPrice?: number | null;
  }): Observable<any[]> {
    let params = new HttpParams();

    // Add parameters only if they have a value
    if (filters.title) params = params.set('title', filters.title);
    if (filters.brand) params = params.set('brand', filters.brand);
    if (filters.size) params = params.set('size', filters.size);
    if (filters.condition) params = params.set('condition', filters.condition);
    if (filters.minPrice != null) params = params.set('minPrice', filters.minPrice.toString());
    if (filters.maxPrice != null) params = params.set('maxPrice', filters.maxPrice.toString());

    const rawToken = localStorage.getItem('authToken');
    const token = typeof rawToken === 'string' ? rawToken : '';

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get<any[]>(`${this.baseUrl}/clothing-items/filter`, { headers, params });
  }

  // Get items for the logged-in user
  getMyClothingItems(userId: number): Observable<any[]> {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get<any[]>(`${this.baseUrl}/users/${userId}/clothing-items`, { headers }).pipe(
      tap((response) => console.log('API response:', response)), // Log response
      catchError((error) => {
        console.error('API call failed:', error); // Log error
        throw error;
      })
    );
  }

// Add a new clothing item
  addClothingItem(item: any): Observable<any> {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.post(`${this.baseUrl}/clothing-items`, item, { headers });
  }

  updateClothingItem(itemId: number, updatedItem: any): Observable<any> {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put(`${this.baseUrl}/clothing-items/${itemId}`, updatedItem, { headers });
  }


  getUserTrades(): Observable<any> {
    const token = localStorage.getItem('authToken'); // Ensure consistent token key
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get(`${this.baseUrl}/trades`, { headers });
  }

  updateTradeStatus(tradeId: number, status: string): Observable<any> {
    const body = { status };
    const token = localStorage.getItem('authToken'); // Ensure consistent token key usage
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.put(`${this.baseUrl}/trades/${tradeId}/status`, body, { headers });
  }

  initiateTrade(itemId: number, status: string): Observable<any> {
    const token = localStorage.getItem('authToken'); // Get the token from localStorage
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const body = { itemId, status }; // Payload for the API request

    return this.http.post(`${this.baseUrl}/trades`, body, { headers });
  }

  getItemDetails(itemId: number): Observable<any> {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get(`${this.baseUrl}/clothing-items/${itemId}`, { headers });
  }

  checkExistingTrade(itemId: number): Observable<boolean> {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<boolean>(`${this.baseUrl}/trades/check?itemId=${itemId}`, { headers });
  }
}
