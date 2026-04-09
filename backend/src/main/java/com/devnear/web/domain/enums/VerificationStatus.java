package com.devnear.web.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VerificationStatus {
    PENDING("승인 대기"),
    VERIFIED("승인 완료"),
    REJECTED("승인 거절");

    private final String description;
}