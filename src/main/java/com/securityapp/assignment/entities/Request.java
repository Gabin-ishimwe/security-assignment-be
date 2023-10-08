package com.securityapp.assignment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "requests")
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "sender_email")
    private String senderEmail;
    @Column(name = "receiver_email")
    private String receiverEmail;
    private String message;
//    @OneToMany()
//    private List<Comment> comments;
}
