package com.auditPersist.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class ClassTypeValidator {

    public static final Pattern IS_BOOLEAN_PATTERN = Pattern.compile("true", Pattern.CASE_INSENSITIVE);
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static Integer getStringAsInteger(String input) {
        return Integer.valueOf(input);
    }

    public static LocalDateTime getStringAsLocalDateTime(String input) {
        return LocalDateTime.parse(input, DATE_TIME_FORMATTER);
    }

    public static Double getStringAsDouble(String input) {
        return Double.valueOf(input);
    }

    public static boolean getStringAsBoolean(Object input) {
        return input != null && IS_BOOLEAN_PATTERN.matcher(input.toString()).matches();
    }

}