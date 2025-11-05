package com.demo.service;

import com.auditWriter.annotations.AuditableClass;
import com.auditWriter.annotations.NotAuditableFunction;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@AuditableClass
public class DatabaseTestService {

    ApplicationContext applicationContext;

    DatabaseTestService self;

    public DatabaseTestService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        this.self = applicationContext.getBean(DatabaseTestService.class);
    }

    public boolean databaseAuditableFunction1(String token) throws IOException {
        self.serviceAuditableFunction2("2");
        return true;
    }


    public boolean serviceAuditableFunction2(String token) throws IOException {
        System.out.println(token);
        return true;
    }

    @NotAuditableFunction
    public boolean serviceNonAuditableFunction(String token) throws IOException, InterruptedException {
        System.out.println(token);
        Thread.sleep(500);
        return true;
    }



}
