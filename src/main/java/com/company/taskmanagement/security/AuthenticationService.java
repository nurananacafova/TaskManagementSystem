package com.company.taskmanagement.security;

import com.company.taskmanagement.enums.Role;
import com.company.taskmanagement.enums.StatusType;
import com.company.taskmanagement.exception.DataNotFoundException;
import com.company.taskmanagement.exception.UserAlreadyExistsException;
import com.company.taskmanagement.model.Organization;
import com.company.taskmanagement.model.User;
import com.company.taskmanagement.repository.OrganizationRepository;
import com.company.taskmanagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, OrganizationRepository organizationRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.organizationRepository = organizationRepository;
    }

    public AuthenticationResponse register(RegisterRequest request) {
//        List<Organization> organizations = new ArrayList<>();
        User user = new User();
        if (request.getRole().equals(Role.USER)) {
            if (request.getOrgId() == 0) {
                throw new DataNotFoundException("User must to enter the Organization ID!");
            }
            Organization organization = organizationRepository.findById(request.getOrgId())
                    .orElseThrow(() ->
                            new DataNotFoundException("Cannot find Organization with ID: " + request.getOrgId()));
            if (organization.getStatus().equals(StatusType.DELETED)) {
                throw new DataNotFoundException("Cannot find Organization with ID: " + request.getOrgId());
            }
//            organizations.add(organization);
            user.setOrganizationId(Collections.singletonList(organization));
        }


        if (emailExists(request.getEmail()))
            throw new UserAlreadyExistsException("User already exists with Email: " + request.getEmail());

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setOrgId(request.getOrgId());
//        user.setOrganizationId(organizations);
        user.setStatus(request.getStatus());
        user.setRole(request.getRole());
        user = repository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public boolean emailExists(String email) {
        Optional<User> user = repository.findByEmail(email);
        return user.isPresent();
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        if (!emailExists(request.getEmail()))
            throw new DataNotFoundException("User not exist");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
                        request.getEmail(),
                        request.getPassword()
                )
        );
//        User user = repository.findByEmail(request.getUsername()).orElseThrow();
        User user = repository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

}
