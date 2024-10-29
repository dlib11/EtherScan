package it.librone.okipo.task.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "ADDRESS")
@Data
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="address", unique = true, nullable = false)
    private String ethAddress;

    @Column(name="balance")
    private Double balance=0.0;

    @Column(name="createdAt")
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;

    @Column(name="lastUpdatedAt")
    //@UpdateTimestamp(source = SourceType.DB)
    private Instant lastUpdatedAt;

    @OneToMany(mappedBy = "address")
    private List<Transaction> transactions;
}
