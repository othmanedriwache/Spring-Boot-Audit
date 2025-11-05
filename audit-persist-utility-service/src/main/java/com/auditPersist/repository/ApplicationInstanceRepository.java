package com.auditPersist.repository;

import com.auditPersist.entity.ApplicationInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationInstanceRepository extends JpaRepository<ApplicationInstance, String> {

    @Query(nativeQuery=true,
            value = "SELECT " +
                    "MAX(GREATEST(" +
                    "COALESCE(CAST(function_requests.input_line AS DECIMAL), 0), " +
                    "COALESCE(CAST(function_requests.output_line AS DECIMAL), 0), " +
                    "COALESCE(CAST(http_requests.input_line AS DECIMAL), 0), " +
                    "COALESCE(CAST(http_requests.output_line AS DECIMAL), 0)" +
                    ")) " +
                    "FROM http_requests " +
                    "LEFT JOIN function_requests ON http_requests.id = function_requests.http_requests_id " +
                    "WHERE http_requests.application_instances_id = ?1"
    )
    Optional<String> findMaxLineInHttpRequestAndFunctionRequest(String ApplicationInstanceId);

    // Custom delete methods with native SQL
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM function_requests_arguments WHERE function_requests_id IN " +
            "(SELECT fr.id FROM function_requests fr " +
            "JOIN http_requests hr ON fr.http_requests_id = hr.id " +
            "WHERE hr.application_instances_id = :applicationInstanceId)")
    void deleteFunctionRequestArgumentsByApplicationInstanceId(@Param("applicationInstanceId") String applicationInstanceId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM business_requests WHERE http_requests_id IN " +
            "(SELECT id FROM http_requests WHERE application_instances_id = :applicationInstanceId)")
    void deleteBusinessRequestsByHttpRequestApplicationInstanceId(@Param("applicationInstanceId") String applicationInstanceId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM business_requests WHERE function_requests_id IN " +
            "(SELECT fr.id FROM function_requests fr " +
            "JOIN http_requests hr ON fr.http_requests_id = hr.id " +
            "WHERE hr.application_instances_id = :applicationInstanceId)")
    void deleteBusinessRequestsByFunctionRequestApplicationInstanceId(@Param("applicationInstanceId") String applicationInstanceId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM function_requests WHERE http_requests_id IN " +
            "(SELECT id FROM http_requests WHERE application_instances_id = :applicationInstanceId)")
    void deleteFunctionRequestsByApplicationInstanceId(@Param("applicationInstanceId") String applicationInstanceId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM http_requests_parameters WHERE http_requests_id IN " +
            "(SELECT id FROM http_requests WHERE application_instances_id = :applicationInstanceId)")
    void deleteHttpRequestParametersByApplicationInstanceId(@Param("applicationInstanceId") String applicationInstanceId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM http_requests_headers WHERE http_headers_id IN " +
            "(SELECT id FROM http_requests WHERE application_instances_id = :applicationInstanceId)")
    void deleteHttpRequestHeadersByApplicationInstanceId(@Param("applicationInstanceId") String applicationInstanceId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM http_requests WHERE application_instances_id = :applicationInstanceId")
    void deleteHttpRequestsByApplicationInstanceId(@Param("applicationInstanceId") String applicationInstanceId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM application_instances WHERE id = :applicationInstanceId")
    void deleteApplicationInstanceById(@Param("applicationInstanceId") String applicationInstanceId);
}