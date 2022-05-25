package com.signicat.interview.adapters.data.repository;

import java.util.List;

import com.signicat.interview.adapters.data.entity.UserGroupEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Long> {

    @Query("SELECT ug FROM UserGroupEntity ug JOIN SubjectEntity s WHERE s.id = :subjectId")
    List<UserGroupEntity> findAllBySubject(@Param("subjectId") final long subjectId);
}