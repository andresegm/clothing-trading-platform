import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-my-clothing-items',
  templateUrl: './my-clothing-items.component.html',
  styleUrls: ['./my-clothing-items.component.scss'],
})
export class MyClothingItemsComponent implements OnInit {
  clothingItems: any[] = []; // Stores clothing items
  newItem = {
    title: '',
    description: '',
    size: '',
    brand: '',
    condition: '',
    price: null,
  };

  constructor(private apiService: ApiService) {
  }

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

  onAddItem(): void {
    const userId = Number(localStorage.getItem('userId'));
    if (!userId || userId <= 0) {
      alert('User ID is missing or invalid. Please log in again.');
      return;
    }

    const itemWithUserId = {...this.newItem, userId};

    this.apiService.addClothingItem(itemWithUserId).subscribe(
      () => {
        alert('Clothing item added successfully!');
        this.fetchClothingItems(userId); // Refresh the list
        this.newItem = {title: '', description: '', size: '', brand: '', condition: '', price: null}; // Reset form
      },
      (error) => {
        console.error('Error adding clothing item:', error);
        alert('Failed to add clothing item. Please try again.');
      }
    );
  }
}
