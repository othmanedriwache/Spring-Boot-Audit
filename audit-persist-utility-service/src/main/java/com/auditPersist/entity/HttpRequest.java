package com.auditPersist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "http_requests")
public class HttpRequest{

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    private String url;

    private String method;

    private String host;

    private int status;

    private String inputLine;

    private LocalDateTime inputDate;
    @Lob
    @Column(name = "exceptionInput")
    private String exceptionInput;

    private String outputLine;
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss.SSSz")
    private LocalDateTime outputDate;
    @Lob
    @Column(name = "exceptionOutput")
    private String exceptionOutput;

    private Integer duration;
    @Column(name = "logPath")
    private  String logPath;

    @OneToMany(mappedBy = "httpRequest", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<HttpRequestParameter> parameters;

    @OneToMany(mappedBy = "httpRequest", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<HttpRequestHeader> headers;

    @OneToMany(mappedBy = "httpRequest", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<FunctionRequest> functionRequests;

    @OneToMany(mappedBy = "httpRequest", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<BusinessRequest> businessRequests;

    @OneToOne
    @JoinColumn(name = "http_requests_id", nullable = true)
    @JsonIgnore
    private HttpRequest parent;

    @JoinColumn(name = "application_instances_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnore
    private ApplicationInstance applicationInstance;

    public void addFunctionRequest(FunctionRequest functionRequest){
        if(this.functionRequests == null)
            this.functionRequests = new ArrayList<>();
        this.functionRequests.add(functionRequest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HttpRequest that = (HttpRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(url, that.url) && Objects.equals(method, that.method) && Objects.equals(host, that.host) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, url, method, host, status);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", host='" + host + '\'' +
                ", status=" + status +
                ", inputLine='" + inputLine + '\'' +
                ", inputDate=" + inputDate +
                ", exceptionInput='" + exceptionInput + '\'' +
                ", outputLine='" + outputLine + '\'' +
                ", outputDate=" + outputDate +
                ", exceptionOutput='" + exceptionOutput + '\'' +
                ", duration=" + duration +
                ", logPath='" + logPath + '\'' +
                '}';
    }
}