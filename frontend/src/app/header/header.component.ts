import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';

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
  userId: number = 0; // Ensure userId is stored as a number

  constructor(private router: Router, private apiService: ApiService) {}

  ngOnInit() {
    this.userId = Number(localStorage.getItem('userId'));
    console.log('ngOnInit called. Retrieved userId:', this.userId);

    if (!this.userId || this.userId <= 0) {
      console.error('Invalid userId in localStorage:', this.userId);
      alert('User ID is missing or invalid. Please log in again.');
      return;
    }

    this.fetchNotifications();
  }

  onSearch(): void {
    this.router.navigate(['/search'], { queryParams: { title: this.searchQuery } });
  }

  onLogout(): void {
    localStorage.removeItem('authToken'); // Clear the token
    localStorage.removeItem('userId'); // Clear userId
    this.router.navigate(['/login']); // Redirect to login page
  }

  toggleNotifications(event?: Event) {
    if (event) {
      event.stopPropagation(); // Prevent closing when clicking inside
    }
    this.showDropdown = !this.showDropdown;
  }


  fetchNotifications() {
    this.apiService.getUnreadNotifications(this.userId).subscribe((data) => {
      this.notifications = data;
      this.unreadCount = data.length;
    });
  }


  markAsRead(notification: any) {
    this.apiService.markNotificationsAsRead(this.userId).subscribe(() => {
      this.notifications = this.notifications.filter(n => n.id !== notification.id);
      this.unreadCount = this.notifications.length;
    });
  }


  markAllAsRead(event: Event) {
    event.stopPropagation();
    this.apiService.markNotificationsAsRead(this.userId).subscribe(() => {
      this.notifications = [];
      this.unreadCount = 0;
    });
  }

}
