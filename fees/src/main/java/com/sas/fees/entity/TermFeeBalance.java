package com.sas.fees.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "term_fee_balance")
public class TermFeeBalance {

    @EmbeddedId
    private TermFeeBalanceId id;

    private double arrears;

    @Transient
    private double totalFeePaid;
}
