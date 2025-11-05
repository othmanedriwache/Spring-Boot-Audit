package com.auditPersist.service.httpRequestParameter;

import com.auditPersist.entity.HttpRequest;
import com.auditPersist.entity.HttpRequestParameter;
import com.auditPersist.generic.GenericService;

import java.util.List;
import java.util.Map;

public interface HttpRequestParameterService extends GenericService<HttpRequestParameter, String> {

    List<HttpRequestParameter> saveFromMap(Map<String, String[]> map , HttpRequest httpRequest);
}

