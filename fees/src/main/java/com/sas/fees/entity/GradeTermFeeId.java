package com.sas.fees.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class GradeTermFeeId {
    @Column(name = "grade_id")
    private Integer gradeId;

    @Column(name = "year_id")
    private Long yearId;

    @Column(name = "term_id")
    private Integer termId;
}
