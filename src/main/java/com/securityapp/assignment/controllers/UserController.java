package com.securityapp.assignment.controllers;

import com.securityapp.assignment.dto.AuthResponseDto;
import com.securityapp.assignment.dto.ResponseData;
import com.securityapp.assignment.dto.SignInDto;
import com.securityapp.assignment.dto.SignupDto;
import com.securityapp.assignment.exceptions.NotFoundException;
import com.securityapp.assignment.exceptions.UserAuthException;
import com.securityapp.assignment.exceptions.UserExistsException;
import com.securityapp.assignment.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @PostMapping(path = "/sign-up")
    public ResponseEntity<AuthResponseDto> userSignUp(@RequestBody @Valid SignupDto signupDto, HttpServletRequest request) throws UserExistsException, NotFoundException {
        AuthResponseDto responseDto =  userService.userSignUp(signupDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(path = "/sign-in")
    public ResponseEntity<AuthResponseDto> userLogin(@RequestBody @Valid SignInDto signInDto) throws UserAuthException {
        return ResponseEntity.ok(userService.userSignIn(signInDto));
    }

    // TODO: admin endpoint
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData getAllUsers() throws UserAuthException {
        return userService.getAllUsers();
    }
}
