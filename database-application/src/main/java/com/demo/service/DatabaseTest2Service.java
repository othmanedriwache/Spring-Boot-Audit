package com.demo.service;

import com.auditWriter.annotations.AuditableFunction;
import com.auditWriter.annotations.NotAuditableFunction;
import com.auditWriter.service.auditWriterService.AuditWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DatabaseTest2Service {

    @Autowired
    AuditWriterService auditWriterService;


    @AuditableFunction
    public boolean AuditableFunction1(String token) throws IOException {
        System.out.println(token);
        NotAuditableFunction2(token);
        return true;

    }

    @NotAuditableFunction
    public boolean NotAuditableFunction2(String token) throws IOException {
        auditWriterService.logBusinessInfo("THIS IS TEST FOR BUISNISE serviceAuditarrrrleFunction");
        System.out.println(token);
        return true;

    }

   @NotAuditableFunction
    public boolean NotAuditableFunctionTestException(String token) throws Exception {
        Thread.sleep(200);
        auditWriterService.logBusinessError("THIS IS TEST FOR ERROR serviceAuditarrrrleFunction");
        int a = 0/0;
        System.out.println(token);
        return true;

    }

}
