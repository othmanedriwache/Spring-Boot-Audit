package com.auditPersist.batch.job;

import com.auditPersist.batch.step.AuditDatabaseStep;
import com.auditPersist.batch.step.OrganizerStep;
import com.auditPersist.entity.ApplicationInstance;
import com.auditPersist.service.applicationInstance.ApplicationInstanceService;
import com.auditPersist.utils.ApplicationInstanceManager;
import com.auditWriter.service.logDumpService.LogDumpService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@EnableBatchProcessing
public class DatabaseAuditJob {

    private final JobBuilderFactory jobBuilders;
    private final AuditDatabaseStep auditStep;
    private final OrganizerStep organizerStep;
    private final ApplicationInstanceService applicationInstanceService;
    private final LogDumpService logDumpService;
    private final JobLauncher jobLauncher;

    @Autowired
    public DatabaseAuditJob(JobBuilderFactory jobBuilders,
                            AuditDatabaseStep auditStep,
                            OrganizerStep organizerStep,
                            @Lazy ApplicationInstanceService applicationInstanceService,
                            LogDumpService logDumpService,
                            JobLauncher jobLauncher) {
        this.jobBuilders = jobBuilders;
        this.auditStep = auditStep;
        this.organizerStep = organizerStep;
        this.applicationInstanceService = applicationInstanceService;
        this.logDumpService = logDumpService;
        this.jobLauncher = jobLauncher;
    }

    @Scheduled(fixedRateString = "${scheduler.persist-log-dump-rate}")
    public void persistDatabaseLogDump() {
        List<ApplicationInstance> instances = fetchUnpersistedApplicationInstances();
        instances.forEach(this::processSingleInstance);
    }

    private List<ApplicationInstance> fetchUnpersistedApplicationInstances() {
        return logDumpService.getNotPersistedApplicationInstance()
                .stream()
                .map(applicationInstanceService::getCurrentApplicationInstanceByLogDumpInstance)
                .collect(Collectors.toList());
    }

    private void processSingleInstance(ApplicationInstance instance) {
        try {
            ApplicationInstanceManager.addApplicationInstance(instance.getId(), instance);
            jobLauncher.run(buildPersistenceJob(instance), createUniqueJobParameters());
        } catch (Exception e) {
            throw new RuntimeException("Failed to process instance: " + instance.getId(), e);
        }
    }

    private Job buildPersistenceJob(ApplicationInstance instance) {
        return jobBuilders.get("persist-database-audit-logs")
                .incrementer(new RunIdIncrementer())
                .start(auditStep.auditChunkStep(instance))
                .next(organizerStep.organizer())
                .build();
    }

    private JobParameters createUniqueJobParameters() {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));
        return new JobParameters(params);
    }
}