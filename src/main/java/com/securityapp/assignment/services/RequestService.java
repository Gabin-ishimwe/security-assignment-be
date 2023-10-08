package com.securityapp.assignment.services;

import com.securityapp.assignment.dto.RequestDto;
import com.securityapp.assignment.dto.ResponseData;
import com.securityapp.assignment.entities.Request;
import com.securityapp.assignment.entities.User;
import com.securityapp.assignment.exceptions.NotFoundException;
import com.securityapp.assignment.repositories.RequestRepository;
import com.securityapp.assignment.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public ResponseData sendRequest(RequestDto requestDto) throws NotFoundException {
        User findReceiver = userRepository.findByEmail(requestDto.getReceiverEmail()).orElseThrow(() -> new NotFoundException("Receiver not found"));
        User findSender = userRepository.findByEmail(requestDto.getSenderEmail()).orElseThrow(() -> new NotFoundException("Sender not found"));
        Request newRequest = Request.builder()
                .senderEmail(requestDto.getSenderEmail())
                .receiverEmail(requestDto.getReceiverEmail())
                .message(requestDto.getMessage())
                .build();
        Request savedRequest = requestRepository.save(newRequest);
        return new ResponseData("Request sent", savedRequest);
    }
}
