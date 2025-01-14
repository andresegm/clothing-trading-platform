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

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    HeaderComponent,
    SearchResultsComponent,
    MyClothingItemsComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        AuthModule,
        FormsModule
    ],
  bootstrap: [AppComponent]
})
export class AppModule {}
