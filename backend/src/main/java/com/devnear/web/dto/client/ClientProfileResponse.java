package com.devnear.web.dto.client;

import com.devnear.web.domain.client.ClientProfile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientProfileResponse {
    private Long clientId;
    private String email;
    private String nickname;
    private String companyName;
    private String representativeName;
    private String introduction;
    private String verificationStatus;
    private Integer totalProjects;
    private Double rating;

    public static ClientProfileResponse from(ClientProfile clientProfile) {
        return ClientProfileResponse.builder()
                .clientId(clientProfile.getId())
                .email(clientProfile.getUser().getEmail()) // 연관된 User에서 가져옴
                .nickname(clientProfile.getUser().getNickname())
                .companyName(clientProfile.getCompanyName())
                .representativeName(clientProfile.getRepresentativeName())
                .introduction(clientProfile.getIntroduction())
                .verificationStatus(clientProfile.getVerificationStatus().name())
                .totalProjects(clientProfile.getTotalProjects())
                .rating(clientProfile.getRating().doubleValue()) // BigDecimal을 Double로 변환
                .build();
    }
}