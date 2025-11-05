package com.auditPersist.repository;

import com.auditPersist.entity.BusinessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRequestRepository extends JpaRepository<BusinessRequest, String> {


    @Query(nativeQuery = true,
            value = "SELECT fr.* FROM business_requests  fr " +
                    " WHERE fr.parent_tree_status =  ?1")
    List<BusinessRequest> findByParentTreeStatus(String parentTreeStatus);

}


