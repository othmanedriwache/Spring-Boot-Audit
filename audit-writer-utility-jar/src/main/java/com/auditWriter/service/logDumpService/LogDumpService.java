package com.auditWriter.service.logDumpService;

import com.auditWriter.entity.LogDump;

import java.util.List;

public interface LogDumpService {

    List<LogDump> getNotPersistedApplicationInstance();

    void saveLogDump(List<LogDump> logDump);
}
