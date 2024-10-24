package it.librone.okipo.task.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "TRANSACTION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @Column(name="transactionHash")
    private String transactionHash;

    @Column(name="blockNumber", unique = false, nullable = false)
    private Long blockNumber;

    @Column(name="timeStamp", nullable = false)
    private Long timeStamp;

    @Column(name="toAddress", nullable = false)
    private String to;

    @Column(name="fromAddress", nullable = false)
    private String from;

    @Column(name="value", nullable = false)
    private Long value;

    @Column(name="gas", nullable = false)
    private Long gas;

    @ManyToOne
    @JoinColumn(name = "address", referencedColumnName = "id")
    private Address address;

}
