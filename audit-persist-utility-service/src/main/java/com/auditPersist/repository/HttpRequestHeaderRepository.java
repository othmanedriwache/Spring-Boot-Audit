package com.auditPersist.repository;

import com.auditPersist.entity.HttpRequestHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpRequestHeaderRepository extends JpaRepository<HttpRequestHeader, String> {

}