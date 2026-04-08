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

    public static ClientProfileResponse from(ClientProfile profile) {
        double safeRating = (profile.getRating() != null)
                ? profile.getRating().doubleValue()
                : 0.0;

        return ClientProfileResponse.builder()
                .clientId(profile.getId())
                // 2. 이 부분에서 User 객체에 접근하므로 트랜잭션 유지가 필수입니다.
                .email(profile.getUser().getEmail())
                .nickname(profile.getUser().getNickname())
                .companyName(profile.getCompanyName())
                .representativeName(profile.getRepresentativeName())
                .introduction(profile.getIntroduction())
                // 3. Enum 타입은 .name()을 통해 문자열로 변환
                .verificationStatus(profile.getVerificationStatus().name())
                .totalProjects(profile.getTotalProjects())
                .rating(safeRating) // 위에서 계산한 안전한 값 사용
                .build();
    }
}