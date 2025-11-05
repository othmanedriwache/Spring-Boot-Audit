package com.auditPersist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "application_instances")
public class ApplicationInstance {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    private LocalDateTime creationDate;

    @JoinColumn(name = "application_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnore
    private Application application;

    @OneToMany(mappedBy = "applicationInstance", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<HttpRequest> httpRequests;

    @Override
    public String toString() {
        return "ApplicationInstance{" +
                "id='" + id + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationInstance that = (ApplicationInstance) o;
        return Objects.equals(id, that.id) && Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate);
    }
}