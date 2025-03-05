import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';
import { AuthService } from '../services/auth.service';

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
  editMode: { [key: number]: boolean } = {};
  currentUserId: number | null = null;

  constructor(private apiService: ApiService, private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getAuthenticatedUser().subscribe({
      next: (user) => {
        if (user && user.id) {
          this.currentUserId = user.id;
          console.log("User ID retrieved:", this.currentUserId); // Debugging log
          this.fetchClothingItems();
        } else {
          console.error("User ID is missing!");
        }
      },
      error: (error) => {
        console.error("Error fetching authenticated user:", error);
      }
    });
  }

  currentPage: number = 1;
  totalPages: number = 1;
  pageSize: number = 10;


  fetchClothingItems(page: number = 1): void {
    if (!this.currentUserId) {
      console.error("Cannot fetch items: User ID is null.");
      return;
    }

    this.apiService.getPaginatedClothingItems(this.currentUserId, page, this.pageSize).subscribe({
      next: (response) => {
        console.log("Fetched clothing items:", response);
        this.clothingItems = response.items;
        this.totalPages = response.totalPages;
        this.currentPage = response.currentPage;
      },
      error: (error) => {
        console.error("Error fetching clothing items:", error);
      }
    });
  }

// Navigate to the previous page
  previousPage(): void {
    if (this.currentPage > 1) {
      this.fetchClothingItems(this.currentPage - 1);
    }
  }

// Navigate to the next page
  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.fetchClothingItems(this.currentPage + 1);
    }
  }

  onAddItem(): void {
    if (!this.newItem.title || this.newItem.title.length < 3) {
      alert('Title must be at least 3 characters.');
      return;
    }
    if (!this.newItem.size) {
      alert('Size is required.');
      return;
    }
    if (!this.newItem.condition) {
      alert('Condition is required.');
      return;
    }
    if (!this.newItem.price || this.newItem.price < 1 || this.newItem.price > 10000) {
      alert('Price must be between 1 and 10,000.');
      return;
    }

    // Debugging: Log the current user ID before adding the item
    console.log('Adding item for user ID:', this.currentUserId);

    if (this.currentUserId === null) {
      console.error("Cannot add item: User ID is null.");
      return;
    }

    const itemWithUserId = { ...this.newItem, userId: this.currentUserId };

    this.apiService.addClothingItem(itemWithUserId).subscribe(
      () => {
        alert('Clothing item added successfully!');
        this.fetchClothingItems(); // Refresh the list
        this.newItem = { title: '', description: '', size: '', brand: '', condition: '', price: null }; // Reset form
      },
      (error) => {
        console.error('Error adding clothing item:', error);
        alert('Failed to add clothing item. Please try again.');
      }
    );
  }


  toggleEditMode(itemId: number): void {
    this.editMode[itemId] = !this.editMode[itemId];
  }

  updateClothingItem(item: any): void {
    this.apiService.updateClothingItem(item.id, item).subscribe({
      next: (updatedItem) => {
        console.log('Item updated:', updatedItem);
        this.editMode[item.id] = false; // Exit edit mode
        this.fetchClothingItems(); // Refresh list
      },
      error: (err) => {
        console.error('Error updating item:', err);
      }
    });
  }
}
