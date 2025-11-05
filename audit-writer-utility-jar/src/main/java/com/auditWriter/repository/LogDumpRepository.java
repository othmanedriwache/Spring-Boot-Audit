package com.auditWriter.repository;

import com.auditWriter.entity.LogDump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogDumpRepository extends JpaRepository<LogDump, String> {

    @Query("SELECT DISTINCT l.applicationInstance, l.applicationName, l.applicationVersion FROM LogDump l WHERE l.exception IS NULL AND l.persisted IS NULL")
    List<Object[]> findDistinctApplicationDetails();


}
