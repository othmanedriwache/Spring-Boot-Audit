# Audit Persist Utility Service - Oracle

## Overview

The Audit Persist Utility Service is a Spring Boot application designed to persist and manage audit logs for applications. It provides a comprehensive solution for importing, storing, querying, and analyzing application audit trails, including HTTP requests, function calls, business requests, and their relationships.

## Features

- **Log File Import**: Import audit log files via REST API
- **Batch Processing**: Asynchronous processing of large log files using Spring Batch
- **Database Persistence**: Store audit data in Oracle database with full relational mapping
- **Advanced Search**: Dynamic search and filtering capabilities with custom criteria
- **Tree Structure**: Hierarchical representation of HTTP requests and function calls
- **Multi-Application Support**: Manage audit logs for multiple applications and versions
- **RESTful API**: Complete REST API for CRUD operations and queries

## Architecture

### Technology Stack

- **Framework**: Spring Boot
- **Database**: Oracle Database
- **ORM**: Spring Data JPA / Hibernate
- **Batch Processing**: Spring Batch
- **Build Tool**: Maven
- **Language**: Java

### Core Components

#### 1. Controllers
- `AuditController`: Handles audit log file imports
- `HttpRequestController`: Manages HTTP request queries and searches
- `ApplicationController`: Manages application lifecycle and cleanup operations

#### 2. Entities

The application models the following domain entities:

- **Application**: Represents an application with name and version
- **ApplicationInstance**: Specific instance of an application deployment
- **HttpRequest**: HTTP request audit data (URL, method, status, headers, parameters)
- **FunctionRequest**: Function/method call audit data
- **FunctionRequestArgument**: Arguments passed to functions
- **BusinessRequest**: Business logic request audit data
- **HttpRequestHeader**: HTTP request headers
- **HttpRequestParameter**: HTTP request parameters

#### 3. Batch Jobs

- **AuditJob**: Main batch job for processing audit log files
- **DatabaseAuditJob**: Handles database-specific audit operations
- **ApplicationInstanceStep**: Extracts application instance information
- **AuditStep**: Processes and persists audit data
- **OrganizerStep**: Organizes and links related audit records

#### 4. Services

Service layer follows a modular design with dedicated services for each entity:
- Application Service
- Application Instance Service
- HTTP Request Service
- Function Request Service
- Business Request Service
- Audit Persist Service

#### 5. Search Infrastructure

Advanced search capabilities with:
- `SpecificationBuilder`: Dynamic query builder
- `SearchCriteria`: Search criteria definition
- `SearchOperation`: Supported search operations (EQUALS, CONTAINS, GREATER_THAN, etc.)
- `SpecificationService`: Specification validation and creation

## API Endpoints

### Base URL
```
http://localhost:8081/common-services/v1
```

### Audit Management

#### Import Audit Log File
```
POST /audit/import
Parameters:
  - file: MultipartFile (required)
  - applicationName: String (required)
  - applicationVersion: String (required)
```

### HTTP Request Queries

#### Get HTTP Request by ID
```
GET /httpRequest/get?id={id}
```

#### Get HTTP Request as Tree Structure
```
GET /httpRequest/getAsTree?id={id}
```

#### Search HTTP Requests
```
POST /httpRequest/search?pageNum=0&pageSize=10
Body: SearchDto (JSON)
```

#### Get Searchable Fields
```
GET /httpRequest/getHttpRequestSearchableFields
```

### Application Management

#### Create Application
```
POST /application/create
Body: Application (JSON)
```

#### Clean Application Logs
```
DELETE /application/clean
Body: Application (JSON)
```

#### Clean by Application Instance ID
```
DELETE /application/cleanById/{ApplicationInstanceId}
```

#### Clean by Name and Version
```
DELETE /application/cleanByNameAndVersion/{ApplicationName}/{ApplicationVersion}
```

## Configuration

### Application Configuration (`application.yml`)

```yaml
spring:
  mvc:
    throw-exception-if-no-handler-found: true
  profiles:
    active: dev

server:
  port: 8081
  servlet:
    context-path: /common-services/v1

management:
  server:
    port: 9060
  endpoint:
    web:
      base-path: /actuator

logging:
  file:
    path: /comman-services/logs
```

### Environment-Specific Configuration

The application uses Spring profiles with a development profile (`application-dev.yml`) for environment-specific settings.

## Data Model

### Entity Relationships

