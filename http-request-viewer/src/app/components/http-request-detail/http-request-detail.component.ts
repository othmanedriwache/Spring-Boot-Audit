import { Component, OnInit, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpRequestService } from '../../services/http-request.service';
import * as d3 from 'd3';

interface Argument {
  id: string;
  content: string;
  type: string;
}

interface BusinessRequest {
  id: string;
  type: string;
  content: string;
  exception: string | null;
  inputLine: string;
  inputDate: string;
  parentTreeStatus: string;
}

interface FunctionRequest {
  id: string;
  type: string;
  path: string;
  packagePath: string;
  returnType: string | null;
  functionName: string;
  returnContent: string | null;
  inputLine: string;
  inputDate: string;
  exceptionInput: string | null;
  outputLine: string;
  outputDate: string;
  exceptionOutput: string | null;
  duration: number;
  parentTreeStatus: string;
  exception: FunctionRequest | null;
  arguments: Argument[];
  businessRequests: BusinessRequest[];
  parent: any;
  children: FunctionRequest[] | null;
}

interface HttpRequestDetail {
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
  parameters: Array<{ id: string; parameter: string; content: string }>;
  headers: Array<{ id: string; header: string; content: string }>;
  functionRequests: FunctionRequest[];
  businessRequests: BusinessRequest[];
  applicationInstance: any;
  returnContent: string | null;
}

interface GraphNode {
  id: string;
  name: string;
  type: string;
  duration: number;
  level: number;
  func: FunctionRequest;
}

interface GraphLink {
  source: string;
  target: string;
}

@Component({
  selector: 'app-http-request-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './http-request-detail.component.html',
  styleUrls: ['./http-request-detail.component.css']
})
export class HttpRequestDetailComponent implements OnInit, AfterViewInit {
  @ViewChild('graphContainer', { static: false }) graphContainer!: ElementRef;

  requestDetail: HttpRequestDetail | null = null;
  loading = true;
  error: string | null = null;
  requestId: string | null = null;
  showGraph = false;

  private graphNodes: GraphNode[] = [];
  private graphLinks: GraphLink[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private httpRequestService: HttpRequestService
  ) {}

  ngOnInit(): void {
    this.requestId = this.route.snapshot.paramMap.get('id');
    if (this.requestId) {
      this.loadRequestDetail(this.requestId);
    } else {
      this.error = 'No request ID provided';
      this.loading = false;
    }
  }

  ngAfterViewInit(): void {
    if (this.requestDetail && this.showGraph) {
      setTimeout(() => this.renderGraph(), 100);
    }
  }

  loadRequestDetail(id: string): void {
    this.loading = true;
    this.error = null;

    this.httpRequestService.getHttpRequestAsTree(id).subscribe({
      next: (response) => {
        this.requestDetail = response.data;
        this.buildGraphData();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading request detail:', error);
        this.error = 'Failed to load request details';
        this.loading = false;
      }
    });
  }

  toggleGraph(): void {
    this.showGraph = !this.showGraph;
    if (this.showGraph) {
      setTimeout(() => this.renderGraph(), 100);
    }
  }

  buildGraphData(): void {
    this.graphNodes = [];
    this.graphLinks = [];

    if (!this.requestDetail?.functionRequests) return;

    let nodeId = 0;
    const processFunction = (func: FunctionRequest, level: number, parentId?: string) => {
      const currentNodeId = `node-${nodeId++}`;

      this.graphNodes.push({
        id: currentNodeId,
        name: func.functionName,
        type: func.type,
        duration: func.duration,
        level: level,
        func: func
      });

      if (parentId) {
        this.graphLinks.push({
          source: parentId,
          target: currentNodeId
        });
      }

      if (func.children && func.children.length > 0) {
        func.children.forEach(child => {
          processFunction(child, level + 1, currentNodeId);
        });
      }
    };

    this.requestDetail.functionRequests.forEach(func => {
      processFunction(func, 0);
    });
  }

