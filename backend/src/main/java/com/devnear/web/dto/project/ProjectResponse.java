package com.devnear.web.dto.project;

import com.devnear.web.domain.enums.ProjectStatus;
import com.devnear.web.domain.project.Project;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectResponse {
    private Long projectId;
    private String projectName;
    private String companyName;    // ClientProfile에서 가져옴
    private Integer budget;
    private LocalDate deadline;
    private String detail;
    private ProjectStatus status;
    private boolean online;
    private boolean offline;
    private LocalDateTime createdAt;

    // 엔티티를 응답 DTO로 변환하는 정적 팩토리 메서드
    public static ProjectResponse from(Project project) {
        return ProjectResponse.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .companyName(project.getClientProfile().getCompanyName()) // 연관관계 탐색
                .budget(project.getBudget())
                .deadline(project.getDeadline())
                .detail(project.getDetail())
                .status(project.getStatus())
                .online(project.isOnline())
                .offline(project.isOffline())
                .createdAt(project.getCreatedAt())
                .build();
    }
}