package com.sas.fees.service;

import com.sas.clients.ResponseDTO;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import com.sas.clients.users.UserClient;
import com.sas.clients.users.UserResponse;
import com.sas.exception.dto.GradeTermFeeNotFoundException;
import com.sas.fees.entity.GradeTermFee;
import com.sas.fees.entity.TermFeeBalance;
import com.sas.fees.entity.TermFeeBalanceId;
import com.sas.fees.repository.TermFeeBalanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TermFeeBalanceServiceTest {

    @Mock
    private TermFeeBalanceRepository termFeeBalanceRepository;

    @Mock
    private GradeTermFeeService gradeTermFeeService;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private TermFeeBalanceService termFeeBalanceService;

    @Test
    void testGetTermFeeBalanceById() {
        // Arrange
        String studId = "student1";
        int termId = 1;
        long yearId = 2024;
        TermFeeBalance termFeeBalance = new TermFeeBalance();
        termFeeBalance.setId(new TermFeeBalanceId(studId, yearId, termId));

        when(termFeeBalanceRepository.findById(any(TermFeeBalanceId.class))).thenReturn(Optional.of(termFeeBalance));
        when(termFeeBalanceRepository.findStudentTotalFeePaidByTermAndYear(anyString(), anyInt(), anyLong())).thenReturn(500.0);

        // Act
        Optional<TermFeeBalance> result = termFeeBalanceService.getTermFeeBalanceById(studId, termId, yearId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTotalFeePaid()).isEqualTo(500.0);
    }

    @Test
    void testCreateCurrentTermFeeStudentFeeRecords() {
        // Arrange
        AcademicYearTermResponse prevTerm = new AcademicYearTermResponse();
        prevTerm.setTermId(1);
        prevTerm.setYearId(2024L);
        AcademicYearTermResponse curTerm = new AcademicYearTermResponse();
        curTerm.setTermId(2);
        curTerm.setYearId(2024L);

        List<AcademicYearTermResponse> academicYearTerms = List.of(prevTerm, curTerm);
        UserResponse student = new UserResponse();
        student.setId("student1");
        student.setGradeId(1);

        ResponseDTO<List<UserResponse>> responseDTO = new ResponseDTO<>();
        responseDTO.setBody(Collections.singletonList(student));
        ResponseEntity<ResponseDTO<List<UserResponse>>> responseEntity = new ResponseEntity<>(responseDTO, HttpStatus.OK);

        when(userClient.getStudents()).thenReturn(responseEntity);
        when(termFeeBalanceRepository.findById(any(TermFeeBalanceId.class))).thenReturn(Optional.empty());
        when(gradeTermFeeService.getGradeTermFeeById(anyInt(), anyInt(), anyLong())).thenReturn(Optional.of(GradeTermFee.builder().fee(1000.0).build()));

        // Act
        termFeeBalanceService.createCurrentTermFeeStudentFeeRecords(academicYearTerms);

        // Assert
        ArgumentCaptor<TermFeeBalance> captor = ArgumentCaptor.forClass(TermFeeBalance.class);
        verify(termFeeBalanceRepository, times(1)).save(captor.capture());

        TermFeeBalance savedTermFeeBalance = captor.getValue();
        assertThat(savedTermFeeBalance.getId().getStudId()).isEqualTo("student1");
        assertThat(savedTermFeeBalance.getId().getTermId()).isEqualTo(2);
        assertThat(savedTermFeeBalance.getId().getYearId()).isEqualTo(2024);
        assertThat(savedTermFeeBalance.getArrears()).isEqualTo(1000.0); // assuming no previous fees were paid
    }

    @Test
    void testCreateCurrentTermFeeStudentFeeRecordsThrowsException() {
        // Arrange
        AcademicYearTermResponse prevTerm = new AcademicYearTermResponse();
        prevTerm.setTermId(1);
        prevTerm.setYearId(2024L);
        AcademicYearTermResponse curTerm = new AcademicYearTermResponse();
        curTerm.setTermId(2);
        curTerm.setYearId(2024L);

        List<AcademicYearTermResponse> academicYearTerms = List.of(prevTerm, curTerm);
        UserResponse student = new UserResponse();
        student.setId("student1");
        student.setGradeId(1);

        ResponseDTO<List<UserResponse>> responseDTO = new ResponseDTO<>();
        responseDTO.setBody(Collections.singletonList(student));
        ResponseEntity<ResponseDTO<List<UserResponse>>> responseEntity = new ResponseEntity<>(responseDTO, HttpStatus.OK);

        when(userClient.getStudents()).thenReturn(responseEntity);
        when(termFeeBalanceRepository.findById(any(TermFeeBalanceId.class))).thenReturn(Optional.empty());
        when(gradeTermFeeService.getGradeTermFeeById(anyInt(), anyInt(), anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> termFeeBalanceService.createCurrentTermFeeStudentFeeRecords(academicYearTerms))
                .isInstanceOf(GradeTermFeeNotFoundException.class)
                .hasMessageContaining("The fee for the year: null and term: null is missing");
    }
}