```
Application (1) ──> (*) ApplicationInstance
                           │
                           │
                           ├─> (*) HttpRequest
                           │      │
                           │      ├─> (*) HttpRequestHeader
                           │      ├─> (*) HttpRequestParameter
                           │      ├─> (*) FunctionRequest
                           │      │      │
                           │      │      ├─> (*) FunctionRequestArgument
                           │      │      └─> (*) BusinessRequest
                           │      │
                           │      └─> (*) BusinessRequest
```

### Key Features

- **Self-referencing relationships**: HttpRequest and FunctionRequest support parent-child relationships
- **Exception tracking**: Special relationship to track exception-causing requests
- **Eager loading**: Related entities are loaded eagerly for complete data access
- **LOB support**: Large text fields (exceptions, return content) stored as LOBs

## Batch Processing

The application uses Spring Batch for processing large audit log files:

1. **Application Instance Identification**: Extracts or creates application instance
2. **Audit Data Processing**: Reads and parses log entries
3. **Data Persistence**: Persists audit records to database
4. **Organization**: Links related records (parent-child relationships)

### Processing Steps

1. Upload audit log file
2. Identify application instance from log
3. Chunk-based processing of log entries
4. Parse and validate each entry
5. Persist to database
6. Organize relationships between records

## Exception Handling

Custom exception handling framework:
- `CustomExceptionHandler`: Application-specific exception handling
- `SpringBootExceptionHandler`: Spring framework exception handling
- `StopJobExceptionHandler`: Batch job exception handling

Custom exceptions:
- `DeleteException`
- `EmptyResponseException`
- `HttpRequestException`
- `NoRecordException`
- `NoValidDataException`
- `NullParameterException`
- `ResponseException`
- `StopJobException`
- `UnableToCastException`

## Security

The application includes security configuration (`SecurityConfig.java`) for:
- Authentication and authorization
- Endpoint security
- CORS configuration (enabled for `/httpRequest` endpoints)

## Utilities

- **ApplicationInstanceManager**: Manages application instance lifecycle
- **ClassTypeValidator**: Validates class types for dynamic operations
- **StringUtil**: String manipulation utilities

## Search Capabilities

### Searchable Fields

The application supports dynamic searches with various operations:

- **Operations**: EQUALS, NOT_EQUALS, CONTAINS, NOT_CONTAINS, STARTS_WITH, ENDS_WITH, GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL, IN, NOT_IN
- **Field Types**: String, Number, Date, Boolean
- **Sorting**: Ascending/Descending on any field
- **Pagination**: Page number and page size parameters

### Search Request Example

```json
{
  "criteria": [
    {
      "field": "url",
      "operation": "CONTAINS",
      "value": "api/users"
    },
    {
      "field": "status",
      "operation": "EQUALS",
      "value": 200
    }
  ],
  "logicalOperator": "AND",
  "sort": {
    "field": "inputDate",
    "direction": "DESC"
  }
}
```

## Monitoring

### Actuator Endpoints

The application exposes Spring Boot Actuator endpoints on port 9060:
- Base path: `/actuator`
- Available metrics, health checks, and monitoring endpoints

## Logging

Application logs are stored at:
```
/comman-services/logs
```

## Project Structure

```
src/main/java/com/auditPersist/
├── batch/                      # Batch processing components
│   ├── job/                   # Batch job definitions
│   ├── step/                  # Batch step definitions
│   └── task/                  # Task processors, readers, writers
├── config/                    # Configuration classes
├── constant/                  # Application constants
├── controller/                # REST API controllers
├── entity/                    # JPA entities
├── exeptions/                 # Custom exceptions and handlers
├── generic/                   # Generic services and utilities
├── model/                     # Domain models and enums
├── repository/                # JPA repositories
├── response/                  # Response handling utilities
├── search/                    # Search and filtering infrastructure
├── service/                   # Business logic services
└── utils/                     # Utility classes
```

## Getting Started

### Prerequisites

- Java 8 or higher
- Oracle Database
- Maven 3.6+

### Configuration

1. Configure database connection in `application-dev.yml`
2. Set up Oracle database schema
3. Configure log file path if needed

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on:
- Main application: http://localhost:8081/common-services/v1
- Actuator: http://localhost:9060/actuator

## Development

### Building the Project

```bash
mvn clean install
```

### Running Tests

```bash
mvn test
```

## License

[Specify your license here]


## Support

For issues and questions, please https://www.linkedin.com/in/othmane-mohamed-mahmoud-271647182/
