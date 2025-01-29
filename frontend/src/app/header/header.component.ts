import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  searchQuery: string = '';

  constructor(private router: Router) {}

  onSearch(): void {
    this.router.navigate(['/search'], { queryParams: { title: this.searchQuery } });
  }

  onLogout(): void {
    localStorage.removeItem('authToken'); // Clear the token
    this.router.navigate(['/login']); // Redirect to login page
  }
}
