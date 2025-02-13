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
  isAdmin: boolean = false; // Tracks if the user is an admin

  constructor(
    private authService: AuthService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.fetchDashboardData();
    this.checkAdminRole();
  }

  fetchDashboardData(): void {
    this.apiService.getDashboardData().subscribe({
      next: (data) => {
        this.user = data.user;
        this.recentClothingItems = data.recentClothingItems;
        this.recentTrades = data.recentTrades;
      },
      error: (err) => {
        this.error = 'Failed to load dashboard data.';
        console.error(err);
      },
    });
  }

  checkAdminRole(): void {
    this.authService.getAuthenticatedUser().subscribe({
      next: (user) => {
        this.isAdmin = user.roles.includes('ADMIN');
      },
      error: () => {
        this.isAdmin = false;
      },
    });
  }


  generateReport(): void {
    this.apiService.getTradeReport().subscribe({
      next: (reportData) => {
        const csvContent = this.convertToCSV(reportData);
        this.downloadCSV(csvContent, 'trade-report.csv');
      },
      error: (err) => {
        console.error('Error generating report:', err);
        alert('Failed to generate report. Please try again.');
      },
    });
  }

  private convertToCSV(data: any[]): string {
    const headers = ['ID', 'Status', 'Initiator', 'Receiver', 'Item', 'Date'];
    const rows = data.map((row) => [
      row.id,
      row.status,
      row.initiatorUsername,
      row.receiverUsername,
      row.itemTitle,
      row.tradeDate,
    ]);
    return [headers, ...rows]
      .map((row) => row.join(','))
      .join('\n');
  }

  private downloadCSV(content: string, filename: string): void {
    const blob = new Blob([content], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);
    link.setAttribute('href', url);
    link.setAttribute('download', filename);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  onLogout(): void {
    this.authService.logout();
  }
}
