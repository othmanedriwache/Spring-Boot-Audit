
package com.auditPersist.batch.task;

import com.auditPersist.constant.AuditConstant;
import com.auditPersist.entity.ApplicationInstance;
import com.auditPersist.model.LogReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.auditPersist.constant.AuditConstant.LOG_DATE_PATTERN;

@Service
public class LineMapperInfo {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(LOG_DATE_PATTERN);

    public LineMapper<LogReader> lineMapper(ApplicationInstance applicationInstance) {
        return (line, lineNum) -> {
            LogReader logReader = new LogReader();
            logReader.setAuditLine(String.valueOf(lineNum));

            if (!parseLine(line, logReader, applicationInstance)) {
                logReader.setAuditContent(AuditConstant.AUDIT_IGNORE);
            }

            return logReader;
        };
    }

    private boolean parseLine(String line, LogReader logReader, ApplicationInstance applicationInstance) {
        String[] logParts = line.split(AuditConstant.AUDIT_KEY_SEPARATOR);

        if (logParts.length < 2) {
            return false;
        }

        logReader.setAuditContent(logParts[1]);

        String[] infoParts = line.split(AuditConstant.LOG_TYPE);
        if (infoParts.length < 2) {
            return false;
        }

        return parseMetadata(infoParts[0], logReader, applicationInstance);
    }

    private boolean parseMetadata(String metadata, LogReader logReader, ApplicationInstance applicationInstance) {
        try {
            LocalDateTime timestamp = extractTimestamp(metadata);
            logReader.setAuditTime(timestamp);
            logReader.setAuditLogFilePath(null);
            logReader.setApplicationInstance(applicationInstance);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private LocalDateTime extractTimestamp(String timeString) {
        String cleaned = timeString.trim();
        String[] bracketSplit = cleaned.split("\\[");

        if (bracketSplit.length < 2) {
            throw new DateTimeParseException("Invalid timestamp format", timeString, 0);
        }

        String[] closeBracketSplit = bracketSplit[1].trim().split("\\]");
        String timestampValue = closeBracketSplit[0].trim();

        return LocalDateTime.parse(timestampValue, DATE_FORMATTER);
    }
}