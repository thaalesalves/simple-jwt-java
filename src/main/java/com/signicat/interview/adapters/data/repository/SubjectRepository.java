package com.signicat.interview.adapters.data.repository;

import com.signicat.interview.adapters.data.entity.SubjectEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {

    SubjectEntity findByUsername(String username);

    @Query("SELECT s FROM SubjectEntity s INNER JOIN UserGroupEntity ug WHERE s.username = :username")
    SubjectEntity findByUsernameFetchUserGroup(@Param("username") String username);
}
