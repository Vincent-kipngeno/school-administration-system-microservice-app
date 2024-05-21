package com.sas.academic_terms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public record AcademicYearRequest(List<AcademicYearTermRequest> terms){
}
