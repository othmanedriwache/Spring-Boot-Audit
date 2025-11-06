import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AgGridAngular } from 'ag-grid-angular';
import {
  ColDef,
  GridApi,
  GridReadyEvent,
  ModuleRegistry,
  AllCommunityModule,
  ICellRendererParams
} from 'ag-grid-community';

ModuleRegistry.registerModules([AllCommunityModule]);
import {HttpRequestService} from '../../services/http-request.service';

interface SearchCriteria {
  filterKey: string;
  operation: string;
  value: any;
}

interface SearchRequest {
  dataOption: string;
  searchCriteriaList: SearchCriteria[];
}

interface HttpRequestData {
  id: string;
  url: string;
  method: string;
  host: string;
  status: number;
  inputLine: string;
  inputDate: string;
  exceptionInput: string | null;
  outputLine: string;
  outputDate: string;
  exceptionOutput: string | null;
  duration: number;
  logPath: string;
  applicationInstance: any;
  returnContent: string | null;
}

@Component({
  selector: 'app-http-request-list',
  standalone: true,
  imports: [CommonModule, FormsModule, AgGridAngular],
  templateUrl: './http-request-list.component.html',
  styleUrls: ['./http-request-list.component.css']
})
export class HttpRequestListComponent implements OnInit {
  private gridApi!: GridApi;

