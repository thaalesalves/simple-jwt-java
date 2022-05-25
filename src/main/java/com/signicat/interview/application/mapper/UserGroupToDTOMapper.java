package com.signicat.interview.application.mapper;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.signicat.interview.adapters.data.entity.UserGroupEntity;
import com.signicat.interview.domain.model.Subject;
import com.signicat.interview.domain.model.UserGroup;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserGroupToDTOMapper implements Function<UserGroupEntity, UserGroup> {

    @Override
    public UserGroup apply(UserGroupEntity t) {

        return UserGroup.builder()
                .name(t.getName())
                .id(t.getId())
                .subjects(Optional.ofNullable(t.getSubjects())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(s -> Subject.builder()
                                .id(s.getId())
                                .username(s.getUsername())
                                .password(s.getPassword())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
