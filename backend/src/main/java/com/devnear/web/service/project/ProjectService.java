package com.devnear.web.service.project;

import com.devnear.web.domain.client.ClientProfile;
import com.devnear.web.domain.client.ClientProfileRepository;
import com.devnear.web.domain.project.Project;
import com.devnear.web.domain.project.ProjectRepository;
import com.devnear.web.domain.user.User;
import com.devnear.web.dto.project.ProjectRequest;
import com.devnear.web.dto.project.ProjectResponse;
import com.devnear.web.exception.ProjectAccessDeniedException;
import com.devnear.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ClientProfileRepository clientProfileRepository;

    @Transactional
    public Long createProject(User user, ProjectRequest request) {
        ClientProfile clientProfile = findClientProfileByUser(user);

        // 비즈니스 로직 수준의 추가 검증 (선택 사항)
        if (request.isOffline() && (request.getLocation() == null || request.getLatitude() == null || request.getLongitude() == null)) {
            throw new IllegalArgumentException("오프라인 프로젝트는 장소 정보가 필수입니다.");
        }

        return projectRepository.save(request.toEntity(clientProfile)).getId();
    }

    @Transactional
    public void updateProject(User user, Long projectId, ProjectRequest request) {
        Project project = findProjectAndValidateOwner(user, projectId);
        project.update(request);
    }

    @Transactional
    public void deleteProject(User user, Long projectId) {
        projectRepository.delete(findProjectAndValidateOwner(user, projectId));
    }

    public Page<ProjectResponse> getProjectList(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(ProjectResponse::from);
    }

    public Page<ProjectResponse> getMyProjectList(User user, Pageable pageable) {
        ClientProfile clientProfile = findClientProfileByUser(user);
        return projectRepository.findAllByClientProfile(clientProfile, pageable)
                .map(ProjectResponse::from);
    }

    private ClientProfile findClientProfileByUser(User user) {
        return clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("클라이언트 프로필이 등록되지 않았습니다."));
    }

    private Project findProjectAndValidateOwner(User user, Long projectId) {
        Project project = projectRepository.findByIdWithClientProfile(projectId)  // 교체
                .orElseThrow(() -> new ResourceNotFoundException("해당 공고를 찾을 수 없습니다."));

        if (!project.getClientProfile().getUser().getId().equals(user.getId())) {
            throw new ProjectAccessDeniedException("해당 공고에 대한 권한이 없습니다.");
        }
        return project;
    }
}