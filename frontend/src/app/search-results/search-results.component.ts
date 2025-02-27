import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../services/api.service';

interface SearchResponse {
  items: any[];
  totalPages: number;
  totalItems: number;
  currentPage: number;
}

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
  itemsPerPage: number = 6; // Default items per page
  totalPages: number = 1;

  pageSizes: number[] = [6, 12, 21]; // Allowed page sizes

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
    const params = {
      ...this.filters,
      page: this.currentPage,
      pageSize: this.itemsPerPage
    };

    this.apiService.searchClothingItems(params).subscribe({
      next: (data: SearchResponse) => {
        this.clothingItems = data.items;
        this.totalPages = data.totalPages;
        this.searched = true;
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
    this.currentPage = 1;
    this.fetchItems();
  }

  resetFilters(): void {
    this.filters = { title: '', brand: '', size: '', minPrice: null, maxPrice: null, condition: '' };
    this.currentPage = 1;
    this.fetchItems();
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.goToPage(this.currentPage + 1);
    }
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.goToPage(this.currentPage - 1);
    }
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
      this.currentPage = page;
      this.fetchItems();
    }
  }

  // Update the number of items per page
  updatePageSize(): void {
    this.currentPage = 1; // Reset to first page when changing page size
    this.fetchItems();
  }

  getPageNumbers(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }
}
