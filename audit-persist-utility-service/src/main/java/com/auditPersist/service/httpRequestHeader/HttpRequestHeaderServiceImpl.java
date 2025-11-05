package com.auditPersist.service.httpRequestHeader;

import com.auditPersist.entity.HttpRequest;
import com.auditPersist.entity.HttpRequestHeader;
import com.auditPersist.generic.GenericServiceImpl;
import com.auditPersist.repository.HttpRequestHeaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class HttpRequestHeaderServiceImpl extends GenericServiceImpl<HttpRequestHeader, HttpRequestHeaderRepository, String> implements HttpRequestHeaderService {

    public HttpRequestHeaderServiceImpl(HttpRequestHeaderRepository entityRepository) { super(entityRepository); }

    @Override
    public List<HttpRequestHeader> saveFromMap(Map<String, List<String>> map, HttpRequest httpRequest) {
        List<HttpRequestHeader> httpRequestHeaders =
                map.entrySet()
                        .stream()
                        .map(entry-> HttpRequestHeader.builder()
                                        .header(entry.getKey())
                                        .content(Objects.toString(entry.getValue(), null))
                                        .httpRequest(httpRequest)
                                        .build())
                        .collect(Collectors.toList());
        return this.saveAll(httpRequestHeaders);
    }

}