  renderGraph(): void {
    if (!this.graphContainer || this.graphNodes.length === 0) return;

    const container = this.graphContainer.nativeElement;
    d3.select(container).selectAll('*').remove();

    const width = container.offsetWidth;
    const height = 600;
    const radius = Math.min(width, height) / 2 - 100;

    const svg = d3.select(container)
      .append('svg')
      .attr('width', width)
      .attr('height', height);

    const g = svg.append('g')
      .attr('transform', `translate(${width / 2},${height / 2})`);

    // Create radial layout
    const maxLevel = Math.max(...this.graphNodes.map(n => n.level));
    const angleStep = (2 * Math.PI) / Math.max(this.graphNodes.length, 1);

    // Position nodes in concentric circles
    const nodePositions = new Map<string, { x: number; y: number }>();

    this.graphNodes.forEach((node, index) => {
      const levelRadius = (node.level + 1) * (radius / (maxLevel + 1));
      const angle = index * angleStep;

      nodePositions.set(node.id, {
        x: levelRadius * Math.cos(angle - Math.PI / 2),
        y: levelRadius * Math.sin(angle - Math.PI / 2)
      });
    });

    // Draw links
    g.append('g')
      .selectAll('line')
      .data(this.graphLinks)
      .enter()
      .append('line')
      .attr('x1', d => nodePositions.get(d.source)?.x || 0)
      .attr('y1', d => nodePositions.get(d.source)?.y || 0)
      .attr('x2', d => nodePositions.get(d.target)?.x || 0)
      .attr('y2', d => nodePositions.get(d.target)?.y || 0)
      .attr('stroke', '#999')
      .attr('stroke-width', 2)
      .attr('stroke-opacity', 0.6)
      .attr('marker-end', 'url(#arrowhead)');

    // Add arrow marker
    svg.append('defs')
      .append('marker')
      .attr('id', 'arrowhead')
      .attr('viewBox', '-0 -5 10 10')
      .attr('refX', 20)
      .attr('refY', 0)
      .attr('orient', 'auto')
      .attr('markerWidth', 8)
      .attr('markerHeight', 8)
      .append('svg:path')
      .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
      .attr('fill', '#999');

    // Draw nodes
    const nodes = g.append('g')
      .selectAll('g')
      .data(this.graphNodes)
      .enter()
      .append('g')
      .attr('transform', d => {
        const pos = nodePositions.get(d.id);
        return `translate(${pos?.x || 0},${pos?.y || 0})`;
      })
      .style('cursor', 'pointer');

    nodes.append('circle')
      .attr('r', 20)
      .attr('fill', d => this.getTypeColor(d.type))
      .attr('stroke', d => d.type === 'EXCEPTION' ? '#dc3545' : '#fff')
      .attr('stroke-width', d => d.type === 'EXCEPTION' ? 3 : 2)
      .on('mouseover', function(event, d) {
        d3.select(this)
          .transition()
          .duration(200)
          .attr('r', 25);

        // Show tooltip
        tooltip.transition()
          .duration(200)
          .style('opacity', 0.9);
        tooltip.html(`
          <strong>${d.name}</strong><br/>
          Type: ${d.type}<br/>
          Duration: ${(d.duration / 1000000).toFixed(2)} ms
        `)
          .style('left', (event.pageX + 10) + 'px')
          .style('top', (event.pageY - 28) + 'px');
      })
      .on('mouseout', function() {
        d3.select(this)
          .transition()
          .duration(200)
          .attr('r', 20);

        tooltip.transition()
          .duration(500)
          .style('opacity', 0);
      });

    // Add exception indicator
    nodes.filter(d => d.type === 'EXCEPTION')
      .append('text')
      .attr('text-anchor', 'middle')
      .attr('dy', 5)
      .attr('font-size', '16px')
      .attr('fill', '#fff')
      .text('!');

    // Add labels
    nodes.append('text')
      .attr('text-anchor', 'middle')
      .attr('dy', 35)
      .attr('font-size', '11px')
      .attr('fill', '#333')
      .text(d => d.name.length > 15 ? d.name.substring(0, 12) + '...' : d.name);

    // Add level circles
    for (let i = 0; i <= maxLevel; i++) {
      const levelRadius = (i + 1) * (radius / (maxLevel + 1));
      g.append('circle')
        .attr('r', levelRadius)
        .attr('fill', 'none')
        .attr('stroke', '#e0e0e0')
        .attr('stroke-width', 1)
        .attr('stroke-dasharray', '5,5');
    }

    // Tooltip
    const tooltip = d3.select('body')
      .append('div')
      .attr('class', 'graph-tooltip')
      .style('opacity', 0)
      .style('position', 'absolute')
      .style('background-color', 'rgba(0, 0, 0, 0.8)')
      .style('color', '#fff')
      .style('padding', '10px')
      .style('border-radius', '5px')
      .style('font-size', '12px')
      .style('pointer-events', 'none')
      .style('z-index', '1000');
  }

  goBack(): void {
    this.router.navigate(['/http-requests']);
  }

  formatDuration(nanoseconds: number): string {
    return (nanoseconds / 1000000).toFixed(2) + ' ms';
  }

  getStatusClass(status: number): string {
    // 2xx - Success (Green)
    if (status >= 200 && status < 300) return 'success';
    // 3xx - Redirection (Blue)
    if (status >= 300 && status < 400) return 'info';
    // 4xx - Client Error (Red)
    if (status >= 400 && status < 500) return 'danger';
    // 5xx - Server Error (Red - will appear same as 4xx in detail view)
    if (status >= 500) return 'danger';
    // Others (Gray)
    return 'secondary';
  }

  getTypeColor(type: string): string {
    switch (type) {
      case 'CONTROLLER':
        return '#007bff';
      case 'SERVICE':
        return '#28a745';
      case 'REPOSITORY':
        return '#17a2b8';
      case 'EXCEPTION':
        return '#dc3545';
      default:
        return '#6c757d';
    }
  }

  getBusinessTypeClass(type: string): string {
    switch (type) {
      case 'INFO':
        return 'info';
      case 'WARNING':
        return 'warning';
      case 'ERROR':
        return 'danger';
      default:
        return 'secondary';
    }
  }

  isException(func: FunctionRequest): boolean {
    return func.type === 'EXCEPTION';
  }
}
