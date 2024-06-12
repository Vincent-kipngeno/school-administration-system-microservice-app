package com.sas.academic_terms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "academic_term")
public class AcademicTerm {

    @Id
    @SequenceGenerator(
            name = "term_id_sequence",
            sequenceName = "term_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "term_id_sequence"
    )
    private Long id;

    private String name;

    @OneToMany(mappedBy = "term")
    private List<AcademicYearTerm> terms;
}

