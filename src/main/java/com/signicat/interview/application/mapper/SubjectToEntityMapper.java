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
public class SubjectToEntityMapper implements Function<Subject, SubjectEntity> {

    private final UserGroupToEntityMapper userGroupToEntityMapper;

    @Override
    public SubjectEntity apply(Subject t) {

        return SubjectEntity.builder()
                .id(t.getId())
                .password(t.getPassword())
                .username(t.getUsername())
                .groups(Optional.ofNullable(t.getGroups())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(userGroupToEntityMapper::apply)
                        .collect(Collectors.toList()))
                .build();
    }
}