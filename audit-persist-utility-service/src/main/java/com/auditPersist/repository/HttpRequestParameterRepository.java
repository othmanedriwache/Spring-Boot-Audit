package com.auditPersist.repository;

import com.auditPersist.entity.HttpRequestParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpRequestParameterRepository extends JpaRepository<HttpRequestParameter, String> {

}