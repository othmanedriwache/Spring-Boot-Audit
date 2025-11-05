export interface SearchCriteria {
  filterKey: string;
  operation: string;
  value: any;
  sortAction?: string;
}

export interface SearchDto {
  dataOption: string;
  searchCriteriaList: SearchCriteria[];
}
