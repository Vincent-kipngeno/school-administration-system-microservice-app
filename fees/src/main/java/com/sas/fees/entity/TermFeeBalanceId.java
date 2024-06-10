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
    private int studId;

    @Column(name = "ay_term_id")
    private int ayTermId;
}
