package com.devnear.web.dto.user;

import com.devnear.web.domain.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OnboardingRequest {
    private String nickname;
    private Role role;
}
