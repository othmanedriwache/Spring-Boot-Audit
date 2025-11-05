package com.auditPersist.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Objects;

public class SearchQueryBuilder <ENTITY>   implements Specification<ENTITY> {

    @Autowired
    private final SearchCriteria searchCriteria;

    public SearchQueryBuilder(final SearchCriteria searchCriteria){
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<ENTITY> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        Comparable strToSearch = (Comparable)searchCriteria.getValue();

        switch(Objects.requireNonNull(SearchOperation.getSimpleOperation(searchCriteria.getOperation()))){
            case CONTAINS:
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");

            case DOES_NOT_CONTAIN:
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");

            case BEGINS_WITH:
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), strToSearch + "%");

            case DOES_NOT_BEGIN_WITH:
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), strToSearch + "%");

            case ENDS_WITH:
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch);

            case DOES_NOT_END_WITH:
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch);

            case EQUAL:
                return cb.equal(root.get(searchCriteria.getFilterKey()), strToSearch);

            case NOT_EQUAL:
                return cb.notEqual(root.get(searchCriteria.getFilterKey()), strToSearch);

            case NUL:
                return cb.isNull(root.get(searchCriteria.getFilterKey()));

            case NOT_NULL:
                return cb.isNotNull(root.get(searchCriteria.getFilterKey()));

            case GREATER_THAN:
                return cb.greaterThan(root.get(searchCriteria.getFilterKey()), strToSearch);

            case GREATER_THAN_EQUAL:
                return cb.greaterThanOrEqualTo(root.get(searchCriteria.getFilterKey()), strToSearch);

            case LESS_THAN:
                return cb.lessThan(root.get(searchCriteria.getFilterKey()), strToSearch);

            case LESS_THAN_EQUAL:
                return cb.lessThanOrEqualTo(root.get(searchCriteria.getFilterKey()), strToSearch);

            case JOIN_TABLE:
                Join<Object, Object> teamJoin = root.join(searchCriteria.getFilterKey());
                return cb.equal(teamJoin.get("id"), strToSearch);

            case APPLICATION_NAME:
                Join<Object, Object> instanceJoin = root.join("applicationInstance");
                Join<Object, Object> appJoin = instanceJoin.join("application");
                return cb.equal(appJoin.get("name"), strToSearch);
        }
        return null;
    }


}