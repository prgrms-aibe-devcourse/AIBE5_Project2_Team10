package com.devnear.web.service.project;

import com.devnear.web.domain.client.ClientProfile;
import com.devnear.web.domain.client.ClientProfileRepository;
import com.devnear.web.domain.project.Project;
import com.devnear.web.domain.project.ProjectRepository;
import com.devnear.web.domain.user.User;
import com.devnear.web.domain.user.UserRepository;
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
    private final UserRepository userRepository;

    /**
     * 프로젝트 공고 생성
     */
    @Transactional
    public Long createProject(String email, ProjectRequest request) {
        ClientProfile clientProfile = findClientProfileByEmail(email);
        Project project = request.toEntity(clientProfile);
        return projectRepository.save(project).getId();
    }

    /**
     * 프로젝트 공고 수정
     */
    @Transactional
    public void updateProject(String email, Long projectId, ProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 공고를 찾을 수 없습니다.")); // 404

        // 권한 확인
        if (!project.getClientProfile().getUser().getEmail().equals(email)) {
            throw new ProjectAccessDeniedException("해당 공고를 수정할 권한이 없습니다."); // 403
        }

        project.update(request);
    }

    /**
     * 프로젝트 공고 삭제
     */
    @Transactional
    public void deleteProject(String email, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 공고를 찾을 수 없습니다.")); // 404

        if (!project.getClientProfile().getUser().getEmail().equals(email)) {
            throw new ProjectAccessDeniedException("해당 공고를 삭제할 권한이 없습니다."); // 403
        }

        projectRepository.delete(project);
    }
    /**
     * 전체 프로젝트 목록 조회 (페이징)
     */
    public Page<ProjectResponse> getProjectList(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(ProjectResponse::from);
    }

    /**
     * 내가 작성한 프로젝트 목록 조회
     */
    public Page<ProjectResponse> getMyProjectList(String email, Pageable pageable) {
        ClientProfile clientProfile = findClientProfileByEmail(email);
        return projectRepository.findAllByClientProfile(clientProfile, pageable)
                .map(ProjectResponse::from);
    }

    // --- 내부 헬퍼 메서드 (중복 제거 및 캡슐화) ---

    private ClientProfile findClientProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("클라이언트 프로필이 등록되지 않았습니다."));
    }

    private Project findProjectAndValidateOwner(String email, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("공고를 찾을 수 없습니다."));

        // 권한 확인: 프로젝트 작성자의 이메일과 로그인한 유저의 이메일 비교
        if (!project.getClientProfile().getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("해당 공고에 대한 권한이 없습니다.");
        }
        return project;
    }
}