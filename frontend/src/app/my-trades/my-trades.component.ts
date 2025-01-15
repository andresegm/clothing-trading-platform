import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Trade } from '../model/Trade';

@Component({
  selector: 'app-my-trades',
  templateUrl: './my-trades.component.html',
  styleUrls: ['./my-trades.component.scss']
})
export class MyTradesComponent implements OnInit {
  initiatedTrades: Trade[] = [];
  receivedTrades: Trade[] = [];

  constructor(private apiService: ApiService, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.fetchTrades();
  }

  fetchTrades(): void {
    this.apiService.getUserTrades().subscribe({
      next: (trades: Trade[]) => {
        const userId = localStorage.getItem('userId');
        this.initiatedTrades = trades.filter(trade => trade.initiatorId.toString() === userId);
        this.receivedTrades = trades.filter(trade => trade.receiverId.toString() === userId);
      },
      error: (err) => {
        console.error(err);
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
      next: (response) => {
        this.snackBar.open('Trade marked as completed!', 'Close', { duration: 3000 });
        this.fetchTrades(); // Refresh the list to reflect changes
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Failed to complete the trade.', 'Close', { duration: 3000 });
      }
    });
  }


  updateTradeStatus(tradeId: number, status: string): void {
    this.apiService.updateTradeStatus(tradeId, status).subscribe({
      next: (response) => {
        this.snackBar.open(`Trade ${status}ed successfully!`, 'Close', { duration: 3000 });
        this.fetchTrades(); // Refresh the list to reflect the updated status
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open(`Failed to ${status} the trade.`, 'Close', { duration: 3000 });
      }
    });
  }

}
