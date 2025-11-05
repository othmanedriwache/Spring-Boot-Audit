
package com.auditPersist.batch.task.audit;

import com.auditPersist.service.auditPersistService.AuditPersistService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditOrganizer implements Tasklet {

    private final AuditPersistService auditPersistService;

    @Autowired
    public AuditOrganizer(AuditPersistService auditPersistService) {
        this.auditPersistService = auditPersistService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        auditPersistService.organizeAuditTree();
        return RepeatStatus.FINISHED;
    }
}