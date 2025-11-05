package com.auditPersist.service.application;

import com.auditPersist.entity.Application;
import com.auditPersist.exeptions.NoValidDataException;
import com.auditPersist.generic.GenericServiceImpl;
import com.auditPersist.repository.ApplicationRepository;
import com.auditPersist.service.applicationInstance.ApplicationInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@Configuration
@Primary
public class ApplicationServiceImpl extends GenericServiceImpl<Application, ApplicationRepository, String> implements ApplicationService {

    @Autowired
    @Lazy
    ApplicationInstanceService applicationInstanceService;

    @Autowired
    Validator validator;

    public ApplicationServiceImpl(ApplicationRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    public Application createIfNotExist(Application app) {
        Application application = this.findByNameAndVersion(app.getName(),app.getVersion());
        if(application != null)
            return application;
        app.setCreationDate(LocalDateTime.now());
        return this.save(app);
    }

    @Override
    public Application findByNameAndVersion(String applicationName, String applicationVersion) {
        if (applicationName == null || applicationVersion == null)
            return null;
        Optional<Application> optionalApplication = this.getEntityRepository().findByNameAndVersion( applicationName ,  applicationVersion);
        if (optionalApplication!= null && optionalApplication.isPresent())
            return optionalApplication.get();
        return null;
    }

    @Override
    public void cleanApplication(Application app) {
        Application application = this.findByIdElseThrowException(app.getId());
        this.cleanApplicationLogs(application);
    }


    @Override
    public void cleanApplicationLogsById(String id) {
        Application application = this.findByIdElseThrowException(id);
        this.cleanApplicationLogs(application);
    }

    @Override
    public void cleanApplicationLogs(Application application) {
        try {
            this.delete(application);
        } catch (Exception e) {
            throw new NoValidDataException("This Records Cant Be Deleted Contact Admin For More Information: " + e.getMessage());
        }
    }

    @Override
    public void cleanApplicationLogsByNameAndVersion(String applicationName, String applicationVersion) {
        Application application = this.findByNameAndVersion(applicationName,applicationVersion);
        if(application == null)
            throw new NoValidDataException("There is no record with this name and version");
        this.cleanApplicationLogs(application);
    }

    @Override
    public boolean isValidApplicationNameAndVersion(String applicationName, String applicationVersion) {
        if(applicationName == null || applicationName.length() <=2)
            return false;
        return applicationVersion != null && applicationVersion.length() > 2;
    }

    @Override
    public boolean isValidApplicationLogFile(MultipartFile logFile) {
        return !logFile.isEmpty();
    }

    @Override
    public void isValidApplicationElseThrow(Application application,MultipartFile logFile) {
        Set<ConstraintViolation<Application>> violations = validator.validate(application);
        if (!violations.isEmpty())
            throw new NoValidDataException("the application detail have validation error ");
        if(findByNameAndVersion(application.getName(),application.getVersion()) == null)
            throw new NoValidDataException("there is no application with this credentials please try again with different data  ");
        if(!isValidApplicationLogFile(logFile))
            throw new NoValidDataException("log file not valid please try again with different file ");
    }
}