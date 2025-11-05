package com.auditPersist.utils;

import com.auditPersist.entity.ApplicationInstance;

import java.util.HashMap;
import java.util.Map;

public class ApplicationInstanceManager {

    public static Map<String, ApplicationInstance> applicationInstanceMap = new HashMap<>();

    public static void addApplicationInstance(String key, ApplicationInstance applicationInstance) {
        applicationInstanceMap.put(key, applicationInstance);
    }

    public static ApplicationInstance getApplicationInstance(String key) {
        return applicationInstanceMap.get(key);
    }
}
