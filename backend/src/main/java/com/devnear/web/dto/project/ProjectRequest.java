package com.devnear.web.dto.project;

import com.devnear.web.domain.client.ClientProfile;
import com.devnear.web.domain.enums.ProjectStatus;
import com.devnear.web.domain.project.Project;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ProjectRequest {
    @NotBlank(message = "프로젝트 명은 필수입니다.")
    @Size(max = 100)
    private String projectName;

    @NotNull(message = "예산은 필수입니다.")
    private Integer budget;

    @NotNull(message = "마감기한은 필수입니다.")
    private LocalDate deadline;

    private String detail;
    private boolean online;
    private boolean offline;

    public Project toEntity(ClientProfile clientProfile) {
        return Project.builder()
                .clientProfile(clientProfile)
                .projectName(projectName)
                .budget(budget)
                .deadline(deadline)
                .detail(detail)
                .online(online)
                .offline(offline)
                .build();
    }
}