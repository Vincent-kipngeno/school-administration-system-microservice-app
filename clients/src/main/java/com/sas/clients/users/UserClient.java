package com.sas.clients.users;

import com.sas.clients.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "users",
        url = "${clients.users.url}"
)
public interface UserClient {

    @GetMapping(path = "api/v1/users/students")
    ResponseEntity<ResponseDTO<List<UserResponse>>> getStudents();
}