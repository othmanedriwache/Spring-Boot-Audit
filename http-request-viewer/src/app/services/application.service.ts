import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Application, ApplicationWithInstances } from '../models/application.model';
import { ApiResponse } from '../models/http-request.model';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private baseUrl = 'http://localhost:8081/common-services/v1/application';

  constructor(private http: HttpClient) {}

  getAllApplicationsWithInstances(): Observable<ApiResponse<ApplicationWithInstances[]>> {
    return this.http.get<ApiResponse<ApplicationWithInstances[]>>(
        `${this.baseUrl}/all-with-instances`
    );
  }

  createApplication(application: { name: string, version: string }): Observable<ApiResponse<Application>> {
    return this.http.post<ApiResponse<Application>>(
        `${this.baseUrl}/create`,
        application
    );
  }

  cleanApplication(application: ApplicationWithInstances): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
        `${this.baseUrl}/clean`,
        {
          body: application
        }
    );
  }
}
