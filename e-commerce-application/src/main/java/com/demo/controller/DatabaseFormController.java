package com.demo.controller;

import com.auditWriter.annotations.AuditableClass;
import com.demo.service.DatabaseTest2Service;
import com.demo.service.DatabaseTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@AuditableClass
public class DatabaseFormController {

    @Autowired
    private DatabaseTestService databaseTestService;

    @Autowired
    private DatabaseTest2Service databaseTest2Service;

    @PostMapping("/submit")
    public String databaseSubmitForm(@RequestBody Map<String, String> payload) throws IOException, InterruptedException {
        databaseTestService.databaseAuditableFunction1("auditable class");
        databaseTestService.serviceNonAuditableFunction("non audit ");
        databaseTest2Service.AuditableFunction1("auditable function ");
        return "Form submitted successfully!";
    }


    @PostMapping("/test_exception")
    public String databaseTestException(@RequestBody Map<String, String> payload) throws Exception{
        String token = payload.get("token");
        databaseTest2Service.NotAuditableFunctionTestException(token);
        return "Form submitted successfully!";
    }
}
