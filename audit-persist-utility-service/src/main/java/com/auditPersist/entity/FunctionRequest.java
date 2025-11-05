package com.auditPersist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.auditPersist.model.FunctionType;
import com.auditPersist.model.ParentTreeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "function_requests")
public class FunctionRequest{

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    private FunctionType type;
    @Column(name = "path")
    private String path;
    @Column(name = "packagePath")
    private String packagePath;
    @Column(name = "returnType")
    private String returnType;
    @Column(name = "functionName")
    private String functionName;
    @Lob
    @Column(name = "returnContent")
    private String returnContent;
    @Column(name = "inputLine")
    private String inputLine;

    private LocalDateTime inputDate;
    @Lob
    @Column(name = "exceptionInput")
    private String exceptionInput;
    @Column(name = "outputLine")
    private String outputLine;
    @Column(name = "outputDate")
    private LocalDateTime outputDate;
    @Column(name = "exceptionOutput")
    private String exceptionOutput;
    @Column(name = "duration")
    private Integer duration;

    @Enumerated(EnumType.STRING)
    private ParentTreeStatus parentTreeStatus;

    @OneToOne
    @JoinColumn(name = "exception_function_requests_id", nullable = true)
    @JsonIgnore
    private FunctionRequest exception;

    @OneToOne
    @JoinColumn(name = "parent_function_requests_id", nullable = true)
    @JsonIgnore
    private FunctionRequest parent;

    @OneToMany(mappedBy = "functionRequest", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<FunctionRequestArgument> arguments;

    @JoinColumn(name = "http_requests_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JsonIgnore
    private HttpRequest httpRequest;

    @OneToMany(mappedBy = "functionRequest", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<BusinessRequest> businessRequests;

    @Override
    public String toString() {
        return "FunctionRequest{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", packagePath='" + packagePath + '\'' +
                ", returnType='" + returnType + '\'' +
                ", functionName='" + functionName + '\'' +
                ", returnContent='" + returnContent + '\'' +
                ", arguments=" + arguments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FunctionRequest that = (FunctionRequest) o;
        return Objects.equals(id, that.id) && type == that.type && Objects.equals(path, that.path) && Objects.equals(packagePath, that.packagePath) && Objects.equals(returnType, that.returnType) && Objects.equals(functionName, that.functionName) && Objects.equals(returnContent, that.returnContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, type, path, packagePath, returnType, functionName, returnContent);
    }
}