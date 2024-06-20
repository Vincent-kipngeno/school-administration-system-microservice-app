package com.sas.academic_terms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "academic_year")
public class AcademicYear {

    @Id
    @SequenceGenerator(
            name = "year_id_sequence",
            sequenceName = "year_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "year_id_sequence"
    )
    private Long id;

    @Column(unique = true)
    private String year;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @OneToMany(mappedBy = "year", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcademicYearTerm> yearTerms;
}
