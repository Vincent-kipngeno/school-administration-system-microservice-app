package com.sas.fees.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "grade_term_fee")
public class GradeTermFee {

    @EmbeddedId
    private GradeTermFeeId id;

    private double fee;

}
