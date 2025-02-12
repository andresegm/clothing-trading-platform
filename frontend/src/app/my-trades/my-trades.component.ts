import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Trade } from '../model/Trade';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-my-trades',
  templateUrl: './my-trades.component.html',
  styleUrls: ['./my-trades.component.scss']
})
export class MyTradesComponent implements OnInit {
  initiatedTrades: Trade[] = [];
  receivedTrades: Trade[] = [];
  currentUserId: number | null = null;

  constructor(private apiService: ApiService, private snackBar: MatSnackBar, private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getAuthenticatedUser().subscribe({
      next: (user) => {
        if (user && user.id) {
          this.currentUserId = user.id;
          console.log("User ID retrieved:", this.currentUserId); // Debugging log
          this.fetchTrades(); // Fetch trades only after user ID is set
        } else {
          console.error("User ID is missing!");
        }
      },
      error: (error) => {
        console.error("Error fetching authenticated user:", error);
        this.currentUserId = null;
      }
    });
  }


  fetchTrades(): void {
    if (!this.currentUserId) {
      console.error("Cannot fetch trades: User ID is null.");
      return;
    }

    this.apiService.getUserTrades().subscribe({
      next: (trades: Trade[]) => {
        this.initiatedTrades = trades.filter(trade => trade.initiatorId === this.currentUserId);
        this.receivedTrades = trades.filter(trade => trade.receiverId === this.currentUserId);
        console.log("Initiated trades:", this.initiatedTrades);
        console.log("Received trades:", this.receivedTrades);
      },
      error: (err) => {
        console.error("Error fetching trades:", err);
        this.snackBar.open('Failed to load trades.', 'Close', { duration: 3000 });
      }
    });
  }


  acceptTrade(tradeId: number): void {
    this.updateTradeStatus(tradeId, 'accept');
  }

  declineTrade(tradeId: number): void {
    this.updateTradeStatus(tradeId, 'decline');
  }

  cancelTrade(tradeId: number): void {
    this.updateTradeStatus(tradeId, 'cancel');
  }

  completeTrade(tradeId: number): void {
    this.apiService.updateTradeStatus(tradeId, 'complete').subscribe({
      next: () => {
        this.snackBar.open('Trade marked as completed!', 'Close', { duration: 3000 });
        this.fetchTrades();
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Failed to complete the trade.', 'Close', { duration: 3000 });
      }
    });
  }

  updateTradeStatus(tradeId: number, status: string): void {
    this.apiService.updateTradeStatus(tradeId, status).subscribe({
      next: () => {
        this.snackBar.open(`Trade ${status}ed successfully!`, 'Close', { duration: 3000 });
        this.fetchTrades();
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open(`Failed to ${status} the trade.`, 'Close', { duration: 3000 });
      }
    });
  }
}
