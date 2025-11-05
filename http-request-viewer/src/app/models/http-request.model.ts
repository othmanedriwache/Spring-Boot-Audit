export interface HttpRequest {
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

export interface ApiResponse<T> {
  timeStamp: string;
  statusCode: number;
  status: string;
  developerStatus: string;
  message: string;
  data: T;
}

export interface PageableResponse<T> {
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
