package com.auditPersist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "http_requests_parameters")
public class HttpRequestParameter {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @Column(name = "parameter")
    private String parameter;
    @Lob
    @Column(name="content")
    private String content;

    @JoinColumn(name = "http_requests_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnore
    private HttpRequest httpRequest;

    @Override
    public String toString() {
        return "HttpRequestParameter{" +
                "id='" + id + '\'' +
                ", parameter='" + parameter + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestParameter that = (HttpRequestParameter) o;
        return Objects.equals(id, that.id) && Objects.equals(parameter, that.parameter) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parameter, content);
    }
}
