package com.sas.clients.academic_terms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademicYearRequest{
    private Long yearId;
    private List<AcademicYearTermRequest> terms;
}
