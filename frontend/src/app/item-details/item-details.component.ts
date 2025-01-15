import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../services/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.scss']
})
export class ItemDetailsComponent implements OnInit {
  item: any; // Store item details
  currentUserId: number = +localStorage.getItem('userId')!; // Get logged-in user ID


  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService,
    private snackBar: MatSnackBar
  ) {}

  isTradeRequested = false;
  ngOnInit(): void {
    const itemId = +this.route.snapshot.paramMap.get('id')!;
    this.fetchItemDetails(itemId);
    this.checkTradeRequest(itemId);
  }

  checkTradeRequest(itemId: number): void {
    this.apiService.checkExistingTrade(itemId).subscribe({
      next: (exists) => {
        this.isTradeRequested = exists;
        console.log('Trade already requested:', exists);
      },
      error: (err) => {
        console.error('Error checking trade request:', err);
      }
    });
  }

  fetchItemDetails(itemId: number): void {
    this.apiService.getItemDetails(itemId).subscribe({
      next: (response) => {
        this.item = response;
      },
      error: (err) => {
        console.error('Error fetching item details:', err);
        this.snackBar.open('Failed to load item details.', 'Close', { duration: 3000 });
      }
    });
  }

  isRequestingTrade = false;

  onTradeRequest(itemId: number): void {
    this.apiService.initiateTrade(itemId, 'Pending').subscribe({
      next: (response) => {
        this.snackBar.open('Trade request sent successfully!', 'Close', { duration: 3000 });
        this.isTradeRequested = true; // Update the button state immediately
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Failed to send trade request.', 'Close', { duration: 3000 });
      }
    });
  }


}
