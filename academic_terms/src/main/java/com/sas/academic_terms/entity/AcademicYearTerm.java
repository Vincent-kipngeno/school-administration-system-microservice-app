package com.sas.academic_terms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "a_year_term")
public class AcademicYearTerm {

    @EmbeddedId
    private AcademicYearTermId id;

    @ManyToOne
    @MapsId("yearId")
    private AcademicYear year;

    @ManyToOne
    @MapsId("termId")
    private AcademicTerm term;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "is_current")
    private Boolean isCurrent;
}
