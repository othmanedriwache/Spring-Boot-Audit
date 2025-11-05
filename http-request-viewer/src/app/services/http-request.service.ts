import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

interface ApiResponse<T> {
  timeStamp: string;
  statusCode: number;
  status: string;
  developerStatus: string;
  message: string;
  data: T;
}

interface SearchableField {
  key: string;
  type: string;
  regex: string;
}

interface SearchCriteria {
  filterKey: string;
  operation: string;
  value: any;
}

interface SearchRequest {
  dataOption: string;
  searchCriteriaList: SearchCriteria[];
}

interface PageableResponse<T> {
  content: T[];
  pageable: any;
  last: boolean;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  sort: any;
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class HttpRequestService {
  private baseUrl = 'http://localhost:8081/common-services/v1/httpRequest';

  constructor(private http: HttpClient) {}

  getSearchableFields(): Observable<ApiResponse<SearchableField[]>> {
    return this.http.get<ApiResponse<SearchableField[]>>(
      `${this.baseUrl}/getHttpRequestSearchableFields`
    );
  }

  searchHttpRequests(
    searchRequest: SearchRequest,
    pageNum: number = 0,
    pageSize: number = 20
  ): Observable<ApiResponse<PageableResponse<any>>> {
    const params = new HttpParams()
      .set('pageNum', pageNum.toString())
      .set('pageSize', pageSize.toString());

    return this.http.post<ApiResponse<PageableResponse<any>>>(
      `${this.baseUrl}/search`,
      searchRequest,
      { params }
    );
  }

  getHttpRequestAsTree(id: string): Observable<ApiResponse<any>> {
    const params = new HttpParams().set('id', id);

    return this.http.get<ApiResponse<any>>(
      `${this.baseUrl}/getAsTree`,
      { params }
    );
  }
}
