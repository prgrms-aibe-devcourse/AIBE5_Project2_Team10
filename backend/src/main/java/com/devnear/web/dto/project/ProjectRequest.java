package com.devnear.web.dto.project;

import com.devnear.web.domain.client.ClientProfile;
import com.devnear.web.domain.enums.ProjectStatus;
import com.devnear.web.domain.project.Project;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@Getter
@NoArgsConstructor
public class ProjectRequest {
    @NotBlank(message = "프로젝트 명은 필수입니다.")
    @Size(max = 100)
    private String projectName;

    @NotNull(message = "예산은 필수입니다.")
    @Positive(message = "예산은 0보다 커야 합니다.")  // 추가
    private Integer budget;

    @NotNull(message = "마감기한은 필수입니다.")
    @Future(message = "마감기한은 오늘 이후여야 합니다.")  // 추가
    private LocalDate deadline;

    private String detail;
    private boolean online;
    private boolean offline;

    @Size(max = 500, message = "주소는 500자를 초과할 수 없습니다.")
    private String location;

    private Double latitude;

    private Double longitude;

    @AssertTrue(message = "오프라인 프로젝트는 주소 정보가 필수입니다.")
    private boolean isOfflineAddressValid() {
        if (!offline) {
            return true;
        }
        return location != null && ! location.trim().isEmpty()
                && latitude != null && longitude != null;
    }

    public Project toEntity(ClientProfile clientProfile) {
        return Project.builder()
                .clientProfile(clientProfile)
                .projectName(projectName)
                .budget(budget)
                .deadline(deadline)
                .detail(detail)
                .online(online)
                .offline(offline)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}