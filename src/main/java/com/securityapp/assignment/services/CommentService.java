package com.securityapp.assignment.services;

import com.securityapp.assignment.dto.CommentDto;
import com.securityapp.assignment.dto.ResponseData;
import com.securityapp.assignment.entities.Comment;
import com.securityapp.assignment.entities.Request;
import com.securityapp.assignment.exceptions.NotFoundException;
import com.securityapp.assignment.repositories.CommentRepository;
import com.securityapp.assignment.repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    public ResponseData createComment(UUID requestId, CommentDto commentDto) throws NotFoundException {
        Request findRequest = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));
        Comment newComment = Comment.builder()
                .message(commentDto.getMessage())
                .request(findRequest)
                .build();

        Comment savedComment = commentRepository.save(newComment);
        return new ResponseData("Comment created", savedComment);
    }
}
