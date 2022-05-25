package com.signicat.interview.adapters.rest.controller;

import com.signicat.interview.application.service.UserGroupService;
import com.signicat.interview.domain.exception.UserGroupNotFoundException;
import com.signicat.interview.domain.model.PayloadResponse;
import com.signicat.interview.domain.model.UserGroup;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class UserGroupController {

    private final UserGroupService userGroupService;

    @PostMapping("/save")
    public ResponseEntity<PayloadResponse> save(@RequestBody final UserGroup userGroup) {

        log.info("Received POST call in /api/groups/save. UserGroup -> {}", userGroup);
        try {
            final UserGroup savedUserGroup = userGroupService.saveUserGroup(userGroup);
            log.info("User group saved successfully.");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(PayloadResponse.builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("User group saved succesfully")
                            .additionalDetails(savedUserGroup)
                            .build());
        } catch (Exception e) {
            log.error("There was a problem saving the new user group. UserGroup -> {}", userGroup, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PayloadResponse.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("There was an error saving the new user group")
                            .build());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PayloadResponse> listById(@PathVariable("id") final long userGroupId) {

        try {
            log.info("Received GET call in /get/{}. UserGroupId -> {}", userGroupId, userGroupId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(PayloadResponse.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("The item request was found successfully")
                            .additionalDetails(userGroupService.retrieveUserGroupById(userGroupId))
                            .build());
        } catch (UserGroupNotFoundException e) {
            log.error("The user group requested was not found. UserGroupId -> {}", userGroupId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(PayloadResponse.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("The user group requested was not found")
                            .build());
        } catch (Exception e) {
            log.error("There was a problem retrieving the user group. UserGroupId -> {}. Exception -> {}", userGroupId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PayloadResponse.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("There was an error retrieving the user group")
                            .build());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<PayloadResponse> listAll() {

        try {
            log.info("Received GET call in /get/");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(PayloadResponse.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("The item request was found successfully")
                            .additionalDetails(userGroupService.retrieveAllUserGroups())
                            .build());
        } catch (Exception e) {
            log.error("There was a problem retrieving the user groups. Exception -> {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PayloadResponse.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("There was an error retrieving the user groups")
                            .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<PayloadResponse> update(@RequestBody final UserGroup userGroup) {

        log.info("Received PUT call to /update. UserGroup -> {}", userGroup);
        try {
            final UserGroup updatedUserGroup = userGroupService.updateUserGroup(userGroup);
            log.info("User group saved successfully.");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(PayloadResponse.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("User group saved succesfully")
                            .additionalDetails(updatedUserGroup)
                            .build());
        } catch (Exception e) {
            log.error("There was a problem updating the user group with id {}. UserGroup -> {}", userGroup.getId(), userGroup, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PayloadResponse.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("There was an error updating the user group with id " + userGroup.getId())
                            .build());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PayloadResponse> delete(@PathVariable("id") final long id) {

        log.info("Received DELETE call to /delete/{}", id);
        userGroupService.deleteUserGroup(id);
        return ResponseEntity.status(HttpStatus.OK)
                    .body(PayloadResponse.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("User group deleted succesfully")
                            .build());
    }
}
