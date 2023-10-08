package com.securityapp.assignment.controllers;

import com.securityapp.assignment.dto.CommentDto;
import com.securityapp.assignment.dto.ResponseData;
import com.securityapp.assignment.exceptions.NotFoundException;
import com.securityapp.assignment.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<ResponseData> createComment(@RequestBody @Valid CommentDto commentDto, @RequestParam("requestId") UUID requestId) throws NotFoundException {
        return ResponseEntity.ok(commentService.createComment(requestId, commentDto));
    }
}
