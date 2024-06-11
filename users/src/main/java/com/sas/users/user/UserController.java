package com.sas.users.user;

import com.sas.clients.ResponseDTO;
import com.sas.clients.users.Role;
import com.sas.clients.users.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/students")
    public ResponseEntity<ResponseDTO> getStudentIds() {
        List<UserResponse> students = userService.getUsersByRole("STUDENT");
        ResponseDTO response = ResponseDTO.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message("List of students")
                .timestamp(LocalDateTime.now())
                .body(students)
                .build();
        return ResponseEntity.ok(response);
    }
}

