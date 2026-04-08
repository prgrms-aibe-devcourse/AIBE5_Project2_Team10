package com.devnear.web.domain.project;

import com.devnear.web.domain.client.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // 특정 클라이언트가 등록한 모든 프로젝트 조회
    List<Project> findAllByClientProfile(ClientProfile clientProfile);
}