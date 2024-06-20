package com.sas.users.user;

import com.sas.clients.users.Role;
import com.sas.clients.users.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getUsersByRole(String role) {
        return userRepository.findByRoleContaining(role)
                .stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .gradeId(user.getGradeId())
                        .build())
                .collect(Collectors.toList());
    }
}
