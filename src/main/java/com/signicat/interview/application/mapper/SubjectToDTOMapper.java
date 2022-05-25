package com.signicat.interview.application.mapper;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.signicat.interview.adapters.data.entity.SubjectEntity;
import com.signicat.interview.domain.model.Subject;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubjectToDTOMapper implements Function<SubjectEntity, Subject> {

    private final UserGroupToDTOMapper userGroupToDTOMapper;

    @Override
    public Subject apply(SubjectEntity t) {

        return Subject.builder()
                .id(t.getId())
                .password(t.getPassword())
                .username(t.getUsername())
                .groups(Optional.ofNullable(t.getGroups())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(userGroupToDTOMapper::apply)
                        .collect(Collectors.toList()))
                .build();
    }
}
