package com.biotech.vitalsenseapi.auth.mapper;

import com.biotech.vitalsenseapi.auth.dto.UserResponse;
import com.biotech.vitalsenseapi.auth.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public UserResponse toUserResponse(User user, Long profileId) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .profileId(profileId)
                .build();
    }
}
