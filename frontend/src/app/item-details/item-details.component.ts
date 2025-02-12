import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../services/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.scss']
})
export class ItemDetailsComponent implements OnInit {
  item: any; // Store item details
  currentUserId: number | null = null; // User ID retrieved via API
  isTradeRequested = false; // Track if trade has been requested

  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService,
    private snackBar: MatSnackBar,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const itemId = +this.route.snapshot.paramMap.get('id')!;
    this.fetchItemDetails(itemId);
    this.checkTradeRequest(itemId);
    this.getCurrentUserId();
  }

  getCurrentUserId(): void {
    this.authService.getAuthenticatedUser().subscribe({
      next: (user) => {
        if (user && user.id) {
          this.currentUserId = user.id;
        } else {
          console.error("User ID is missing in the response!");
          this.currentUserId = null;
        }
      },
      error: (error) => {
        console.error("Error fetching authenticated user:", error);
        this.currentUserId = null;
      }
    });
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

  onTradeRequest(itemId: number): void {
    if (this.isTradeRequested) {
      this.snackBar.open('Trade already requested.', 'Close', { duration: 3000 });
      return;
    }

    this.apiService.initiateTrade(itemId, 'Pending').subscribe({
      next: () => {
        this.snackBar.open('Trade request sent successfully!', 'Close', { duration: 3000 });
        this.isTradeRequested = true; // Update trade request state
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Failed to send trade request.', 'Close', { duration: 3000 });
      }
    });
  }
}
