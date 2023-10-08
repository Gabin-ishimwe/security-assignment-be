package com.securityapp.assignment.services;

import com.securityapp.assignment.dto.AuthResponseDto;
import com.securityapp.assignment.dto.ResponseData;
import com.securityapp.assignment.dto.SignInDto;
import com.securityapp.assignment.dto.SignupDto;
import com.securityapp.assignment.entities.Role;
import com.securityapp.assignment.entities.Token;
import com.securityapp.assignment.entities.TokenType;
import com.securityapp.assignment.entities.User;
import com.securityapp.assignment.exceptions.NotFoundException;
import com.securityapp.assignment.exceptions.UserAuthException;
import com.securityapp.assignment.exceptions.UserExistsException;
import com.securityapp.assignment.repositories.RoleRepository;
import com.securityapp.assignment.repositories.TokenRepository;
import com.securityapp.assignment.repositories.UserRepository;
import com.securityapp.assignment.utils.JwtUserDetailService;
import com.securityapp.assignment.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final JwtUtil jwtUtil;

    private final JwtUserDetailService jwtUserDetailService;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;


    @Transactional
    public AuthResponseDto userSignUp(SignupDto signupDto) throws UserExistsException, NotFoundException {
        Optional<User> findUser = userRepository.findByEmail(signupDto.getEmail());
        if(findUser.isPresent()) throw new UserExistsException("User already exists");
        List<Role> roles = new ArrayList<>();
        Role userRole = roleRepository.findByName("USER");
        if(userRole == null) {
            throw new NotFoundException("User not found");
        }
        roles.add(userRole);
        // create user in database
        User user = User.builder()
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .contactNumber(signupDto.getPhoneNumber())
                .roles(roles)
                .build();
        User createdUser = userRepository.save(user);

        return AuthResponseDto.builder()
                .message("Verify your email")
                .user(createdUser)
                .build();
    }

    public AuthResponseDto userSignIn(SignInDto signInDto) throws UserAuthException {
        String userEmail = signInDto.getEmail();
        String password = signInDto.getPassword();
        Optional<User> findUser = userRepository.findByEmail(userEmail);
        if(findUser.isEmpty()) throw new UserAuthException("Invalid Credential, Try again");

//        if(!findUser.get().isEnabled()) throw new UserAuthException("Account isn't verified");

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password));
        if(!authenticate.isAuthenticated()) throw new UsernameNotFoundException("Bad Credentials, Try again");
        revokeUserTokens(findUser.get());
        return createJwt("User logged in successfully", findUser.get());
    }

    public AuthResponseDto createJwt(String message, User user) {
        String token = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        Token saveToken = Token.builder()
                .user(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(saveToken);
        return AuthResponseDto.builder()
                .message(message)
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();

    }

    public void revokeUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllUserTokens(user.getId());
        if(validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthResponseDto createRefreshToken(HttpServletRequest request, HttpServletResponse response) throws UserAuthException {
        String header = request.getHeader("Authorization");
        String refreshToken = null;
        String userEmail = null;
        // check if header has keyword 'bearer' in it
        if(header == null || !header.startsWith("Bearer ")) {
            throw new UserAuthException("User not authenticated");
        }
        refreshToken = header.split(" ")[1];
        // extracting payload from token
        userEmail = jwtUtil.getUserNameFromToken(refreshToken);
        if(userEmail != null) {
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserAuthException("User not found"));
            System.out.println(jwtUtil.validateRefreshToken(refreshToken, user));
            if(!jwtUtil.validateRefreshToken(refreshToken, user)) {
                throw new UserAuthException("Invalid token");
            }
            String accessToken = jwtUtil.generateToken(user);
            revokeUserTokens(user);
            Token saveToken = Token.builder()
                    .user(user)
                    .token(accessToken)
                    .tokenType(TokenType.BEARER)
                    .revoked(false)
                    .expired(false)
                    .build();

            tokenRepository.save(saveToken);
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        return null;
    }

    public ResponseData getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return ResponseData.builder()
                .message("All users")
                .data(allUsers)
                .build();
    }
}
