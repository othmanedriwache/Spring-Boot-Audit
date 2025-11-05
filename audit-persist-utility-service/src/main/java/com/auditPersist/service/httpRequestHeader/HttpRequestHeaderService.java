package com.auditPersist.service.httpRequestHeader;

import com.auditPersist.entity.HttpRequest;
import com.auditPersist.entity.HttpRequestHeader;
import com.auditPersist.generic.GenericService;

import java.util.List;
import java.util.Map;

public interface HttpRequestHeaderService extends GenericService<HttpRequestHeader, String> {

    List<HttpRequestHeader> saveFromMap(Map<String, List<String>> map , HttpRequest httpRequest);
}
