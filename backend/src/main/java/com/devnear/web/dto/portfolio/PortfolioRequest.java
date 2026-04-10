package com.devnear.web.dto.portfolio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class PortfolioRequest {

    @NotBlank(message = "포트폴리오 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "상세 설명(desc)은 필수입니다.")
    private String desc; // 기획안의 desc

    private String thumbnailUrl; // 썸네일은 선택사항 (null 시 프론트 자체 기본이미지 렌더링)

    @NotNull(message = "포트폴리오 다중 이미지(portfolioImages) 필드는 필수입니다.")
    @NotEmpty(message = "포트폴리오 상세 이미지는 1장 이상 등록해야 합니다.")
    private List<String> portfolioImages;

    @NotNull(message = "스킬 목록(skills) 파라미터는 필수입니다.")
    @NotEmpty(message = "최소 1개 이상의 스킬을 등록해야 합니다.")
    private List<Long> skills;
}
