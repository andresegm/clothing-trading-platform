import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  user: any; // Stores user info
  recentClothingItems: any[] = []; // Stores recent clothing items
  recentTrades: any[] = []; // Stores recent trades
  error: string | null = null; // Error handling

  constructor(
    private authService: AuthService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.fetchDashboardData();
  }

  fetchDashboardData(): void {
    this.apiService.getDashboardData().subscribe(
      (data) => {
        // Populate data into respective variables
        this.user = data.user;
        this.recentClothingItems = data.recentClothingItems;
        this.recentTrades = data.recentTrades;
      },
      (err) => {
        // Handle errors
        this.error = 'Failed to load dashboard data.';
        console.error(err);
      }
    );
  }

  onLogout(): void {
    this.authService.logout(); // Call logout from AuthService
  }
}
