package com.devnear.web.dto.project;

import com.devnear.web.domain.client.ClientProfile;
import com.devnear.web.domain.enums.ProjectStatus;
import com.devnear.web.domain.project.Project;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ProjectRequest {

    @NotBlank(message = "프로젝트 제목은 필수입니다.")
    private String projectName;

    @NotNull(message = "예산은 필수입니다.")
    @Min(value = 0, message = "예산은 0원 이상이어야 합니다.")
    private Integer budget;

    @NotNull(message = "마감 기한은 필수입니다.")
    @FutureOrPresent(message = "마감 기한은 오늘 이후여야 합니다.")
    private LocalDate deadline;

    private String detail;

    private boolean online;  // 원격(재택) 여부
    private boolean offline; // 상주 여부

    /**
     * 엔티티 변환 메서드
     */
    public Project toEntity(ClientProfile clientProfile) {
        // 비즈니스 검증: 근무 형태가 하나도 선택되지 않은 경우 예외 발생
        if (!online && !offline) {
            throw new IllegalArgumentException("원격(online) 또는 상주(offline) 근무 형태 중 최소 하나를 선택해야 합니다.");
        }

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