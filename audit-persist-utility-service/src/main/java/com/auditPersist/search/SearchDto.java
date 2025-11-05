package com.auditPersist.search;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {

    private List<SearchCriteria> searchCriteriaList;

    // Set default value to "ALL" for AND logic
    private String dataOption = "ALL"; // or "ANY" for OR logic
}