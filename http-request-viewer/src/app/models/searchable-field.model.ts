export interface SearchableField {
  key: string;
  type: string;
  regex: string;
}

export interface SearchableFieldsResponse {
  timeStamp: string;
  statusCode: number;
  status: string;
  developerStatus: string;
  message: string;
  data: SearchableField[];
}
