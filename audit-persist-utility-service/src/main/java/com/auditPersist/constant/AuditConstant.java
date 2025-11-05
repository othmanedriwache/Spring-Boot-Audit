package com.auditPersist.constant;

public class AuditConstant {

    /**
     * THIS ARGUMENT NEED TO CHANGE FOR EVERY NEW APPLICATION
     * ----------------------------- END ------------------------------------
     */

    public static final String AUDIT_KEY_SEPARATOR = "AUDIT_SEPARATOR";
    public static final String AUDIT_IGNORE = "AUDIT_IGNORE";
    public static final String LOG_TYPE = "INFO";
    public static final String LOG_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String NO_AUDITABLE_CLASS_IN_EXCEPTION_HANDLER = "there is no @AuditableClass or @AuditableFunction in the main application exception handler";
    public static final String APPLICATION_INSTANCE_ID_SEPARATOR = "APPLICATION_INSTANCE_ID_SEPARATOR";



    public static final String INTEGER = "Integer";
    public static final String INT = "int";
    public static final String STRING = "String";
    public static final String BOOLEAN = "boolean";
    public static final String LOCAL_DATE_TIME = "LocalDateTime";
    public static final String DOUBLE = "Double";
    public static final String TABLE = "Table";
    public static final String LONG = "Long";
    public static final String NOT_SEARCHABLE_KEY = "Not searchable key";
    public static final String OPERATION_TYPE_NOT_EXIST = "Operation Type Not Exist";
    public static final String OPERATION_NOT_ALLOWED_WITH_THIS_FIELD_TYPE = "Operation not allowed With This Field Type";
    public static final String THE_VALUE_NO_COMPATIBLE_WITH_SEARCHABLE_TYPE = "the value no compatible with searchable type";
}
