import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  searchQuery: string = '';
  notifications: any[] = [];
  unreadCount: number = 0;
  showDropdown: boolean = false;
  isAuthenticated: boolean = false;
  isMenuOpen: boolean = false;
  isScrolled: boolean = false;

  constructor(private router: Router, private apiService: ApiService, private authService: AuthService) {}

  ngOnInit() {
    this.authService.isLoggedIn().subscribe({
      next: (isAuthenticated) => {
        this.isAuthenticated = isAuthenticated;
        if (this.isAuthenticated) {
          this.fetchNotifications();
        }
      },
      error: () => {
        this.isAuthenticated = false;
      }
    });
  }

  onSearch(): void {
    this.router.navigate(['/search'], { queryParams: { title: this.searchQuery } });
  }

  onLogout(): void {
    this.authService.logout();
  }

  toggleNotifications(event?: Event) {
    if (event) {
      event.stopPropagation();
    }
    this.showDropdown = !this.showDropdown;
  }

  fetchNotifications() {
    if (!this.isAuthenticated) {
      console.warn("Skipping notification fetch: User not logged in.");
      return;
    }

    this.apiService.getUnreadNotifications().subscribe({
      next: (data) => {
        this.notifications = data;
        this.unreadCount = data.length;
      },
      error: (err) => {
        console.error("Failed to fetch notifications:", err);
      }
    });
  }

  markAsRead(notification: any) {
    if (!this.isAuthenticated) return;

    this.apiService.markNotificationsAsRead().subscribe(() => {
      this.notifications = this.notifications.filter(n => n.id !== notification.id);
      this.unreadCount = this.notifications.length;
    });
  }

  markAllAsRead(event: Event) {
    event.stopPropagation();

    if (!this.isAuthenticated) return;

    this.apiService.markNotificationsAsRead().subscribe(() => {
      this.notifications = [];
      this.unreadCount = 0;
    });
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.isScrolled = window.scrollY > 50;
  }

  onLogin(): void {
    this.router.navigate(['/login']);
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(): void {
    this.isMenuOpen = false;
  }

  @HostListener('document:click', ['$event'])
  closeNotificationsOnClickOutside(event: Event): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.notification-container')) {
      this.showDropdown = false;
    }
  }
}
