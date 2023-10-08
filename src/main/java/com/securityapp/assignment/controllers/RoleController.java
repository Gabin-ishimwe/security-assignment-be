package com.securityapp.assignment.controllers;

import com.securityapp.assignment.dto.ResponseData;
import com.securityapp.assignment.exceptions.NotFoundException;
import com.securityapp.assignment.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseData assignRole(@RequestParam("userId") @Valid @org.hibernate.validator.constraints.UUID UUID userId, @RequestParam("roleId") Long roleId) throws NotFoundException {
        return roleService.addRolePermission(userId, roleId);
    }

    @PutMapping
    public ResponseData removeRole(@RequestParam("userId") @Valid @org.hibernate.validator.constraints.UUID UUID userId, @RequestParam("roleId") Long roleId) throws NotFoundException {
        return roleService.removeRolePermission(userId, roleId);
    }

    @GetMapping
    public ResponseData getRoles() {
        return roleService.getRoles();
    }
}
