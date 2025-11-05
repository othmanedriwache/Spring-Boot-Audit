package com.auditPersist.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StringUtil {

    public static List<String> convertListOfObjectsToListOfStrings(Object[] objects){
        List<String> strings = new ArrayList<>();
        String tempValue ;
        for (Object object : objects) {
            tempValue = Objects.toString(object, null);
            if(tempValue != null)
                 strings.add(tempValue);
        }
        return strings;
    }

    public static String convertListOfStringToOneStringWithDeliminator(List<String> list ,String deliminator){
        String result = "";
        if(!CollectionUtils.isEmpty(list))
            result = String.join(deliminator, list);
        return result;
    }

    public  static String convertListOfObjectsToOneStringWithDeliminator(Object[] objects ,String deliminator){
        List<String> list = convertListOfObjectsToListOfStrings(objects);
        return convertListOfStringToOneStringWithDeliminator(list,deliminator);
    }

    public static String getFunctionName(String str){
        return saveUntilFirstChar(str,'(');
    }

    public static String getFunctionContent(String str){
        str = removeUntilFirstChar(str,'(');
        str = removeLastChar(str);
        return str;
    }

    public static String saveUntilFirstChar(String str, char character){
        return str.substring(0,str.indexOf(character) +1);
    }

    public static String removeUntilFirstChar(String str, char character){
        return str.substring(str.indexOf(character) +1);
    }
    public static String removeLastChar(String str){
        return str.substring(0, str.length() - 1);
    }
}
