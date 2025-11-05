package com.auditPersist.search;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    public SpecificationBuilder createSpecificationFromSearchCriteria(SearchDto searchDto) {
        SpecificationBuilder builder = new SpecificationBuilder();

        if (searchDto.getDataOption() != null) {
            builder.withDataOption(searchDto.getDataOption());
        }

        List<SearchCriteria> criteriaList = searchDto.getSearchCriteriaList();
        if (criteriaList != null && !criteriaList.isEmpty()) {
            criteriaList.forEach(criteria -> {
                builder.with(criteria);
            });
        }

        // Add default sort by inputDate DESC if not already specified
        boolean hasInputDateSort = criteriaList != null && criteriaList.stream()
                .anyMatch(c -> "inputDate".equals(c.getFilterKey()) &&
                        c.getSortAction() != null &&
                        !c.getSortAction().isEmpty());

        if (!hasInputDateSort) {
            // Create a sort-only criteria (no operation, no value)
            // This will be picked up by the sort() method but ignored by build()
            SearchCriteria defaultSort = new SearchCriteria();
            defaultSort.setFilterKey("inputDate");
            defaultSort.setSortAction("DESC");
            defaultSort.setOperation(null); // No operation for sort-only criteria
            defaultSort.setValue(null); // No value for sort-only criteria
            builder.with(defaultSort);
        }
        return builder;
    }

    public List<CriteriaInvalid> isCriteriaValid(
            SpecificationBuilder builder,
            List<SearchableField> searchableFields) {
        return builder.isSearchCriteriaValid(searchableFields);
    }
}