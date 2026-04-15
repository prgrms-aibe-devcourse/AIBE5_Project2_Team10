package com.devnear.web.dto.application;

import com.devnear.web.domain.application.ProjectApplication;
import com.devnear.web.domain.enums.ApplicationStatus;
import com.devnear.web.dto.skill.SkillResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [CLI] 클라이언트가 자신의 프로젝트 지원자를 조회할 때 사용하는 응답 DTO입니다.
 */
@Getter
@Builder
public class ApplicantResponse {

    private Long applicationId;
    private ApplicationStatus status;
    private Double matchingRate;
    private Integer bidPrice;
    private String message;
    private LocalDateTime appliedAt;

    private Long freelancerId;
    private String freelancerNickname;
    private String freelancerProfileImageUrl;
    private List<SkillResponse> freelancerSkills;

    public static ApplicantResponse from(ProjectApplication app) {
        List<SkillResponse> skills = app.getFreelancerProfile().getFreelancerSkills().stream()
                .map(fs -> SkillResponse.from(fs.getSkill()))
                .toList();

        return ApplicantResponse.builder()
                .applicationId(app.getId())
                .status(app.getStatus())
                .matchingRate(app.getMatchingRate())
                .bidPrice(app.getBidPrice())
                .message(app.getMessage())
                .appliedAt(app.getCreatedAt())
                .freelancerId(app.getFreelancerProfile().getId())
                .freelancerNickname(app.getFreelancerProfile().getUser().getNickname())
                .freelancerProfileImageUrl(app.getFreelancerProfile().getUser().getProfileImageUrl())
                .freelancerSkills(skills)
                .build();
    }
}

