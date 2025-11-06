package com.auditPersist.search;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.*;

import static com.auditPersist.constant.AuditConstant.*;

public enum SearchOperation {
    @Enumerated(EnumType.STRING)
    CONTAINS, DOES_NOT_CONTAIN, EQUAL, NOT_EQUAL, BEGINS_WITH, DOES_NOT_BEGIN_WITH, ENDS_WITH,
    DOES_NOT_END_WITH, NUL, NOT_NULL, GREATER_THAN, GREATER_THAN_EQUAL, LESS_THAN, LESS_THAN_EQUAL,
    ANY, ALL, JOIN_TABLE, APPLICATION_NAME, APPLICATION_VERSION;

    public static final Map<String, List<String>> SIMPLE_OPERATION_SET = initOperationMapper();

    public static SearchOperation getDataOption(final String dataOption){
        if (dataOption == null) {
            return ALL;
        }
        switch(dataOption.toLowerCase()){
            case "all": return ALL;
            case "any": return ANY;
            default: return ALL;
        }
    }

    public static SearchOperation getSimpleOperation(final String input) {
        switch (input){
            case "CONTAINS": return CONTAINS;
            case "DOES_NOT_CONTAIN": return DOES_NOT_CONTAIN;
            case "EQUAL": return EQUAL;
            case "NOT_EQUAL": return NOT_EQUAL;
            case "BEGINS_WITH": return BEGINS_WITH;
            case "DOES_NOT_BEGIN_WITH": return DOES_NOT_BEGIN_WITH;
            case "ENDS_WITH": return ENDS_WITH;
            case "DOES_NOT_END_WITH": return DOES_NOT_END_WITH;
            case "NUL": return NUL;
            case "NOT_NULL": return NOT_NULL;
            case "GREATER_THAN": return GREATER_THAN;
            case "GREATER_THAN_EQUAL": return GREATER_THAN_EQUAL;
            case "LESS_THAN": return LESS_THAN;
            case "LESS_THAN_EQUAL": return LESS_THAN_EQUAL;
            case "JOIN_TABLE": return JOIN_TABLE;
            case "APPLICATION_NAME": return APPLICATION_NAME;
            case "APPLICATION_VERSION": return APPLICATION_VERSION;
            default: return null;
        }
    }

    public static Map<String, List<String>> initOperationMapper(){
        List<String> operations;
        Map<String, List<String>> operationsByType = new HashMap<>();

        operations = Arrays.asList("EQUAL","NOT_EQUAL","GREATER_THAN","GREATER_THAN_EQUAL","LESS_THAN","LESS_THAN_EQUAL");
        operationsByType.put(INTEGER, operations);
        operationsByType.put(INT, operations);
        operationsByType.put(DOUBLE, operations);

        operations = Arrays.asList("CONTAINS","DOES_NOT_CONTAIN", "EQUAL","NOT_EQUAL","BEGINS_WITH","DOES_NOT_BEGIN_WITH","ENDS_WITH","DOES_NOT_END_WITH");
        operationsByType.put(STRING, operations);

        operations = Arrays.asList("EQUAL","NOT_EQUAL","GREATER_THAN","GREATER_THAN_EQUAL","LESS_THAN","LESS_THAN_EQUAL");
        operationsByType.put(LOCAL_DATE_TIME, operations);

        operations = Collections.singletonList("JOIN_TABLE");
        operationsByType.put(TABLE, operations);

        operations = Collections.singletonList("APPLICATION_NAME");
        operationsByType.put("APPLICATION_NAME_TYPE", operations);

        operations = Collections.singletonList("APPLICATION_VERSION");
        operationsByType.put("APPLICATION_VERSION_TYPE", operations);

        return operationsByType;
    }

    public static Map<String, List<String>> getSimpleOperationSet() {
        return SIMPLE_OPERATION_SET;
    }
}