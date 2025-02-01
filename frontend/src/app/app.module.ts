import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { AuthModule } from './auth/auth.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HeaderComponent } from './header/header.component';
import {FormsModule} from "@angular/forms";
import { SearchResultsComponent } from './search-results/search-results.component';
import { MyClothingItemsComponent } from './my-clothing-items/my-clothing-items.component';
import { MyTradesComponent } from './my-trades/my-trades.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTabsModule} from "@angular/material/tabs";
import { ItemDetailsComponent } from './item-details/item-details.component';
import { HomeComponent } from './home/home.component';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    HeaderComponent,
    SearchResultsComponent,
    MyClothingItemsComponent,
    MyTradesComponent,
    ItemDetailsComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AuthModule,
    FormsModule,
    MatSnackBarModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatTabsModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
