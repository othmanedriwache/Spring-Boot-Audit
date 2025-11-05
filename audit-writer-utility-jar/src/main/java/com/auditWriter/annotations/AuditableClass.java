package com.auditWriter.annotations;

import com.auditWriter.model.FunctionType;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface AuditableClass {
    FunctionType value() default FunctionType.COMPONENT;
}