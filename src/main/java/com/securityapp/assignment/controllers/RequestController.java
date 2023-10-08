package com.securityapp.assignment.controllers;

import com.securityapp.assignment.dto.RequestDto;
import com.securityapp.assignment.dto.ResponseData;
import com.securityapp.assignment.exceptions.NotFoundException;
import com.securityapp.assignment.services.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping()
    public ResponseEntity<ResponseData> sendRequest(@RequestBody @Valid RequestDto requestDto) throws NotFoundException {
        return ResponseEntity.ok(requestService.sendRequest(requestDto));
    }
}
