package com.auditPersist.repository;

import com.auditPersist.entity.HttpRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpRequestRepository extends JpaRepository<HttpRequest, String>, JpaSpecificationExecutor<HttpRequest> {

    Page<HttpRequest> findAll(Specification<HttpRequest> spec, Pageable page);
}
