package com.devnear.web.domain.project;

import com.devnear.web.domain.client.ClientProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // 수정/삭제 시 권한 확인용
    @Query("SELECT p FROM Project p JOIN FETCH p.clientProfile cp JOIN FETCH cp.user WHERE p.id = :projectId")
    Optional<Project> findByIdWithClientProfile(@Param("projectId") Long projectId);

    // 페이징 목록 조회 시 N+1 문제 해결
    @Override
    @EntityGraph(attributePaths = {"clientProfile", "clientProfile.user"})
    Page<Project> findAll(Pageable pageable);

    // 특정 클라이언트의 공고 목록 조회
    @EntityGraph(attributePaths = {"clientProfile", "clientProfile.user"})
    Page<Project> findAllByClientProfile(ClientProfile clientProfile, Pageable pageable);
}