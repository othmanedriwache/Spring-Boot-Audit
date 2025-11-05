package com.auditWriter.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StringUtil {

    public static List<String> convertListOfObjectsToListOfStrings(Object[] objects) {
        List<String> strings = new ArrayList<>();
        String tempValue;
        for (Object object : objects) {
            tempValue = Objects.toString(object, null);
            if (tempValue != null)
                strings.add(tempValue);
        }
        return strings;
    }

}
