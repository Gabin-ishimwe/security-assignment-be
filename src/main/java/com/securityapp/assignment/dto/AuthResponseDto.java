package com.securityapp.assignment.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.securityapp.assignment.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDto {
    private String message;

    private String accessToken;

    private String refreshToken;

    private User user;

    private List<User> users;
}
