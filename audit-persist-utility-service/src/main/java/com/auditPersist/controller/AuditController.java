package com.auditPersist.controller;

import com.auditPersist.batch.job.AuditJob;
import com.auditPersist.entity.Application;
import com.auditPersist.response.Response;
import com.auditPersist.response.ResponseService;
import com.auditPersist.service.application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditJob auditJob;
    private final ApplicationService applicationService;

    @Autowired
    public AuditController(AuditJob auditJob, ApplicationService applicationService) {
        this.auditJob = auditJob;
        this.applicationService = applicationService;
    }

    @PostMapping("/import")
    public ResponseEntity<Response> importAuditFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("applicationName") String applicationName,
            @RequestParam("applicationVersion") String applicationVersion) throws Exception {

        Application application = buildApplication(applicationName, applicationVersion);
        validateApplication(application, file);
        Application existingApplication = applicationService.findByNameAndVersion(applicationName, applicationVersion);

        processAuditFile(existingApplication, file);

        return ResponseService.success("Audit file imported successfully");
    }

    private Application buildApplication(String name, String version) {
        return Application.builder()
                .name(name)
                .version(version)
                .build();
    }

    private void validateApplication(Application application, MultipartFile file) throws Exception {
        applicationService.isValidApplicationElseThrow(application, file);
    }

    private void processAuditFile(Application application, MultipartFile file) throws Exception {
        auditJob.importLogFile(application, file);
    }
}