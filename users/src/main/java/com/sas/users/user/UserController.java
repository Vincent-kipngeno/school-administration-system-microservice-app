package com.sas.users.user;

import com.sas.clients.ResponseDTO;
import com.sas.clients.users.Role;
import com.sas.clients.users.UserResponse;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/students")
    public ResponseEntity<ResponseDTO<List<UserResponse>>> getStudentIds() {
        List<UserResponse> students = userService.getUsersByRole("STUDENT");
        ResponseDTO<List<UserResponse>> response = ResponseDTO.<List<UserResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message("List of students")
                .timestamp(System.currentTimeMillis())
                .body(students)
                .build();
        return ResponseEntity.ok(response);
    }
}

