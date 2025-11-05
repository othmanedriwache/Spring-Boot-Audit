import { Routes } from '@angular/router';
import { ApplicationListComponent } from './components/application-list/application-list.component';
import { HttpRequestListComponent } from './components/http-request-list/http-request-list.component';
import { HttpRequestDetailComponent } from './components/http-request-detail/http-request-detail.component';

export const routes: Routes = [
  {
    path: '',
    component: ApplicationListComponent,
    title: 'Application Management'
  },
  {
    path: 'http-requests',
    component: HttpRequestListComponent,
    title: 'HTTP Requests'
  },
  {
    path: 'http-request-detail/:id',
    component: HttpRequestDetailComponent,
    title: 'Request Details'
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full'
  }
];
