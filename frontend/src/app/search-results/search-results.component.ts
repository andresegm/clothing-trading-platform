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

  // Filters object
  filters = {
    title: '',
    brand: '',
    size: '',
    minPrice: null,
    maxPrice: null,
  };

  constructor(private route: ActivatedRoute, private apiService: ApiService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      // Pre-fill title filter from query params
      this.filters.title = params['title'] || '';
      this.fetchItems();
    });
  }

  fetchItems(): void {
    this.apiService.searchClothingItems(this.filters).subscribe((data) => {
      this.clothingItems = data;
      this.searched = true;
    });
  }

  applyFilters(): void {
    this.fetchItems();
  }
}
