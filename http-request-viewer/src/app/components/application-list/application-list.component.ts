import { Component, OnInit, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
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

interface Application {
  id: string;
  name: string;
  version: string;
  creationDate: string;
  applicationInstances: ApplicationInstance[];
}

interface ApplicationInstance {
  id: string;
  creationDate: string;
}

interface CreateApplicationRequest {
  name: string;
  version: string;
}

interface ApiResponse<T> {
  statusCode: number;
  message: string;
  data: T;
}

@Component({
  selector: 'app-application-list',
  standalone: true,
  imports: [CommonModule, FormsModule, AgGridAngular],
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.css']
})
export class ApplicationListComponent implements OnInit {
  private gridApi!: GridApi;
  private baseUrl = 'http://localhost:8081/common-services/v1/application';

  rowData: Application[] = [];
  columnDefs: ColDef[] = [
    {
      field: 'name',
      headerName: 'Application Name',
      width: 250,
      filter: true,
      cellStyle: {
        fontWeight: '600',
        color: '#667eea'
      }
    },
    {
      field: 'version',
      headerName: 'Version',
      width: 150,
      filter: true,
      cellStyle: {
        fontWeight: '500'
      }
    },
    {
      field: 'creationDate',
      headerName: 'Created Date',
      width: 200,
      filter: 'agDateColumnFilter',
      valueFormatter: (params) => {
        if (params.value) {
          return new Date(params.value).toLocaleString();
        }
        return '';
      }
    },
    {
      field: 'applicationInstances',
      headerName: 'Instances',
      width: 120,
      filter: false,
      sortable: false,
      valueGetter: (params) => params.data?.applicationInstances?.length || 0,
      cellStyle: (params) => {
        const count = params.value;
        if (count === 0) return { color: '#6c757d', fontWeight: 'bold', textAlign: 'center' };
        if (count > 0 && count <= 3) return { color: '#28a745', fontWeight: 'bold', textAlign: 'center' };
        if (count > 3) return { color: '#007bff', fontWeight: 'bold', textAlign: 'center' };
        return null;
      }
    },
    {
      field: 'id',
      headerName: 'Actions',
      width: 350,
      pinned: 'right',
      sortable: false,
      filter: false,
      cellRenderer: (params: ICellRendererParams) => {
        const container = document.createElement('div');
        container.style.cssText = 'display: flex; gap: 8px; align-items: center; justify-content: center;';

        // View HTTP Requests Button
        const httpBtn = document.createElement('button');
        httpBtn.className = 'btn btn-sm btn-success';
        httpBtn.innerHTML = '<i class="fa fa-search"></i> HTTP Requests';
        httpBtn.title = 'View HTTP Requests';
        httpBtn.style.cssText = `
          padding: 6px 12px;
          font-size: 13px;
          font-weight: 600;
          border-radius: 6px;
          transition: all 0.2s ease;
          background: #28a745;
          color: white;
          border: none;
          cursor: pointer;
        `;
        httpBtn.addEventListener('mouseover', () => {
          httpBtn.style.transform = 'translateY(-2px)';
          httpBtn.style.boxShadow = '0 4px 8px rgba(40, 167, 69, 0.4)';
        });
        httpBtn.addEventListener('mouseout', () => {
          httpBtn.style.transform = 'translateY(0)';
          httpBtn.style.boxShadow = 'none';
        });
        httpBtn.addEventListener('click', (e) => {
          e.stopPropagation();
          this.ngZone.run(() => {
            this.viewApplicationHttpRequests(params.data);
          });
        });

        // View Instances Button
        const viewBtn = document.createElement('button');
        viewBtn.className = 'btn btn-sm btn-info';
        viewBtn.innerHTML = '<i class="fa fa-list"></i> Instances';
        viewBtn.title = 'View Instances';
        viewBtn.style.cssText = `
          padding: 6px 12px;
          font-size: 13px;
          font-weight: 600;
          border-radius: 6px;
          transition: all 0.2s ease;
          background: #17a2b8;
          color: white;
          border: none;
          cursor: pointer;
        `;
        const instanceCount = params.data?.applicationInstances?.length || 0;
        if (instanceCount === 0) {
          viewBtn.disabled = true;
          viewBtn.style.opacity = '0.5';
          viewBtn.style.cursor = 'not-allowed';
        } else {
          viewBtn.addEventListener('mouseover', () => {
            viewBtn.style.transform = 'translateY(-2px)';
            viewBtn.style.boxShadow = '0 4px 8px rgba(23, 162, 184, 0.4)';
          });
          viewBtn.addEventListener('mouseout', () => {
            viewBtn.style.transform = 'translateY(0)';
            viewBtn.style.boxShadow = 'none';
          });
          viewBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            this.ngZone.run(() => {
              this.showInstances(params.data);
            });
          });
        }

        // Delete Button
        const deleteBtn = document.createElement('button');
        deleteBtn.className = 'btn btn-sm btn-danger';
        deleteBtn.innerHTML = '<i class="fa fa-trash"></i> Delete';
        deleteBtn.title = 'Delete Application';
        deleteBtn.style.cssText = `
          padding: 6px 12px;
          font-size: 13px;
          font-weight: 600;
          border-radius: 6px;
          transition: all 0.2s ease;
          background: #dc3545;
          color: white;
          border: none;
          cursor: pointer;
        `;
        deleteBtn.addEventListener('mouseover', () => {
          deleteBtn.style.transform = 'translateY(-2px)';
          deleteBtn.style.boxShadow = '0 4px 8px rgba(220, 53, 69, 0.4)';
        });
        deleteBtn.addEventListener('mouseout', () => {
          deleteBtn.style.transform = 'translateY(0)';
          deleteBtn.style.boxShadow = 'none';
        });
        deleteBtn.addEventListener('click', (e) => {
          e.stopPropagation();
          this.ngZone.run(() => {
            this.deleteApplication(params.data);
          });
        });

        container.appendChild(httpBtn);
        container.appendChild(viewBtn);
        container.appendChild(deleteBtn);
        return container;
      },
      cellStyle: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
      }
    }
  ];

  defaultColDef: ColDef = {
    sortable: true,
    resizable: true,
    filter: true,
  };

  // Create Application Modal
  showCreateModal = false;
  newApplication: CreateApplicationRequest = {
    name: '',
    version: ''
  };
  createErrors = {
    name: '',
    version: ''
  };

  // Instances Modal
  showInstancesModal = false;
  selectedApplication: Application | null = null;
  selectedInstances: ApplicationInstance[] = [];

  // Error Modal
  showErrorModal = false;
  errorMessage = '';
  errorTitle = '';

  isLoading = false;

  constructor(
    private router: Router,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    this.loadApplications();
  }

  getTotalInstances(): number {
    return this.rowData.reduce((sum, app) => sum + (app.applicationInstances?.length || 0), 0);
  }

  onGridReady(params: GridReadyEvent): void {
    this.gridApi = params.api;
  }

  loadApplications(): void {
    this.isLoading = true;

    fetch(`${this.baseUrl}/all-with-instances`)
      .then(response => response.json())
      .then((data: ApiResponse<Application[]>) => {
        if (data.statusCode === 200) {
          this.rowData = data.data;
        } else {
          console.error('Failed to load applications:', data.message);
        }
        this.isLoading = false;
      })
      .catch((error: Error) => {
        console.error('Error loading applications:', error);
        this.isLoading = false;
      });
  }

  openCreateModal(): void {
    this.showCreateModal = true;
    this.newApplication = { name: '', version: '' };
    this.createErrors = { name: '', version: '' };
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
    this.newApplication = { name: '', version: '' };
    this.createErrors = { name: '', version: '' };
  }

  validateCreateForm(): boolean {
    let isValid = true;
    this.createErrors = { name: '', version: '' };

    if (!this.newApplication.name || this.newApplication.name.trim() === '') {
      this.createErrors.name = 'Application name is required';
      isValid = false;
    }

    if (!this.newApplication.version || this.newApplication.version.trim() === '') {
      this.createErrors.version = 'Version is required';
      isValid = false;
    }

    return isValid;
  }

  createApplication(): void {
    if (!this.validateCreateForm()) {
      return;
    }

    this.isLoading = true;

    fetch(`${this.baseUrl}/create`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(this.newApplication)
    })
      .then(response => response.json())
      .then((data: ApiResponse<Application>) => {
        if (data.statusCode === 200) {
          this.closeCreateModal();
          this.refreshApplications();
        } else {
          console.error('Failed to create application:', data.message);
        }
        this.isLoading = false;
      })
      .catch((error: Error) => {
        console.error('Error creating application:', error);
        this.isLoading = false;
      });
  }

  showInstances(application: Application): void {
    this.selectedApplication = application;
    this.selectedInstances = application.applicationInstances || [];
    this.showInstancesModal = true;
  }

  closeInstancesModal(): void {
    this.showInstancesModal = false;
    this.selectedApplication = null;
    this.selectedInstances = [];
  }

  closeErrorModal(): void {
    this.showErrorModal = false;
    this.errorMessage = '';
    this.errorTitle = '';
  }

  deleteApplication(application: Application): void {
    const instanceCount = application.applicationInstances?.length || 0;

    if (instanceCount > 0) {
      this.errorTitle = 'Cannot Delete Application';
      this.errorMessage = `The application "${application.name}" (v${application.version}) cannot be deleted because it has ${instanceCount} active instance${instanceCount > 1 ? 's' : ''}.\n\nPlease delete all instances first before deleting the application.`;
      this.showErrorModal = true;
      return;
    }

    if (!confirm(`Are you sure you want to delete the application "${application.name}" (v${application.version})?\n\nThis action cannot be undone.`)) {
      return;
    }

    this.isLoading = true;

    fetch(`${this.baseUrl}/cleanByNameAndVersion/${encodeURIComponent(application.name)}/${encodeURIComponent(application.version)}`, {
      method: 'DELETE'
    })
      .then(response => response.json())
      .then((data: ApiResponse<void>) => {
        if (data.statusCode === 200) {
          this.refreshApplications();
        } else {
          console.error('Failed to delete application:', data.message);
        }
        this.isLoading = false;
      })
      .catch((error: Error) => {
        console.error('Error deleting application:', error);
        this.isLoading = false;
      });
  }

  deleteInstance(instanceId: string): void {
    if (!confirm(`Are you sure you want to delete this instance?\n\nInstance ID: ${instanceId}\n\nThis action cannot be undone.`)) {
      return;
    }

    this.isLoading = true;

    fetch(`${this.baseUrl}/instance/clean/${instanceId}`, {
      method: 'DELETE'
    })
      .then(response => response.json())
      .then((data: ApiResponse<void>) => {
        if (data.statusCode === 200) {

          // Update the selected instances list
          this.selectedInstances = this.selectedInstances.filter(inst => inst.id !== instanceId);

          // Refresh the applications list to update instance counts
          this.refreshApplications();

          // Close the modal if no more instances
          if (this.selectedInstances.length === 0) {
            this.closeInstancesModal();
          }
        } else {
          console.error('Failed to delete instance:', data.message);
        }
        this.isLoading = false;
      })
      .catch((error: Error) => {
        console.error('Error deleting instance:', error);
        this.isLoading = false;
      });
  }

  refreshApplications(): void {
    this.loadApplications();
  }

  viewApplicationHttpRequests(application: Application): void {
    // Navigate with the application name to filter by APPLICATION_NAME operation
    this.router.navigate(['/http-requests'], {
      queryParams: {
        applicationName: application.name,
        filterByName: 'true'
      }
    });
  }

  viewInstanceHttpRequests(instanceId: string): void {
    this.router.navigate(['/http-requests'], {
      queryParams: {
        applicationInstanceId: instanceId,
        applicationName: this.selectedApplication?.name
      }
    });
  }
}
