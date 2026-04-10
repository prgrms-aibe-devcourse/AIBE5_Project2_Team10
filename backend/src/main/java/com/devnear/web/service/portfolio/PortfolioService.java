package com.devnear.web.service.portfolio;

import com.devnear.web.domain.portfolio.Portfolio;
import com.devnear.web.domain.portfolio.PortfolioImage;
import com.devnear.web.domain.portfolio.PortfolioRepository;
import com.devnear.web.domain.portfolio.PortfolioSkill;
import com.devnear.web.domain.skill.Skill;
import com.devnear.web.domain.skill.SkillRepository;
import com.devnear.web.domain.user.User;
import com.devnear.web.dto.portfolio.PortfolioRequest;
import com.devnear.web.dto.portfolio.PortfolioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final SkillRepository skillRepository;

    // [등록] POST /api/portfolios
    @Transactional
    public Long createPortfolio(User user, PortfolioRequest request) {
        // 1. 포트폴리오 엔티티 생성
        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .title(request.getTitle())
                .desc(request.getDesc())
                .thumbnailUrl(request.getThumbnailUrl())
                .build();

        // 2-1. 다중 이미지 연결 (Cascade 처리)
        for (String imageUrl : request.getPortfolioImages()) {
            PortfolioImage imageEntity = PortfolioImage.builder()
                    .portfolio(portfolio)
                    .imageUrl(imageUrl)
                    .build();
            portfolio.addPortfolioImage(imageEntity);
        }

        // 2-2. 요청받은 스킬 존재 여부 꼼꼼하게 검증 (유령 스킬 차단 방어 로직)
        List<Skill> selectedSkills = skillRepository.findAllById(request.getSkills());
        if (selectedSkills.size() != request.getSkills().size()) {
            List<Long> foundIds = selectedSkills.stream().map(Skill::getId).collect(Collectors.toList());
            List<Long> missingIds = request.getSkills().stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());
            throw new IllegalArgumentException("존재하지 않는 스킬 ID가 포함되어 있습니다: " + missingIds);
        }

        // 3. 브릿지 객체로 스킬 연결
        for (Skill skill : selectedSkills) {
            PortfolioSkill portfolioSkill = PortfolioSkill.builder()
                    .portfolio(portfolio)
                    .skill(skill)
                    .build();
            portfolio.addPortfolioSkill(portfolioSkill);
        }

        // 4. 저장 후 생성된 ID 반환
        return portfolioRepository.save(portfolio).getId();
    }

    // [조회] GET /api/portfolios (특정 유저의 목록 반환)
    public List<PortfolioResponse> getPortfoliosByUserId(Long userId) {
        List<Portfolio> portfolios = portfolioRepository.findByUserIdWithSkills(userId);
        
        return portfolios.stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }

    // [삭제] DELETE /api/portfolios/{id}
    @Transactional
    public void deletePortfolio(User user, Long portfolioId) {
        // 1. 엔티티 우선 조회 및 404 방어
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 포트폴리오를 찾을 수 없습니다. (NOT_FOUND)"));

        // 2. 남의 포트폴리오를 삭제하려고 하는지 권한 검증 (보안)
        if (!portfolio.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 포트폴리오만 삭제할 수 있습니다. (FORBIDDEN)");
        }

        // 3. 권한 문제없으면 삭제 실행 (Cascade 속성으로 인해 딸려있던 기술스택 데이터도 알아서 날아감)
        portfolioRepository.delete(portfolio);
    }
}
