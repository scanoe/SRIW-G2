import { ModuleWithProviders }  from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './user/login/login.component';
import { RegisterComponent } from './user/register/register.component';
import { SuggestionComponent } from './user/suggestion/suggestion.component';
import { RankComponent } from './user/rank/rank.component';

export const routes: Routes = [
    { path: 'user/login', component: LoginComponent },
    { path: 'user/register', component: RegisterComponent },
    { path: 'user/rank', component: RankComponent },
    { path: 'user/suggestion', component: SuggestionComponent },
    { path: '', pathMatch: 'full', redirectTo: '/user/login' },
    { path: 'user/login', pathMatch: 'full', redirectTo: '/user/login' },
    { path: '**', pathMatch: 'full', redirectTo: '/user/login' },
];

export const routing: ModuleWithProviders = RouterModule.forRoot(routes);


