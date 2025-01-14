import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-my-clothing-items',
  templateUrl: './my-clothing-items.component.html',
  styleUrls: ['./my-clothing-items.component.scss'],
})
export class MyClothingItemsComponent implements OnInit {
  clothingItems: any[] = []; // Stores clothing items

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    const userId = Number(localStorage.getItem('userId')); // Retrieve user ID
    if (!userId || userId <= 0) {
      console.error('Invalid userId in localStorage:', userId);
      alert('User ID is missing or invalid. Please log in again.');
      return;
    }
    this.fetchClothingItems(userId);
  }

  fetchClothingItems(userId: number): void {
    this.apiService.getMyClothingItems(userId).subscribe(
      (data) => {
        this.clothingItems = data;
      },
      (error) => {
        console.error('Error fetching clothing items:', error);
      }
    );
  }
}
