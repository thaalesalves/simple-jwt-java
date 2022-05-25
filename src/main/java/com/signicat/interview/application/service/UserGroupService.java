package com.signicat.interview.application.service;

import java.util.List;
import java.util.stream.Collectors;

import com.signicat.interview.adapters.data.entity.UserGroupEntity;
import com.signicat.interview.adapters.data.repository.UserGroupRepository;
import com.signicat.interview.application.mapper.UserGroupToDTOMapper;
import com.signicat.interview.application.mapper.UserGroupToEntityMapper;
import com.signicat.interview.domain.exception.UserGroupNotFoundException;
import com.signicat.interview.domain.model.UserGroup;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserGroupService {

    private final UserGroupToEntityMapper userGroupToEntityMapper;
    private final UserGroupToDTOMapper userGroupToDTOMapper;
    private final UserGroupRepository userGroupRepository;

    public UserGroup saveUserGroup(UserGroup userGroup) {

        log.info("Saving new user group -> {}", userGroup);
        final UserGroupEntity entity = userGroupToEntityMapper.apply(userGroup);
        return userGroupToDTOMapper.apply(userGroupRepository.save(entity));
    }

    public UserGroup retrieveUserGroupById(long userGroupId) {

        log.info("Listing specific UserGroup record by id. Id -> {}", userGroupId);
        return userGroupToDTOMapper.apply(userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new UserGroupNotFoundException("The user group request could not be found")));
    }

    public List<UserGroup> retrieveAllUserGroups() {

        log.info("Listing all UserGroups in database");
        return userGroupRepository.findAll()
                .stream()
                .map(userGroupToDTOMapper::apply)
                .collect(Collectors.toList());
    }

    public UserGroup updateUserGroup(final UserGroup userGroup) {

        log.info("Updating user group with ID {}. UserGroup -> {}", userGroup.getId(), userGroup);
        final UserGroupEntity entity = userGroupToEntityMapper.apply(userGroup);
        return userGroupToDTOMapper.apply(userGroupRepository.save(entity));
    }

    public void deleteUserGroup(final long userGroupId) {

        log.info("Deleting user group with ID {}", userGroupId);
        userGroupRepository.deleteById(userGroupId);
    }
}
