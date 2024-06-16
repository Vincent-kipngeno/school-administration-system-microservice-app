package com.sas.fees.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TermFeeBalanceId implements Serializable {
    @Column(name = "stud_id")
    private String studId;

    @Column(name = "year_id")
    private Long yearId;

    @Column(name = "term_id")
    private Integer termId;
}
