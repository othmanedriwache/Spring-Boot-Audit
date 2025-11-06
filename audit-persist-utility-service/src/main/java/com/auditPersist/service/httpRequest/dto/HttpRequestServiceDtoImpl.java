package com.auditPersist.service.httpRequest.dto;

import com.auditPersist.entity.HttpRequest;
import com.auditPersist.repository.HttpRequestRepository;
import com.auditPersist.search.SearchableField;
import com.auditPersist.service.httpRequest.HttpRequestServiceImpl;
import com.auditPersist.service.httpRequest.response.*;
import lombok.var;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.auditPersist.constant.AuditConstant.TABLE;
import static com.auditPersist.search.SearchableField.ValidFieldType;

@Service
public class HttpRequestServiceDtoImpl extends HttpRequestServiceImpl implements HttpRequestServiceDto {

    @Autowired
    HttpRequestMapper applicationDtoMapper;

    @Autowired
    HttpRequestBasicMapper httpRequestBasicMapper;

    public static final List<SearchableField> searchableFields = initSearchableFields();
    private Object HttpRequestBasicDto;

    public HttpRequestServiceDtoImpl(HttpRequestRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    public HttpRequestDto findByIdDto(String id) {
        return applicationDtoMapper.entityToBasicDto(this.findById(id));
    }

    @Override
    public HttpRequestDto findByIdElseThrowExceptionDto(String id) {
        var a = this.findByIdElseThrowException(id);
        HttpRequestDto httpRequestDto = applicationDtoMapper.entityToBasicDto(this.findByIdElseThrowException(id));
        return httpRequestDto;
    }

    @Override
    public HttpRequestDto findAsTreeByIdElseThrowExceptionDto(String id) {
        HttpRequestDto httpRequestDto = this.findByIdElseThrowExceptionDto(id);
        List<FunctionRequestDto> functionRequestDtoList = mapperHttpRequestDtoAsChildrenTree(httpRequestDto);
        httpRequestDto.setFunctionRequests(functionRequestDtoList);
        return httpRequestDto;
    }

    @Override
    public Page<HttpRequestBasicDto> findBySearchCriteria(Specification<?> spec, Pageable page) throws BadRequestException {
        Page<HttpRequest> searchResult = null;
        try {
            searchResult = this.getEntityRepository().findAll((Specification<HttpRequest>) spec, page);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        List<HttpRequest> searchResultAsList = searchResult.stream().collect(Collectors.toList());
        List<HttpRequestBasicDto> searchResultAsHttpRequestBasicDtos = httpRequestBasicMapper.entitiesToBasicDtos(searchResultAsList);
        return new PageImpl(searchResultAsHttpRequestBasicDtos, page, searchResult.getTotalElements());
    }

    private List<FunctionRequestDto> mapperHttpRequestDtoAsChildrenTree(HttpRequestDto httpRequestDto) {
        List<FunctionRequestDto> functionRequestDtoWithoutParent =
                httpRequestDto.getFunctionRequests()
                        .stream().filter((functionRequestDto -> functionRequestDto.getParent() == null))
                        .collect(Collectors.toList());

        if (functionRequestDtoWithoutParent.isEmpty())
            return null;
        for (FunctionRequestDto childFunctionRequest : functionRequestDtoWithoutParent)
            childrenTree(httpRequestDto.getFunctionRequests(), childFunctionRequest);
        return functionRequestDtoWithoutParent;
    }

    private FunctionRequestDto childrenTree(List<FunctionRequestDto> functionRequestDtoList, FunctionRequestDto functionRequestDto) {
        List<FunctionRequestDto> functionRequestChildren = findFunctionRequestByParentId(functionRequestDtoList, functionRequestDto.getId());
        if (functionRequestChildren.isEmpty())
            return functionRequestDto;
        for (FunctionRequestDto childIterations : functionRequestChildren)
            childrenTree(functionRequestDtoList, childIterations);
        functionRequestDto.setChildren(functionRequestChildren);
        return functionRequestDto;
    }

    private List<FunctionRequestDto> findFunctionRequestByParentId(List<FunctionRequestDto> functionRequestDtos, String parentId) {
        List<FunctionRequestDto> functionRequestDto = functionRequestDtos.stream()
                .filter((e) -> e.getParent() != null)
                .filter((e) -> e.getParent().getId() == parentId)
                .collect(Collectors.toList());
        return functionRequestDto;
    }

    private static List<SearchableField> initSearchableFields() {
        Field[] classFields = HttpRequestBasicDto.class.getDeclaredFields();
        List<SearchableField> searchableFields =
                Arrays.asList(classFields).stream()
                        .map(elem -> new SearchableField(elem.getName(), elem.getType().getSimpleName()))
                        .map(elem -> {
                            if (!ValidFieldType.contains(elem.getType()))
                                elem.setType(TABLE);
                            return elem;
                        })
                        .collect(Collectors.toList());
        searchableFields.add(new SearchableField("applicationName", "APPLICATION_NAME_TYPE"));
        searchableFields.add(new SearchableField("applicationVersion", "APPLICATION_VERSION_TYPE"));


        return searchableFields;
    }

    @Override
    public List<SearchableField> getSearchableFields() {
        return searchableFields;
    }

}