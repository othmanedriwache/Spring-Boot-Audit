package com.auditPersist.service.httpRequestParameter;

import com.auditPersist.entity.HttpRequest;
import com.auditPersist.entity.HttpRequestParameter;
import com.auditPersist.generic.GenericServiceImpl;
import com.auditPersist.repository.HttpRequestParameterRepository;
import com.auditPersist.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HttpRequestParameterServiceImpl extends GenericServiceImpl<HttpRequestParameter, HttpRequestParameterRepository, String> implements HttpRequestParameterService {

    public HttpRequestParameterServiceImpl(HttpRequestParameterRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    public List<HttpRequestParameter> saveFromMap(Map<String, String[]> map , HttpRequest httpRequest) {
        List<HttpRequestParameter> httpRequestParameters =
                map.entrySet()
                        .stream()
                        .map(entry->  HttpRequestParameter.builder()
                                .parameter(entry.getKey())
                                .content(StringUtil.convertListOfObjectsToOneStringWithDeliminator(entry.getValue(),","))
                                .httpRequest(httpRequest)
                                .build())
                        .collect(Collectors.toList());

        return this.saveAll(httpRequestParameters);
    }
}