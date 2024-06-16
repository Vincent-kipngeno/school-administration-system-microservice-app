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
public class AcademicYearTermRequest{
    private Integer termId;
    private Date startDate;
    private Date endDate;
    private List<GradeTermFeeRequest> gradeTermFees;
}
