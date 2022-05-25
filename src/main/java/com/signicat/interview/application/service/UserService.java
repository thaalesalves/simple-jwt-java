package com.signicat.interview.application.service;

import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

import com.signicat.interview.adapters.data.entity.SubjectEntity;
import com.signicat.interview.adapters.data.repository.SubjectRepository;
import com.signicat.interview.application.mapper.SubjectToDTOMapper;
import com.signicat.interview.application.mapper.SubjectToEntityMapper;
import com.signicat.interview.domain.exception.PasswordNotStrongEnoughException;
import com.signicat.interview.domain.model.Subject;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final SubjectToDTOMapper subjectToDTOMapper;
    private final SubjectToEntityMapper subjectToEntityMapper;
    private final SubjectRepository subjectRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Subject signUp(final Subject subject) {

        final Pattern passwordPattern = Pattern
                .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        if (!passwordPattern.matcher(subject.getPassword()).matches()) {
            throw new PasswordNotStrongEnoughException("The provided password is not strong enough.");
        }

        subject.setPassword(passwordEncoder.encode(subject.getPassword()));
        final SubjectEntity entity = subjectToEntityMapper.apply(subject);
        return subjectToDTOMapper.apply(subjectRepository.save(entity));
    }

    public Subject retrieveByUsername(String username) {

        return subjectToDTOMapper.apply(subjectRepository.findByUsername(username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return Optional.ofNullable(subjectRepository.findByUsername(username))
                .map(subject -> new User(subject.getUsername(), subject.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
