package com.auditPersist.service.httpRequest;

import com.auditPersist.entity.HttpRequest;
import com.auditPersist.generic.GenericService;
import com.auditPersist.model.LogReader;

public interface HttpRequestService extends GenericService<HttpRequest, String> {

    HttpRequest saveFromLogFileReader(LogReader logReader);

}

