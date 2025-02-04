import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { catchError, Observable, tap } from 'rxjs';
import { environment } from "../../environments/environment";
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): { headers: HttpHeaders } {
    const token = this.authService.getToken();
    return { headers: new HttpHeaders({ 'Authorization': `Bearer ${token}` }) };
  }

  getDashboardData(): Observable<any> {
    return this.http.get(`${this.baseUrl}/dashboard/data`, this.getHeaders());
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
    Object.entries(filters).forEach(([key, value]) => {
      if (value !== null && value !== undefined) {
        params = params.set(key, value.toString());
      }
    });

    return this.http.get<any[]>(`${this.baseUrl}/clothing-items/filter`, { ...this.getHeaders(), params });
  }

  getMyClothingItems(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/users/${userId}/clothing-items`, this.getHeaders());
  }

  addClothingItem(item: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/clothing-items`, item, this.getHeaders());
  }

  updateClothingItem(itemId: number, updatedItem: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/clothing-items/${itemId}`, updatedItem, this.getHeaders());
  }

  getUserTrades(): Observable<any> {
    return this.http.get(`${this.baseUrl}/trades`, this.getHeaders());
  }

  updateTradeStatus(tradeId: number, status: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/trades/${tradeId}/status`, { status }, this.getHeaders());
  }

  initiateTrade(itemId: number, status: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/trades`, { itemId, status }, this.getHeaders());
  }

  getItemDetails(itemId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/clothing-items/${itemId}`, this.getHeaders());
  }

  checkExistingTrade(itemId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/trades/check?itemId=${itemId}`, this.getHeaders());
  }

  getTradeReport(status?: string, startDate?: string, endDate?: string): Observable<any[]> {
    let params = new HttpParams();
    if (status) params = params.set('status', status);
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    return this.http.get<any[]>(`${this.baseUrl}/trades/report`, { ...this.getHeaders(), params });
  }

  getUnreadNotifications(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/notifications/unread?userId=${userId}`, this.getHeaders());
  }

  markNotificationsAsRead(userId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/notifications/mark-as-read?userId=${userId}`, {}, this.getHeaders());
  }
}
