package com.devnear.web.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectStatus {
    OPEN("모집중"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료"),
    CLOSED("마감");

    private final String description;
}