package com.sas.academic_terms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public record AcademicYearTermRequest(
        Integer termId,
        Date startDate,
        Date endDate){
}
