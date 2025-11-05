
package com.auditPersist.controller;

import com.auditPersist.response.Response;
import com.auditPersist.response.ResponseService;
import com.auditPersist.search.CriteriaInvalid;
import com.auditPersist.search.SearchDto;
import com.auditPersist.search.SpecificationBuilder;
import com.auditPersist.search.SpecificationService;
import com.auditPersist.service.httpRequest.dto.HttpRequestServiceDto;
import com.auditPersist.service.httpRequest.response.HttpRequestBasicDto;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/httpRequest")
@CrossOrigin(origins = "*")
public class HttpRequestController {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final HttpRequestServiceDto httpRequestService;
    private final SpecificationService specificationService;

    @Autowired
    public HttpRequestController(HttpRequestServiceDto httpRequestService,
                                 SpecificationService specificationService) {
        this.httpRequestService = httpRequestService;
        this.specificationService = specificationService;
    }

    @GetMapping("/get")
    public ResponseEntity<Response> getHttpRequest(@RequestParam("id") String id) {
        return ResponseService.success(
                "Http request info",
                httpRequestService.findByIdElseThrowExceptionDto(id)
        );
    }

    @GetMapping("/getAsTree")
    public ResponseEntity<Response> getHttpRequestAsTree(@RequestParam("id") String id) {
        return ResponseService.success(
                "Http request info",
                httpRequestService.findAsTreeByIdElseThrowExceptionDto(id)
        );
    }

    @PostMapping("/search")
    public ResponseEntity<Response> searchHttpRequests(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestBody SearchDto searchDto) throws BadRequestException {

        SpecificationBuilder specification = buildSpecification(searchDto);
        List<CriteriaInvalid> validationErrors = validateSearchCriteria(specification);

        if (!validationErrors.isEmpty()) {
            return ResponseService.bad_request(validationErrors);
        }

        Page<HttpRequestBasicDto> results = executeSearch(specification, pageNum, pageSize);
        return ResponseService.success("Successfully retrieved http requests", results);
    }

    @GetMapping("/getHttpRequestSearchableFields")
    public ResponseEntity<Response> getSearchableFields() {
        return ResponseService.success(
                "Http request searchable fields",
                httpRequestService.getSearchableFields()
        );
    }

    private SpecificationBuilder buildSpecification(SearchDto searchDto) throws BadRequestException {
        return specificationService.createSpecificationFromSearchCriteria(searchDto);
    }

    private List<CriteriaInvalid> validateSearchCriteria(SpecificationBuilder specification) {
        return specificationService.isCriteriaValid(
                specification,
                httpRequestService.getSearchableFields()
        );
    }

    private Page<HttpRequestBasicDto> executeSearch(SpecificationBuilder specification,
                                                    int pageNum,
                                                    int pageSize) throws BadRequestException {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(specification.sort()));
        return httpRequestService.findBySearchCriteria(specification.build(), pageable);
    }
}