package com.auditPersist.repository;

import com.auditPersist.entity.FunctionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FunctionRequestRepository extends JpaRepository<FunctionRequest, String> {

    @Query(nativeQuery=true,
            value = "SELECT fr.* FROM function_requests  fr " +
                    " WHERE fr.parent_tree_status =  ?1" )
    List<FunctionRequest> findByParentTreeStatus(String parentTreeStatus);

    @Query(nativeQuery = true,
            value = "    SELECT  fr.* from function_requests  fr\n" +
                    "    where  fr.http_requests_id = ?1 \n" +
                    "    and  fr.input_date <= ?2  and fr.output_date >= ?3" +
                    "    and  fr.id <> ?4 " +
                    "    order by fr.input_date  desc\n")
    List<FunctionRequest> findFunctionRequestParent(String httpRequestsId, LocalDateTime inputDate, LocalDateTime outputDate, String currentId);

    @Query(nativeQuery = true,
            value = "    SELECT  fr.* from function_requests  fr\n" +
                    "    where  fr.output_line is null" +
                    "    order by fr.input_date  desc \n")
    List<FunctionRequest> findFunctionRequestWhereOutputLineIsNull();

    @Query(nativeQuery = true,
            value = " select * from function_requests\n" +
                    " where exception_function_requests_id is not null\n" +
                    " and parent_function_requests_id is not null \n" +
                    " and exception_function_requests_id = parent_function_requests_id")
    List<FunctionRequest> getFunctionRequestWhereParentIdEqualExceptionId();
    
    
    @Query(nativeQuery = true,
            value = "    SELECT  fr.* from function_requests  fr\n" +
                    "    where  fr.http_requests_id = ?1 \n" +
                    "    and fr.type = ?2 " +
                    "    and fr.id not in (SELECT  fr2.exception_function_requests_id from function_requests  fr2 where  fr2.http_requests_id = ?1 and fr2.exception_function_requests_id is not null)")
    List<FunctionRequest> findFunctionRequestByHttpRequestsIdAndTypeAndTokenBefore(String httpRequestsId,String type);

    @Query(nativeQuery = true,
            value = " SELECT  fr.* from function_requests  fr" +
                    " where  fr.http_requests_id = ?1 " +
                    " and  fr.input_date <= ?2  and fr.output_date >=  ?2 " +
                    " order by fr.input_date  desc")
    List<FunctionRequest> findBusinessRequestRelatedFunctionRequest(String httpRequestsId, LocalDateTime inputDate);
    
    

}
