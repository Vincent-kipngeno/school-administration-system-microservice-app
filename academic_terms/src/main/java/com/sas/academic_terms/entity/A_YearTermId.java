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
public class A_YearTermId implements Serializable {
    @Column(name = "year_id")
    private Integer yearId;

    @Column(name = "term_id")
    private Integer termId;
}
