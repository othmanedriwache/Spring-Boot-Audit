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
@Table(name = "http_requests_headers")
public class HttpRequestHeader {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    private String header;
    @Lob
    @Column(name="content")
    private String content;

    @JoinColumn(name = "http_headers_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnore
    private HttpRequest httpRequest;

    @Override
    public String toString() {
        return "HttpRequestParameter{" +
                "id='" + id + '\'' +
                ", header='" + header + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestHeader that = (HttpRequestHeader) o;
        return Objects.equals(id, that.id) && Objects.equals(header, that.header) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, header, content);
    }
}
