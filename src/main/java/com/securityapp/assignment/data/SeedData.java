package com.securityapp.assignment.data;

import com.securityapp.assignment.entities.Role;
import com.securityapp.assignment.entities.User;
import com.securityapp.assignment.repositories.RoleRepository;
import com.securityapp.assignment.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedData implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        seedUserAndRoles();
    }

    public void seedUserAndRoles() {
        Role roleStudent = seedRole("STUDENT"); // id: 1
        Role roleFacilitator = seedRole("FACILITATOR"); // id: 2
        Role roleAdmin = seedRole("ADMIN"); // id: 3

        User user1 = seedUser(
                "s.ishimwegabin@gmail.com",
                "#Password123",
                "+250787857036",
                List.of(roleStudent)
        );
        User user2 = seedUser(
                "gabinishimwe02@gmail.com",
                "#Password123",
                "+250787857036",
                List.of(roleFacilitator)
        );
        User user3 = seedUser(
                "g.ishimwe@alustudent.com",
                "#Password123",
                "+250787857036",
                List.of(roleAdmin)
        );
    }

    public User seedUser(String email, String password, String contact, List<Role> roles) {
        return userRepository.save(
               User.builder()
                       .email(email)
                       .password(passwordEncoder.encode(password))
                       .contactNumber(contact)
                       .roles(roles)
                       .build()
        );
    }

    public Role seedRole(String name) {
        return roleRepository.save(
                Role.builder()
                        .name(name)
                        .build()
        );
    }
}
