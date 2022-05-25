package com.signicat.interview.adapters.rest.controller;

import com.signicat.interview.application.service.UserService;
import com.signicat.interview.domain.exception.PasswordNotStrongEnoughException;
import com.signicat.interview.domain.model.PayloadResponse;
import com.signicat.interview.domain.model.Subject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<PayloadResponse> registerUser(@RequestBody final Subject newUser) {

        log.info("Received POST call to /register. Subject -> {}", newUser);
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(PayloadResponse.builder()
                            .message("Success")
                            .statusCode(200)
                            .additionalDetails(userService.signUp(newUser))
                            .build());
        } catch (PasswordNotStrongEnoughException e) {
            log.error("The password provided is not strong enough", e);
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(PayloadResponse.builder()
                            .message(e.getMessage())
                            .statusCode(HttpStatus.PRECONDITION_FAILED.hashCode())
                            .build());
        } catch (Exception e) {
            log.error("Error registering new subject", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PayloadResponse.builder()
                            .message(e.getMessage())
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.hashCode())
                            .build());
        }
    }
}
