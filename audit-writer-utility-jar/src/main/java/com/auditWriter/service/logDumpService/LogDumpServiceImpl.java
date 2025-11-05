package com.auditWriter.service.logDumpService;

import com.auditWriter.entity.LogDump;
import com.auditWriter.repository.LogDumpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LogDumpServiceImpl implements LogDumpService{

    @Autowired
    private LogDumpRepository logDumpRepository;

    @Override
    public List<LogDump> getNotPersistedApplicationInstance() {
        List<Object[]> distinctValues = logDumpRepository.findDistinctApplicationDetails();
        List<LogDump> result = new ArrayList<>();
        for (Object[] values : distinctValues) {
            result.add(LogDump.builder()
                    .applicationInstance((String) values[0])
                    .applicationName((String) values[1])
                    .applicationVersion((String) values[2])
                    .build());
        }
        return result;
    }

    @Override
    public void saveLogDump(List<LogDump> logDump) {
        if (ObjectUtils.isEmpty(logDump))
            return;
        logDumpRepository.saveAll(logDump);
    }
}
