package com.auditPersist.repository;

import com.auditPersist.entity.FunctionRequestArgument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionRequestArgumentRepository extends JpaRepository<FunctionRequestArgument, String> {

}
