package com.auditPersist.service.httpRequest.dto;

import com.auditPersist.search.SearchableField;
import com.auditPersist.service.httpRequest.HttpRequestService;
import com.auditPersist.service.httpRequest.response.HttpRequestBasicDto;
import com.auditPersist.service.httpRequest.response.HttpRequestDto;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface HttpRequestServiceDto extends HttpRequestService {

    List<SearchableField> getSearchableFields();

    HttpRequestDto findByIdDto(String id);

    HttpRequestDto findByIdElseThrowExceptionDto(String id);

    HttpRequestDto findAsTreeByIdElseThrowExceptionDto(String id);

    Page<HttpRequestBasicDto> findBySearchCriteria(Specification<?> spec, Pageable page) throws BadRequestException;
}
