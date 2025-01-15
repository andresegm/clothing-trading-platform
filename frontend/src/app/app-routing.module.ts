import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AuthGuard } from './auth.guard';
import {SearchResultsComponent} from "./search-results/search-results.component";
import {MyClothingItemsComponent} from "./my-clothing-items/my-clothing-items.component";
import {MyTradesComponent} from "./my-trades/my-trades.component";
import {ItemDetailsComponent} from "./item-details/item-details.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'my-clothing-items', component: MyClothingItemsComponent, canActivate: [AuthGuard] },
  { path: 'search', component: SearchResultsComponent, canActivate: [AuthGuard] },
  { path: 'my-trades', component: MyTradesComponent },
  { path: 'item/:id', component: ItemDetailsComponent },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' }, // Default route redirects to the dashboard for logged-in users
  { path: '**', redirectTo: '/dashboard' }, // Wildcard redirects to dashboard
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
