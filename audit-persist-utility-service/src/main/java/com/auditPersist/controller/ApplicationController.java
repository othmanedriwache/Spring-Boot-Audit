package com.auditPersist.controller;

import com.auditPersist.entity.Application;
import com.auditPersist.response.Response;
import com.auditPersist.response.ResponseService;
import com.auditPersist.service.application.dto.ApplicationServiceDto;
import com.auditPersist.service.application.response.ApplicationWithInstancesDto;
import com.auditPersist.service.applicationInstance.ApplicationInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationServiceDto applicationService;
    private final ApplicationInstanceService applicationInstanceService;

    @Autowired
    public ApplicationController(ApplicationServiceDto applicationService,
                                 ApplicationInstanceService applicationInstanceService) {
        this.applicationService = applicationService;
        this.applicationInstanceService = applicationInstanceService;
    }

    @GetMapping("/all-with-instances")
    public ResponseEntity<Response> getAllApplicationsWithInstances() {
        List<ApplicationWithInstancesDto> applications = applicationService.getAllApplicationsWithInstances();
        return ResponseService.success(
                "Applications retrieved successfully",
                applications
        );
    }

    @DeleteMapping("/clean")
    public ResponseEntity<Response> cleanApplication(@Valid @RequestBody Application application) {
        applicationService.cleanApplication(application);
        return ResponseService.success("Application log cleaned");
    }

    @DeleteMapping("/cleanById/{applicationInstanceId}")
    public ResponseEntity<Response> cleanApplicationById(@PathVariable String applicationInstanceId) {
        applicationService.cleanApplicationLogsById(applicationInstanceId);
        return ResponseService.success("Application log cleaned");
    }

    @DeleteMapping("/cleanByNameAndVersion/{applicationName}/{applicationVersion}")
    public ResponseEntity<Response> cleanByNameAndVersion(
            @PathVariable String applicationName,
            @PathVariable String applicationVersion) {
        applicationService.cleanApplicationLogsByNameAndVersion(applicationName, applicationVersion);
        return ResponseService.success("Application log cleaned");
    }

    @DeleteMapping("/instance/clean/{applicationInstanceId}")
    public ResponseEntity<Response> cleanApplicationInstanceLogsById(@PathVariable String applicationInstanceId) {
        applicationInstanceService.cleanApplicationInstanceLogsById(applicationInstanceId);
        return ResponseService.success("Application instance logs cleaned successfully");
    }

    @PostMapping("/create")
    public ResponseEntity<Response> create(@Valid @RequestBody Application application) {
        return ResponseService.success(
                "Application created successfully",
                applicationService.createIfNotExistDto(application)
        );
    }
}