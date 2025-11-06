package com.auditPersist.search;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.auditPersist.constant.AuditConstant.*;
import static com.auditPersist.search.SearchOperation.getSimpleOperationSet;
import static com.auditPersist.utils.ClassTypeValidator.*;

public class SpecificationBuilder {

    private static final Logger log = LoggerFactory.getLogger(SpecificationBuilder.class);

    private final List<SearchCriteria> params;
    private String dataOption;

    public SpecificationBuilder(){
        this.params = new ArrayList<>();
        this.dataOption = "ALL"; // default value
    }

    public SpecificationBuilder withDataOption(String dataOption) {
        this.dataOption = dataOption;
        log.info("DataOption set to: {}", this.dataOption);
        return this;
    }

    public final SpecificationBuilder with(String key, String operation, Object value){
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public final SpecificationBuilder with(SearchCriteria searchCriteria){
        params.add(searchCriteria);
        log.info("Added SearchCriteria - Key: {}, Operation: {}, Value: {}",
                searchCriteria.getFilterKey(),
                searchCriteria.getOperation(),
                searchCriteria.getValue());
        return this;
    }

    public List<Sort.Order> sort(){
        List<Sort.Order> sorts = new ArrayList<>();
        if(params.size() == 0){
            return sorts;
        }
        for (SearchCriteria searchCriteria : params){
            if(searchCriteria.getSortAction() != null && !searchCriteria.getSortAction().isEmpty()){
                if (searchCriteria.getSortAction().equalsIgnoreCase("DESC"))
                    sorts.add(new Sort.Order(Sort.Direction.DESC, searchCriteria.getFilterKey()));
                else
                    sorts.add(new Sort.Order(Sort.Direction.ASC, searchCriteria.getFilterKey()));
            }
        }
        return sorts;
    }

    public Specification<?> build(){
        List<SearchCriteria> filterCriteria = new ArrayList<>();
        for (SearchCriteria param : params) {
            if (param.getOperation() != null && !param.getOperation().isEmpty()) {
                filterCriteria.add(param);
            }
        }

        if(filterCriteria.size() == 0){
            return null;
        }

        log.info("Building specification with dataOption: {}", this.dataOption);
        log.info("Total criteria count: {}", filterCriteria.size());

        Specification<?> result = new SearchQueryBuilder(filterCriteria.get(0));

        for (int idx = 1; idx < filterCriteria.size(); idx++){
            SearchCriteria criteria = filterCriteria.get(idx);

            SearchOperation operation = SearchOperation.getDataOption(this.dataOption);
            boolean isAnd = operation == SearchOperation.ALL;

            log.info("Combining criteria {} with {} logic. DataOption: {}, SearchOperation: {}",
                    idx,
                    isAnd ? "AND" : "OR",
                    this.dataOption,
                    operation);

            result = isAnd
                    ? Specification.where(result).and(new SearchQueryBuilder(criteria))
                    : Specification.where(result).or(new SearchQueryBuilder(criteria));
        }

        return result;
    }

    public List<CriteriaInvalid> isSearchCriteriaValid(List<SearchableField> searchableFields) {
        List<CriteriaInvalid> criteriaInvalids = new ArrayList<>();
        CriteriaInvalid criteriaInvalid;

        for (SearchCriteria criteria : params){
            // Skip validation for sort-only criteria (no operation)
            if (criteria.getOperation() == null || criteria.getOperation().isEmpty()) {
                log.info("Skipping validation for sort-only criteria: {}", criteria.getFilterKey());
                continue;
            }

            SearchableField searchableField = findSearchableFieldBySearchCriteria(criteria, searchableFields);
            criteriaInvalid = validSearchCriteria(criteria, searchableField);
            if (criteriaInvalid != null)
                criteriaInvalids.add(criteriaInvalid);
            else
                criteria.setValue(convertValueWithCorespondentType(criteria.getValue(), searchableField));
        }
        return criteriaInvalids;
    }

    public CriteriaInvalid validSearchCriteria(SearchCriteria searchCriteria, SearchableField searchableField){
        if (searchableField == null)
            return new CriteriaInvalid(searchCriteria.getFilterKey(), NOT_SEARCHABLE_KEY);
        if(!getSimpleOperationSet().containsKey(searchableField.getType()))
            return new CriteriaInvalid(searchCriteria.getFilterKey(), OPERATION_TYPE_NOT_EXIST);
        if (!getSimpleOperationSet().get(searchableField.getType()).contains(searchCriteria.getOperation()))
            return new CriteriaInvalid(searchCriteria.getFilterKey(), OPERATION_NOT_ALLOWED_WITH_THIS_FIELD_TYPE, getSimpleOperationSet().get(searchableField.getType()));

        // Skip type conversion for custom operations like APPLICATION_NAME and APPLICATION_VERSION
        if (!"APPLICATION_NAME_TYPE".equals(searchableField.getType()) &&
                !"APPLICATION_VERSION_TYPE".equals(searchableField.getType())) {
            try {
                convertValueWithCorespondentType(searchCriteria.getValue(), searchableField);
            } catch (Exception e){
                return new CriteriaInvalid(searchableField.getKey(), THE_VALUE_NO_COMPATIBLE_WITH_SEARCHABLE_TYPE, Collections.singletonList("TYPE " + searchableField.getType() + " REGEX " + searchableField.getRegex()));
            }
        }
        return null;
    }

    private SearchableField findSearchableFieldBySearchCriteria(SearchCriteria criteria, List<SearchableField> searchableFields){
        Optional<SearchableField> searchableField = searchableFields.stream().filter((searchable)->
                searchable.getKey().equals(criteria.getFilterKey())
        ).findFirst();
        return searchableField.orElse(null);
    }

    private Object convertValueWithCorespondentType(Object value, SearchableField searchableField){
        String valueAsString = value.toString();
        switch (searchableField.getType()){
            case INT:
            case INTEGER:
                return getStringAsInteger(valueAsString);
            case LONG:
                return getStringAsLong(valueAsString);
            case BOOLEAN:
                return getStringAsBoolean(valueAsString);
            case LOCAL_DATE_TIME:
                return getStringAsLocalDateTime(valueAsString);
            case DOUBLE:
                return getStringAsDouble(valueAsString);
        }
        return valueAsString;
    }

    public static Long getStringAsLong(String value) {
        return Long.parseLong(value);
    }
}