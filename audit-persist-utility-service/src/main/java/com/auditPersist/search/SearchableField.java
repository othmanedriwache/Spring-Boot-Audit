package com.auditPersist.search;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.auditPersist.constant.AuditConstant.*;

@Data
@NoArgsConstructor
public class SearchableField {


    public static final List<String> ValidFieldType = initFieldTypeValidations();

    public SearchableField(String key, String type) {
        this.key = key;
        this.type = type;
        this.regex =getRegexByType(type);
    }

    public SearchableField(String key, String type, String regex) {
        this.key = key;
        this.type = type;
        this.regex = regex;
    }

    private String key;
    private String type;
    private String regex;

    private static String getRegexByType(String type){
        switch (type){
            case INTEGER:
            case INT:
                return "^-?\\d+$";
            case LONG:  // Add this
                return "^-?\\d+$";
            case DOUBLE:
                return "^-?\\d+(\\.\\d+)?$";
            case BOOLEAN:
                return "^(true|false|TRUE|FALSE)$";
            case LOCAL_DATE_TIME:
                return "yyyy-MM-dd HH:mm:ss.SSS";
            default:
                return ".*";
        }
    }

    private static List<String> initFieldTypeValidations(){
        List<String> result = new ArrayList<>();
        result.add(INTEGER);
        result.add(INT);
        result.add(LONG);  // Add this
        result.add(STRING);
        result.add(LOCAL_DATE_TIME);
        result.add(DOUBLE);
        return result;
    }



}
