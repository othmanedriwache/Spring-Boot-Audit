package com.auditPersist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.auditPersist.model.BusinessRequestType;
import com.auditPersist.model.ParentTreeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "business_requests")
public class BusinessRequest {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    private BusinessRequestType type;
    @Lob
    @Column(name = "content")
    private String content;
    @Lob
    @Column(name = "exception")
    private String exception;

    @Column(name = "inputLine")
    private String inputLine;

    private LocalDateTime inputDate;

    @JoinColumn(name = "http_requests_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JsonIgnore
    private HttpRequest httpRequest;

    @Enumerated(EnumType.STRING)
    private ParentTreeStatus parentTreeStatus;

    @JoinColumn(name = "function_requests_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JsonIgnore
    private FunctionRequest functionRequest;

    @Override
    public String toString() {
        return "BusinessRequest{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", exception='" + exception + '\'' +
                ", inputLine='" + inputLine + '\'' +
                ", inputDate=" + inputDate +
                '}';
    }
}