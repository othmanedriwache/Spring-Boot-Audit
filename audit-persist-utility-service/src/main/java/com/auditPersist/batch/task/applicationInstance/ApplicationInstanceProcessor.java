
package com.auditPersist.batch.task.applicationInstance;

import com.google.gson.Gson;
import com.auditPersist.constant.AuditConstant;
import com.auditPersist.exeptions.StopJobException;
import com.auditPersist.model.LogInfo;
import com.auditPersist.model.LogReader;
import com.auditPersist.service.applicationInstance.ApplicationInstanceService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static com.auditPersist.constant.AuditConstant.APPLICATION_INSTANCE_ID_SEPARATOR;

@Component
public class ApplicationInstanceProcessor implements ItemProcessor<LogReader, LogReader> {

    private final ApplicationInstanceService applicationInstanceService;
    private final Gson gson;

    @Autowired
    public ApplicationInstanceProcessor(@Lazy ApplicationInstanceService applicationInstanceService) {
        this.applicationInstanceService = applicationInstanceService;
        this.gson = new Gson();
    }

    @Override
    public LogReader process(LogReader logReader) {
        if (shouldSkipLog(logReader)) {
            return null;
        }

        LogInfo logInfo = parseLogContent(logReader.getAuditContent());
        String instanceId = applicationInstanceService.getCurrentApplicationInstanceByHttpRequestId(
                logInfo.getHttpRequestId()
        );

        throw new StopJobException(formatInstanceId(instanceId));
    }

    private boolean shouldSkipLog(LogReader logReader) {
        return logReader.getAuditContent() == null ||
                logReader.getAuditContent().equals(AuditConstant.AUDIT_IGNORE);
    }

    private LogInfo parseLogContent(String content) {
        return gson.fromJson(content, LogInfo.class);
    }

    private String formatInstanceId(String instanceId) {
        return APPLICATION_INSTANCE_ID_SEPARATOR + instanceId + APPLICATION_INSTANCE_ID_SEPARATOR;
    }
}