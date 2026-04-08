package com.devnear.web.dto.client;

import com.devnear.web.domain.client.ClientProfile;
import com.devnear.web.domain.user.User;
import jakarta.validation.constraints.NotBlank; // 추가 권장
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClientProfileRequest {

    @NotBlank(message = "업체명은 필수입니다.")
    private String companyName;

    @NotBlank(message = "사업자번호는 필수입니다.")
    private String bn;

    @NotBlank(message = "대표자명은 필수입니다.")
    private String representativeName;

    private String introduction;
    private String homepageUrl;

    @NotBlank(message = "연락처는 필수입니다.")
    private String phoneNum;

    public ClientProfile toEntity(User user) {
        return ClientProfile.builder()
                .user(user)
                .companyName(companyName)
                .bn(bn)
                .representativeName(representativeName)
                .introduction(introduction)
                .homepageUrl(homepageUrl)
                .phoneNum(phoneNum)
                .build();
    }
}