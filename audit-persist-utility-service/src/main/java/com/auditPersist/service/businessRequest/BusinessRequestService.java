package com.auditPersist.service.businessRequest;

import com.auditPersist.entity.BusinessRequest;
import com.auditPersist.generic.GenericService;
import com.auditPersist.model.LogReader;

public interface BusinessRequestService extends GenericService<BusinessRequest, String> {


    BusinessRequest saveFromLogFileReader(LogReader logReader);

    void organizeBusinessTree();
}
