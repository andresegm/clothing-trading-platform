import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from "../../environments/environment";

interface SearchResponse {
  items: any[];
  totalPages: number;
  totalItems: number;
  currentPage: number;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient) {}

  private handleAuthError(error: HttpErrorResponse): Observable<any> {
    if (error.status === 401) {
      return throwError(() => new Error('Session expired. Please log in again.'));
    }
    return throwError(() => error);
  }

  getDashboardData(): Observable<any> {
    return this.http.get(`${this.baseUrl}/dashboard/data`, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }

  searchClothingItems(filters: {
    condition: string;
    size: string;
    minPrice: null;
    maxPrice: null;
    page: number;
    pageSize: number;
    title: string;
    brand: string;
  }): Observable<SearchResponse> {

    let params = new HttpParams();
    Object.entries(filters).forEach(([key, value]) => {
      if (value !== null && value !== undefined) {
        params = params.set(key, value.toString());
      }
    });

    return this.http.get<SearchResponse>(`${this.baseUrl}/clothing-items/filter`, { withCredentials: true, params }).pipe(
      catchError(this.handleAuthError)
    );
  }

  getMyClothingItems(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/users/${userId}/clothing-items`, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }

  addClothingItem(item: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/clothing-items`, item, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }

  updateClothingItem(itemId: number, updatedItem: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/clothing-items/${itemId}`, updatedItem, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }

  getUserTrades(): Observable<any> {
    return this.http.get(`${this.baseUrl}/trades`, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }

  updateTradeStatus(tradeId: number, status: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/trades/${tradeId}/status`, { status }, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }

  initiateTrade(itemId: number, status: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/trades`, { itemId, status }, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }

  getItemDetails(itemId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/clothing-items/${itemId}`, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }

  checkExistingTrade(itemId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/trades/check?itemId=${itemId}`, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }

  getTradeReport(status?: string, startDate?: string, endDate?: string): Observable<any[]> {
    let params = new HttpParams();
    if (status) params = params.set('status', status);
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    return this.http.get<any[]>(`${this.baseUrl}/trades/report`, { withCredentials: true, params }).pipe(
      catchError(this.handleAuthError)
    );
  }

  getUnreadNotifications(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/notifications/unread`, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }

  markNotificationsAsRead(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/notifications/mark-as-read`, {}, { withCredentials: true }).pipe(
      catchError(this.handleAuthError)
    );
  }
}
