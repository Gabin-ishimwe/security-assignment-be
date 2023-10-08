package com.securityapp.assignment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestDto {
    @NotBlank(message = "Sender email is required")
    @Email(message = "Sender email is invalid")
    private String senderEmail;

    @NotBlank(message = "Receiver email is required")
    @Email(message = "Receiver email is invalid")
    private String receiverEmail;

    @NotBlank(message = "Request message is required")
    private String message;
}
