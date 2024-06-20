package com.sas.fees.rabbitmq;

import com.sas.clients.ResponseDTO;
import com.sas.clients.academic_terms.AcademicYearRequest;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import com.sas.fees.service.GradeTermFeeService;
import com.sas.fees.service.TermFeeBalanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeesConsumerTest {

    @Mock
    private TermFeeBalanceService termFeeBalanceService;

    @Mock
    private GradeTermFeeService gradeTermFeeService;

    @InjectMocks
    private FeesConsumer feesConsumer;

    @Test
    void testConsumer() {
        // Arrange
        AcademicYearTermResponse termResponse = new AcademicYearTermResponse();
        List<AcademicYearTermResponse> termResponseList = Collections.singletonList(termResponse);
        ResponseDTO<List<AcademicYearTermResponse>> responseDTO = new ResponseDTO<>();
        responseDTO.setBody(termResponseList);

        // Act
        feesConsumer.feesConsumer(responseDTO);

        // Assert
        verify(termFeeBalanceService, times(1)).createCurrentTermFeeStudentFeeRecords(termResponseList);
    }

    @Test
    void testConsumer2() {
        // Arrange
        AcademicYearRequest yearRequest = new AcademicYearRequest();
        ResponseDTO<AcademicYearRequest> responseDTO = new ResponseDTO<>();
        responseDTO.setBody(yearRequest);

        // Act
        feesConsumer.gradeTermFeesConsumer(responseDTO);

        // Assert
        verify(gradeTermFeeService, times(1)).saveGradeTermFees(yearRequest);
    }
}