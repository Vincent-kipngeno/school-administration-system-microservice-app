package com.sas.clients.academic_terms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeTermFeeRequest {
    private int gradeId;
    private double fees;
}
