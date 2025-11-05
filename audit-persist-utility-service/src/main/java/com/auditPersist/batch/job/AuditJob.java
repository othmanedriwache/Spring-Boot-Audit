package com.auditPersist.batch.job;

import com.auditPersist.batch.step.ApplicationInstanceStep;
import com.auditPersist.batch.step.AuditStep;
import com.auditPersist.batch.step.OrganizerStep;
import com.auditPersist.entity.Application;
import com.auditPersist.entity.ApplicationInstance;
import com.auditPersist.exeptions.NoRecordException;
import com.auditPersist.service.applicationInstance.ApplicationInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static com.auditPersist.constant.AuditConstant.APPLICATION_INSTANCE_ID_SEPARATOR;
import static org.springframework.batch.core.BatchStatus.FAILED;

@Component
@EnableBatchProcessing
public class AuditJob {

    private final JobBuilderFactory jobBuilders;
    private final AuditStep auditStep;
    private final ApplicationInstanceStep applicationInstanceStep;
    private final ApplicationInstanceService applicationInstanceService;
    private final OrganizerStep organizerStep;
    private final JobLauncher jobLauncher;

    @Autowired
    public AuditJob(JobBuilderFactory jobBuilders,
                    AuditStep auditStep,
                    ApplicationInstanceStep applicationInstanceStep,
                    @Lazy ApplicationInstanceService applicationInstanceService,
                    OrganizerStep organizerStep,
                    JobLauncher jobLauncher) {
        this.jobBuilders = jobBuilders;
        this.auditStep = auditStep;
        this.applicationInstanceStep = applicationInstanceStep;
        this.applicationInstanceService = applicationInstanceService;
        this.organizerStep = organizerStep;
        this.jobLauncher = jobLauncher;
    }

    public void importLogFile(Application application, MultipartFile file) throws Exception {
        Resource logFile = file.getResource();
        ApplicationInstance applicationInstance = resolveApplicationInstance(application, logFile);

        if (applicationInstance == null) {
            throw new NoRecordException("Invalid or empty file format");
        }

        persistLogFile(applicationInstance, logFile);
    }

    private ApplicationInstance resolveApplicationInstance(Application application, Resource logFile) throws Exception {
        try {
            JobExecution execution = jobLauncher.run(
                    buildApplicationInstanceJob(logFile),
                    createUniqueJobParameters()
            );

            if (execution.getStatus() == FAILED) {
                return handleFailedExecution(application, execution);
            }
        } catch (Exception e) {
            throw new Exception("Failed to resolve application instance", e);
        }
        return null;
    }

    private ApplicationInstance handleFailedExecution(Application application, JobExecution execution) {
        String instanceId = extractApplicationInstanceId(execution);

        if (isValidInstanceId(instanceId)) {
            return applicationInstanceService.findById(instanceId);
        }

        return applicationInstanceService.createNewInstance(application);
    }

    private String extractApplicationInstanceId(JobExecution execution) {
        return StringUtils.substringBetween(
                execution.getExitStatus().getExitDescription(),
                APPLICATION_INSTANCE_ID_SEPARATOR,
                APPLICATION_INSTANCE_ID_SEPARATOR
        );
    }

    private boolean isValidInstanceId(String instanceId) {
        return instanceId != null &&
                !instanceId.isEmpty() &&
                !instanceId.equals("null");
    }

    private Job buildApplicationInstanceJob(Resource logFile) {
        return jobBuilders.get("fetch-application-instance")
                .incrementer(new RunIdIncrementer())
                .start(applicationInstanceStep.applicationInstanceChunkStep(logFile))
                .build();
    }

    private void persistLogFile(ApplicationInstance applicationInstance, Resource logFile) throws Exception {
        try {
            jobLauncher.run(
                    buildPersistenceJob(applicationInstance, logFile),
                    createUniqueJobParameters()
            );
        } catch (Exception e) {
            throw new Exception("Failed to persist log file: " + e.getMessage(), e);
        }
    }

    private Job buildPersistenceJob(ApplicationInstance applicationInstance, Resource logFile) {
        return jobBuilders.get("persist-audit-logs")
                .incrementer(new RunIdIncrementer())
                .start(auditStep.auditChunkStep(applicationInstance, logFile))
                .next(organizerStep.organizer())
                .build();
    }

    private JobParameters createUniqueJobParameters() {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));
        return new JobParameters(params);
    }
}