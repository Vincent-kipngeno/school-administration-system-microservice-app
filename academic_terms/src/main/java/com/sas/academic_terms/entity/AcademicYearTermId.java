package com.sas.academic_terms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AcademicYearTermId implements Serializable {
    @Column(name = "year_id")
    private Long yearId;

    @Column(name = "term_id")
    private Integer termId;
}