  rowData: HttpRequestData[] = [];
  columnDefs: ColDef[] = [
    {
      field: 'id',
      headerName: 'Actions',
      width: 100,
      pinned: 'left',
      sortable: false,
      filter: false,
      cellRenderer: (params: ICellRendererParams) => {
        const button = document.createElement('button');
        button.className = 'btn btn-sm btn-primary view-btn';
        button.innerHTML = '<i class="fa fa-eye"></i>';
        button.title = 'View Details';
        button.style.cssText = `
          padding: 8px 12px;
          font-size: 14px;
          font-weight: 500;
          border-radius: 6px;
          transition: all 0.2s ease;
          box-shadow: 0 2px 4px rgba(0, 123, 255, 0.2);
          display: inline-flex;
          align-items: center;
          justify-content: center;
        `;
        button.addEventListener('mouseover', () => {
          button.style.transform = 'translateY(-2px)';
          button.style.boxShadow = '0 4px 8px rgba(0, 123, 255, 0.4)';
        });
        button.addEventListener('mouseout', () => {
          button.style.transform = 'translateY(0)';
          button.style.boxShadow = '0 2px 4px rgba(0, 123, 255, 0.2)';
        });
        button.addEventListener('click', () => {
          this.viewDetails(params.data.id);
        });
        return button;
      },
      cellStyle: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
      }
    },
    {
      field: 'method',
      headerName: 'Method',
      width: 100,
      filter: true,
      cellStyle: (params) => {
        const method = params.value;
        if (method === 'GET') return { color: '#28a745', fontWeight: 'bold' };
        if (method === 'POST') return { color: '#007bff', fontWeight: 'bold' };
        if (method === 'PUT') return { color: '#ffc107', fontWeight: 'bold' };
        if (method === 'DELETE') return { color: '#dc3545', fontWeight: 'bold' };
        return null;
      }
    },
    { field: 'url', headerName: 'URL', width: 350, filter: true },
    {
      field: 'status',
      headerName: 'Status',
      width: 100,
      filter: 'agNumberColumnFilter',
      cellStyle: (params) => {
        const status = params.value;

        // Base style object
        const baseStyle: any = {
          color: 'white',
          fontWeight: 'bold',
          textAlign: 'center',
          borderRadius: '4px',
          padding: '4px 8px'
        };

        // 2xx - Success (Green)
        if (status >= 200 && status < 300) {
          return {
            ...baseStyle,
            backgroundColor: '#28a745'
          };
        }
        // 3xx - Redirection (Blue)
        else if (status >= 300 && status < 400) {
          return {
            ...baseStyle,
            backgroundColor: '#17a2b8'
          };
        }
        // 4xx - Client Error (Red - Less intense)
        else if (status >= 400 && status < 500) {
          return {
            ...baseStyle,
            backgroundColor: '#e74c3c',
            boxShadow: '0 2px 4px rgba(231, 76, 60, 0.3)'
          };
        }
        // 5xx - Server Error (Darker/Deeper Red - More intense)
        else if (status >= 500) {
          return {
            ...baseStyle,
            backgroundColor: '#c0392b',
            boxShadow: '0 3px 6px rgba(192, 57, 43, 0.5)'
          };
        }
        // 1xx - Informational or others (Gray)
        else {
          return {
            ...baseStyle,
            backgroundColor: '#6c757d'
          };
        }
      }
    },
    {
      field: 'duration',
      headerName: 'Duration (ms)',
      width: 130,
      filter: 'agNumberColumnFilter',
      valueFormatter: (params) => {
        if (params.value) {
          return (params.value / 1000000).toFixed(2) + ' ms';
        }
        return '';
      },
      cellStyle: (params) => {
        const durationMs = params.value / 1000000;
        if (durationMs > 1000) {
          return { color: '#dc3545', fontWeight: 'bold' };
        } else if (durationMs > 500) {
          return { color: '#ffc107', fontWeight: 'bold' };
        } else {
          return { color: '#28a745', fontWeight: 'bold' };
        }
      }
    },
    {
      field: 'applicationInstance.application.name',
      headerName: 'Application',
      width: 180,
      filter: true,
      valueGetter: (params) => params.data?.applicationInstance?.application?.name || 'N/A'
    },
    {
      field: 'applicationInstance.application.version',
      headerName: 'Version',
      width: 120,
      filter: true,
      valueGetter: (params) => params.data?.applicationInstance?.application?.version || 'N/A',
      cellStyle: {
        fontWeight: '600',
        color: '#667eea'
      }
    },
    {
      field: 'applicationInstance.id',
      headerName: 'Instance ID',
      width: 280,
      filter: true,
      valueGetter: (params) => params.data?.applicationInstance?.id || 'N/A',
      cellStyle: {
        fontSize: '12px',
        fontFamily: 'monospace',
        color: '#666'
      }
    },
    {
      field: 'inputDate',
      headerName: 'Request Time',
      width: 180,
      filter: 'agDateColumnFilter',
      valueFormatter: (params) => {
        if (params.value) {
          return new Date(params.value).toLocaleString();
        }
        return '';
      }
    },
    {
      field: 'outputDate',
      headerName: 'Response Time',
      width: 180,
      filter: 'agDateColumnFilter',
      valueFormatter: (params) => {
        if (params.value) {
          return new Date(params.value).toLocaleString();
        }
        return '';
      }
    },
    {
      field: 'host',
      headerName: 'Host',
      width: 150,
      filter: true
    }
  ];

  defaultColDef: ColDef = {
    sortable: true,
    resizable: true,
    filter: true,
  };

  paginationPageSize = 20;
  paginationPageSizeSelector = [10, 20, 50, 100];
  currentPage = 0;
  totalElements = 0;
  totalPages = 0;

  searchableFields: any[] = [];
  selectedField = '';
  selectedOperation = 'EQUAL';
  searchValue: any = '';
  activeFilters: SearchCriteria[] = [];
  validationError = '';
  currentApplicationName = '';
  currentFieldType = '';

  // Define operations by type
  stringOperations = [
    { value: 'BEGINS_WITH', label: 'Begins With' },
    { value: 'ENDS_WITH', label: 'Ends With' },
    { value: 'DOES_NOT_BEGIN_WITH', label: 'Does Not Begin With' },
    { value: 'DOES_NOT_END_WITH', label: 'Does Not End With' },
    { value: 'CONTAINS', label: 'Contains' },
    { value: 'DOES_NOT_CONTAIN', label: 'Does Not Contain' }
  ];

  numericOperations = [
    { value: 'EQUAL', label: 'Equal' },
    { value: 'NOT_EQUAL', label: 'Not Equal' },
    { value: 'GREATER_THAN', label: 'Greater Than' },
    { value: 'LESS_THAN', label: 'Less Than' },
    { value: 'GREATER_THAN_EQUAL', label: 'Greater Than or Equal' },
    { value: 'LESS_THAN_EQUAL', label: 'Less Than or Equal' }
  ];

  tableOperations = [
    { value: 'JOIN_TABLE', label: 'Join Table' }
  ];

  applicationNameOperations = [
    { value: 'APPLICATION_NAME', label: 'Application Name' }
  ];

  applicationVersionOperations = [
    { value: 'APPLICATION_VERSION', label: 'Application Version' }
  ];

  operations: any[] = [];

  readonly MAX_INTEGER_VALUE = 2147483647;
  readonly MIN_INTEGER_VALUE = -2147483648;

  constructor(
    private httpRequestService: HttpRequestService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  addApplicationNameFilter(applicationName: string): void {
    const newFilter: SearchCriteria = {
      filterKey: 'applicationName',
      operation: 'APPLICATION_NAME',
      value: applicationName
    };

    this.activeFilters.push(newFilter);
    // Don't call searchHttpRequests here - let caller decide
  }

  addApplicationVersionFilter(applicationVersion: string): void {
    const newFilter: SearchCriteria = {
      filterKey: 'applicationVersion',
      operation: 'APPLICATION_VERSION',
      value: applicationVersion
    };

    this.activeFilters.push(newFilter);
    // Don't call searchHttpRequests here - let caller decide
  }

  addApplicationInstanceFilter(instanceId: string): void {
    const newFilter: SearchCriteria = {
      filterKey: 'applicationInstance',
      operation: 'JOIN_TABLE',
      value: instanceId
    };

    this.activeFilters.push(newFilter);
    this.searchHttpRequests();
  }

  ngOnInit(): void {
    this.loadSearchableFields();

    this.route.queryParams.subscribe(params => {
      const applicationName = params['applicationName'];
      const applicationVersion = params['applicationVersion'];
      const applicationInstanceId = params['applicationInstanceId'];
      const filterByName = params['filterByName'];

      if (applicationName) {
        this.currentApplicationName = applicationName;
      }

      setTimeout(() => {
        if (applicationInstanceId) {
          this.addApplicationInstanceFilter(applicationInstanceId);
        } else if (filterByName === 'true' && applicationName) {
          // Add application name filter
          this.addApplicationNameFilter(applicationName);

          // Add version filter if provided
          if (applicationVersion) {
            this.addApplicationVersionFilter(applicationVersion);
          }

          // Call search ONCE after adding both filters
          this.searchHttpRequests();
        } else {
          this.searchHttpRequests();
        }
      }, 500);
    });
  }

  onGridReady(params: GridReadyEvent): void {
    this.gridApi = params.api;
  }

  loadSearchableFields(): void {
    this.httpRequestService.getSearchableFields().subscribe({
      next: (response: { data: any[]; }) => {
        this.searchableFields = response.data;
        if (this.searchableFields.length > 0) {
          this.selectedField = this.searchableFields[0].key;
          this.onFieldChange();
        }
      },
      error: (error) => {
        console.error('Error loading searchable fields:', error);
      }
    });
  }

  onFieldChange(): void {
    const field = this.searchableFields.find(f => f.key === this.selectedField);
    if (field) {
      this.currentFieldType = field.type.toLowerCase();

      // Set operations based on field type
      if (field.key === 'applicationInstance') {
        this.operations = this.tableOperations;
        this.selectedOperation = 'JOIN_TABLE';
      } else if (field.key === 'applicationName') {
        this.operations = this.applicationNameOperations;
        this.selectedOperation = 'APPLICATION_NAME';
      } else if (field.key === 'applicationVersion') {
        this.operations = this.applicationVersionOperations;
        this.selectedOperation = 'APPLICATION_VERSION';
      } else if (field.type === 'int' || field.type === 'Integer' || field.type === 'LocalDateTime') {
        this.operations = this.numericOperations;
        this.selectedOperation = 'EQUAL';
      } else {
        // String type
        this.operations = this.stringOperations;
        this.selectedOperation = 'BEGINS_WITH';
      }
    }
    this.searchValue = '';
    this.validationError = '';
  }

  isDateTimeField(): boolean {
    return this.currentFieldType === 'localdatetime';
  }

  isNumericField(): boolean {
    return this.currentFieldType === 'int' || this.currentFieldType === 'integer';
  }

  isStringField(): boolean {
    return this.currentFieldType === 'string' ||
      (!this.isDateTimeField() && !this.isNumericField() &&
        this.selectedField !== 'applicationInstance' &&
        this.selectedField !== 'applicationName' &&
        this.selectedField !== 'applicationVersion');
  }

  validateSearchValue(): boolean {
    this.validationError = '';

    const field = this.searchableFields.find(f => f.key === this.selectedField);
    if (!field) {
      this.validationError = 'Please select a valid field';
      return false;
    }

    // Check if value is empty (handle both string and number types)
    if (this.searchValue === null || this.searchValue === undefined || this.searchValue === '') {
      this.validationError = 'Search value is required';
      return false;
    }

    // For string values, check if it's just whitespace
    if (typeof this.searchValue === 'string' && this.searchValue.trim() === '') {
      this.validationError = 'Search value is required';
      return false;
    }

    // Validate numeric fields
    if (field.type === 'int' || field.type === 'Integer') {
      const numValue = Number(this.searchValue);
      if (isNaN(numValue)) {
        this.validationError = 'Please enter a valid integer';
        return false;
      }
      if (numValue > this.MAX_INTEGER_VALUE || numValue < this.MIN_INTEGER_VALUE) {
        this.validationError = `Integer value must be between ${this.MIN_INTEGER_VALUE} and ${this.MAX_INTEGER_VALUE}`;
        return false;
      }
    }

    // Validate datetime fields
    if (field.type === 'LocalDateTime') {
      // For datetime-local input, the value will be in format: 2025-11-01T00:39
      // We don't need to validate the pattern here as the conversion happens in addFilter
      if (typeof this.searchValue === 'string' && this.searchValue.length === 0) {
        this.validationError = 'Please select a date and time';
        return false;
      }
    }

    return true;
  }

  formatDateTimeValue(dateStr: string): string {
    // Convert from datetime-local format to required format
    // Input: 2025-11-01T00:39
    // Output: 2025-11-01 00:39:00.000
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    const milliseconds = String(date.getMilliseconds()).padStart(3, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}.${milliseconds}`;
  }

  addFilter(): void {
    if (!this.validateSearchValue()) {
      return;
    }

    let valueToSend = this.searchValue;

    // Format datetime if needed
    if (this.isDateTimeField()) {
      valueToSend = this.formatDateTimeValue(String(this.searchValue));
    }
    // Convert milliseconds to nanoseconds for duration field
    else if (this.selectedField === 'duration' && this.isNumericField()) {
      // Convert ms to nanoseconds (multiply by 1,000,000)
      const msValue = Number(this.searchValue);
      valueToSend = String(msValue * 1000000);
    }
    // Convert to string for consistency
    else if (typeof valueToSend !== 'string') {
      valueToSend = String(valueToSend);
    }

    const newFilter: SearchCriteria = {
      filterKey: this.selectedField,
      operation: this.selectedOperation,
      value: valueToSend
    };

    this.activeFilters.push(newFilter);
    this.searchValue = '';
    this.validationError = '';
    this.searchHttpRequests();
  }

  removeFilter(index: number): void {
    this.activeFilters.splice(index, 1);
    this.searchHttpRequests();
  }

  clearAllFilters(): void {
    this.activeFilters = [];
    this.currentApplicationName = '';
    this.searchHttpRequests();
  }

  searchHttpRequests(): void {
    const searchRequest: SearchRequest = {
      dataOption: 'ALL',
      searchCriteriaList: this.activeFilters
    };

    this.httpRequestService.searchHttpRequests(searchRequest).subscribe({
      next: (response) => {
        this.rowData = response.data.content;
        this.totalElements = response.data.totalElements;
        this.totalPages = response.data.totalPages;
        this.currentPage = response.data.number;
      },
      error: (error) => {
        console.error('Error searching HTTP requests:', error);
      }
    });
  }

  viewDetails(id: string): void {
    this.router.navigate(['/http-request-detail', id]);
  }

  exportToCsv(): void {
    if (this.gridApi) {
      this.gridApi.exportDataAsCsv();
    }
  }

  backToApplications(): void {
    this.router.navigate(['/']);
  }

  getFieldLabel(key: string): string {
    const field = this.searchableFields.find(f => f.key === key);
    return field ? field.key : key;
  }

  getOperationLabel(operation: string): string {
    const allOps = [
      ...this.stringOperations,
      ...this.numericOperations,
      ...this.tableOperations,
      ...this.applicationNameOperations,
      ...this.applicationVersionOperations
    ];
    const op = allOps.find(o => o.value === operation);
    return op ? op.label : operation;
  }

  getFilterDisplayValue(filter: SearchCriteria): string {
    // Convert nanoseconds back to milliseconds for duration display
    if (filter.filterKey === 'duration') {
      const nanoseconds = Number(filter.value);
      const milliseconds = (nanoseconds / 1000000).toFixed(2);
      return `${milliseconds} ms`;
    }
    return filter.value;
  }
}
