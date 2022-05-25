package com.signicat.interview.domain.model;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subject {

    private long id;
    private String username;

    @JsonProperty(access = WRITE_ONLY)
    private String password;

    private List<UserGroup> groups;
}