package com.devnear.web.service.project;

import com.devnear.web.domain.client.ClientProfile;
import com.devnear.web.domain.client.ClientProfileRepository;
import com.devnear.web.domain.project.Project;
import com.devnear.web.domain.project.ProjectRepository;
import com.devnear.web.dto.project.ProjectRequest;
import com.devnear.web.dto.project.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ClientProfileRepository clientProfileRepository;

    @Transactional
    public Long createProject(Long userId, ProjectRequest request) {
        // 1. 현재 로그인한 유저의 클라이언트 프로필을 가져옴
        ClientProfile clientProfile = clientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("클라이언트 프로필 등록이 필요합니다."));

        // 2. DTO를 엔티티로 변환 (내부에서 online/offline 검증 수행)
        Project project = request.toEntity(clientProfile);

        // 3. 저장
        return projectRepository.save(project).getId();
    }

    @Transactional
    public void updateProject(Long userId, Long projectId, ProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고가 없습니다."));

        // 작성자 검증: 프로젝트 -> 클라이언트 -> 유저 ID 비교
        if (!project.getClientProfile().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        project.update(request); // Dirty Checking으로 자동 업데이트
    }

    @Transactional
    public void deleteProject(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고가 없습니다."));

        if (!project.getClientProfile().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        projectRepository.delete(project);
    }

    public Page<ProjectResponse> getProjectList(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(ProjectResponse::from); // Entity -> DTO 변환
    }
}