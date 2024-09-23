package com.sas.academic_terms.controller;

import com.sas.academic_terms.controller.AcademicYearController;
import com.sas.academic_terms.service.AcademicTermService;
import com.sas.academic_terms.service.AcademicYearService;
import com.sas.academic_terms.service.AcademicYearTermService;
import com.sas.clients.ResponseDTO;
import com.sas.clients.academic_terms.AcademicTermResponse;
import com.sas.clients.academic_terms.AcademicYearRequest;
import com.sas.clients.academic_terms.AcademicYearResponse;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import com.sas.exception.dto.GlobalErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalErrorCode.class)  // Import the configuration class
public class AcademicYearControllerTest {

    @MockBean
    private AcademicYearService academicYearService;

    @MockBean
    private AcademicYearTermService academicYearTermService;

    @MockBean
    private AcademicTermService academicTermService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAcademicYear() throws Exception {
        AcademicYearRequest request = new AcademicYearRequest();
        when(academicYearService.addAcademicYear(any(AcademicYearRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/academic-years")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"yearId\": 2024, \"terms\": []}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":200,\"status\":\"OK\",\"message\":\"Academic year added with ID: 1\",\"timestamp\":null,\"body\":1}"));
    }

    @Test
    void testGetCurrentAcademicYearTerm() throws Exception {
        AcademicYearTermResponse response = new AcademicYearTermResponse();
        when(academicYearTermService.getCurrentAcademicYearTerm()).thenReturn(response);

        mockMvc.perform(get("/academic-years/currentA_YearTerm"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":200,\"status\":\"OK\",\"message\":\"Current academic year term returned successfully\",\"timestamp\":null,\"body\":{}}"));
    }

    @Test
    void testGetAllAcademicYears() throws Exception {
        List<AcademicYearResponse> responses = Collections.singletonList(new AcademicYearResponse());
        when(academicYearService.getAllAcademicYears()).thenReturn(responses);

        mockMvc.perform(get("/academic-years/academicYears"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":200,\"status\":\"OK\",\"message\":\"List of academic years.\",\"timestamp\":null,\"body\":[{}]}"));
    }

    @Test
    void testGetAllAcademicYearTerms() throws Exception {
        List<AcademicYearTermResponse> responses = Collections.singletonList(new AcademicYearTermResponse());
        when(academicYearTermService.getAllAcademicYearTerms()).thenReturn(responses);

        mockMvc.perform(get("/academic-years/a_YearTerms"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":200,\"status\":\"OK\",\"message\":\"Academic year terms returned successfully\",\"timestamp\":null,\"body\":[{}]}"));
    }

    @Test
    void testGetAcademicYearTermById() throws Exception {
        AcademicYearTermResponse response = new AcademicYearTermResponse();
        when(academicYearTermService.getAcademicYearTermById(anyLong(), anyInt())).thenReturn(response);

        mockMvc.perform(get("/academic-years/a_YearTerm/2024/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":200,\"status\":\"OK\",\"message\":\"Academic year term returned successfully.\",\"timestamp\":null,\"body\":{}}"));
    }

    @Test
    void testGetAcademicYearById() throws Exception {
        AcademicYearResponse response = new AcademicYearResponse();
        when(academicYearService.getAcademicYearById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/academic-years/academicYear/2024"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":200,\"status\":\"OK\",\"message\":\"Academic year terms returned successfully\",\"timestamp\":null,\"body\":{}}"));
    }

    @Test
    void testGetAcademicTermById() throws Exception {
        AcademicTermResponse response = new AcademicTermResponse();
        when(academicTermService.getAcademicTermById(anyInt())).thenReturn(response);

        mockMvc.perform(get("/academic-years/academicTerm/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":200,\"status\":\"OK\",\"message\":\"Academic term returned successfully\",\"timestamp\":null,\"body\":{}}"));
    }
}