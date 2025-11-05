package com.auditPersist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.auditPersist","com.auditWriter"})
@EnableJpaRepositories(basePackages = {"com.auditPersist","com.auditWriter"})
@EntityScan(basePackages = {"com.auditPersist","com.auditWriter"})
public class AuditPersistApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditPersistApplication.class, args);
	}
}