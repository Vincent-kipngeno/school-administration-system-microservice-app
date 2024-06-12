package com.sas.clients.academic_terms;

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
public class AcademicYearResponse {
    private Long id;
    private String year;
    private Date startDate;
    private Date endDate;
    private List<AcademicYearTermResponse> yearTerms;
}
