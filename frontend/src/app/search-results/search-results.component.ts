import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.scss'],
})
export class SearchResultsComponent implements OnInit {
  clothingItems: any[] = [];
  searched = false;

  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 6;

  // Filters object
  filters = {
    title: '',
    brand: '',
    size: '',
    minPrice: null,
    maxPrice: null,
    condition: ''
  };

  constructor(private route: ActivatedRoute, private apiService: ApiService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.filters.title = params['title'] || '';
      this.fetchItems();
    });
  }

  fetchItems(): void {
    this.apiService.searchClothingItems(this.filters).subscribe({
      next: (data) => {
        this.clothingItems = data;
        this.searched = true;
        this.currentPage = 1;
      },
      error: (err) => {
        console.error('Error fetching search results:', err);
      }
    });
  }

  applyFilters(): void {
    if (this.filters.minPrice && this.filters.maxPrice && this.filters.minPrice > this.filters.maxPrice) {
      alert('Min Price cannot be greater than Max Price.');
      return;
    }
    this.fetchItems();
  }

  resetFilters(): void {
    this.filters = { title: '', brand: '', size: '', minPrice: null, maxPrice: null, condition: '' };
    this.fetchItems();
  }

  // Pagination Logic
  get paginatedItems(): any[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.clothingItems.slice(start, start + this.itemsPerPage);
  }

  get totalPages(): number {
    return Math.ceil(this.clothingItems.length / this.itemsPerPage);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  goToPage(page: number): void {
    this.currentPage = page;
  }
}
