package com.auditPersist.service.applicationInstance;

import com.auditPersist.entity.Application;
import com.auditPersist.entity.ApplicationInstance;
import com.auditPersist.entity.FunctionRequest;
import com.auditPersist.entity.HttpRequest;
import com.auditPersist.exeptions.NoValidDataException;
import com.auditPersist.generic.GenericServiceImpl;
import com.auditPersist.repository.ApplicationInstanceRepository;
import com.auditPersist.service.application.ApplicationService;
import com.auditPersist.service.httpRequest.HttpRequestService;
import com.auditWriter.entity.LogDump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
public class ApplicationInstanceServiceImpl extends GenericServiceImpl<ApplicationInstance, ApplicationInstanceRepository, String> implements ApplicationInstanceService {

    @Autowired
    @Lazy
    HttpRequestService httpRequestService;

    @Autowired
    ApplicationService applicationService;

    public ApplicationInstanceServiceImpl(ApplicationInstanceRepository entityRepository) {
        super(entityRepository);
    }


    @Override
    public String getCurrentApplicationInstanceByHttpRequestId(String httpRequestId) {
        if (httpRequestId == null)
            return null;
        HttpRequest httpRequest = httpRequestService.findById(httpRequestId);
        if (httpRequest == null)
            return null;
        ApplicationInstance applicationInstance = this.findById(httpRequest.getApplicationInstance().getId());
        if (applicationInstance == null)
            return null;
        return applicationInstance.getId();
    }

    @Override
    public ApplicationInstance getCurrentApplicationInstanceByLogDumpInstance(LogDump logDumpInstance) {
        Optional<ApplicationInstance> optionalApplicationInstance = this.getEntityRepository().findById(logDumpInstance.getApplicationInstance());
        if (optionalApplicationInstance.isPresent())
            return optionalApplicationInstance.get();
        Application applicationTemp = Application.builder()
                .name(logDumpInstance.getApplicationName())
                .version(logDumpInstance.getApplicationVersion())
                .build();
        Application application = applicationService.createIfNotExist(applicationTemp);
        ApplicationInstance applicationInstance = ApplicationInstance.builder()
                .application(application)
                .id(logDumpInstance.getApplicationInstance())
                .creationDate(LocalDateTime.now())
                .build();
        applicationInstance = this.save(applicationInstance);
        return applicationInstance;
    }

    @Override
    public String findApplicationInstanceCurrentLine(ApplicationInstance applicationInstance) {
        Optional<String>  result = this.getEntityRepository().findMaxLineInHttpRequestAndFunctionRequest(applicationInstance.getId());
        return result.orElse("0");
    }

    @Override
    @Transactional
    public void cleanApplicationInstanceLogsById(String applicationInstanceId) {
        // Verify the application instance exists
        ApplicationInstance applicationInstance = this.findByIdElseThrowException(applicationInstanceId);
        this.cleanApplicationInstance(applicationInstance);
    }


    @Override
    @Transactional
    public void cleanApplicationInstance(ApplicationInstance applicationInstance) {
        try {
            String applicationInstanceId = applicationInstance.getId();

            // Delete in the correct order to respect foreign key constraints
            // 1. Delete function request arguments (deepest level)
            this.getEntityRepository().deleteFunctionRequestArgumentsByApplicationInstanceId(applicationInstanceId);

            // 2. Delete business requests (can be linked to both http and function requests)
            this.getEntityRepository().deleteBusinessRequestsByFunctionRequestApplicationInstanceId(applicationInstanceId);
            this.getEntityRepository().deleteBusinessRequestsByHttpRequestApplicationInstanceId(applicationInstanceId);

            // 3. Delete function requests
            this.getEntityRepository().deleteFunctionRequestsByApplicationInstanceId(applicationInstanceId);

            // 4. Delete http request parameters and headers
            this.getEntityRepository().deleteHttpRequestParametersByApplicationInstanceId(applicationInstanceId);
            this.getEntityRepository().deleteHttpRequestHeadersByApplicationInstanceId(applicationInstanceId);

            // 5. Delete http requests
            this.getEntityRepository().deleteHttpRequestsByApplicationInstanceId(applicationInstanceId);

            // 6. Finally delete the application instance itself
            this.getEntityRepository().deleteApplicationInstanceById(applicationInstanceId);

            // Flush to ensure all changes are committed
            this.getEntityRepository().flush();

        } catch (Exception e) {
            throw new NoValidDataException("This Records Can't Be Deleted. Contact Admin For More Information: " + e.getMessage());
        }
    }

    public ApplicationInstance createNewInstance(Application application){
        ApplicationInstance applicationInstance = ApplicationInstance.builder()
                .id(UUID.randomUUID().toString())
                .application(application)
                .creationDate(LocalDateTime.now())
                .build();
        return this.save(applicationInstance);
    }
}