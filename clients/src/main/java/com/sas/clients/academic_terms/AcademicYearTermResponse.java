package com.sas.clients.academic_terms;

import com.sas.clients.users.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademicYearTermResponse {
    private Long yearId;
    private Integer termId;
    private String year;
    private String term;
    private Date startDate;
    private Date endDate;
    private Boolean isCurrent;
}