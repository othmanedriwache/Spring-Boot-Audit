
package com.auditPersist.batch.step;

import com.auditPersist.batch.task.audit.AuditOrganizer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizerStep {

    private final StepBuilderFactory steps;
    private final AuditOrganizer auditOrganizer;

    @Autowired
    public OrganizerStep(StepBuilderFactory steps, AuditOrganizer auditOrganizer) {
        this.steps = steps;
        this.auditOrganizer = auditOrganizer;
    }

    public Step organizer() {
        return steps.get("organize-audit-tree")
                .tasklet(auditOrganizer)
                .build();
    }
}