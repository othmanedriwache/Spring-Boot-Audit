package com.auditPersist.search;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CriteriaInvalid {

    public CriteriaInvalid(String key, String errorDiscretion) {
        this.key = key;
        this.errorDiscretion = errorDiscretion;
    }

    public CriteriaInvalid(String key, String errorDiscretion, List<String>  allowedOptions) {
        this.key = key;
        this.errorDiscretion = errorDiscretion;
        this.allowedOptions = allowedOptions;
    }

    private String key;
    private String errorDiscretion;
    private List<String> allowedOptions;
}
