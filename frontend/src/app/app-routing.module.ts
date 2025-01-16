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
import {HomeComponent} from "./home/home.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'my-clothing-items', component: MyClothingItemsComponent, canActivate: [AuthGuard] },
  { path: 'search', component: SearchResultsComponent, canActivate: [AuthGuard] },
  { path: 'my-trades', component: MyTradesComponent },
  { path: 'item/:id', component: ItemDetailsComponent },
  { path: '**', redirectTo: '/login' }, // Redirect all unknown routes to the login page
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
