package com.poolc.springproject.poolcreborn.controller;

import com.poolc.springproject.poolcreborn.payload.request.user.SignupRequest;
import com.poolc.springproject.poolcreborn.payload.request.user.LoginRequest;
import com.poolc.springproject.poolcreborn.repository.UserRepository;
import com.poolc.springproject.poolcreborn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.poolc.springproject.poolcreborn.util.Message.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(HttpServletRequest request, @Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity<>(USED_USERNAME, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>(USED_EMAIL, HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(signupRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(SUCCESSFUL_SIGNUP_USER);
    }

}
