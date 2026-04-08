package com.devnear.web.service.client;

import com.devnear.web.domain.client.ClientProfile;
import com.devnear.web.domain.client.ClientProfileRepository;
import com.devnear.web.domain.user.User;
import com.devnear.web.domain.user.UserRepository;
import com.devnear.web.dto.client.ClientProfileRequest;
import com.devnear.web.dto.client.ClientProfileResponse; // 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;

    /**
     * 클라이언트 프로필 등록
     */
    @Transactional
    public Long registerProfile(Long userId, ClientProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (clientProfileRepository.existsByUser(user)) {
            throw new IllegalArgumentException("이미 등록된 클라이언트 프로필이 존재합니다.");
        }

        if (clientProfileRepository.existsByBn(request.getBn())) {
            throw new IllegalArgumentException("이미 등록된 사업자 번호입니다.");
        }

        ClientProfile clientProfile = request.toEntity(user);
        return clientProfileRepository.save(clientProfile).getId();
    }

    /**
     * 클라이언트 프로필 상세 조회
     * 컨트롤러에서 호출하여 화면에 바로 내려줄 때 사용합니다.
     */
    public ClientProfileResponse getProfileResponse(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        ClientProfile profile = clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("클라이언트 프로필을 찾을 수 없습니다."));

        return ClientProfileResponse.from(profile); // 정적 팩토리 메서드 활용
    }

    /**
     * 내부 로직용 엔티티 조회
     */
    public ClientProfile getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("클라이언트 프로필을 찾을 수 없습니다."));
    }

    @Transactional
    public void updateLogo(Long userId, String logoUrl) {
        ClientProfile profile = clientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다."));
        profile.updateLogo(logoUrl);
    }

    @Transactional
    public void updateProfile(Long userId, ClientProfileRequest request) {
        // 1. 해당 유저의 프로필이 있는지 확인
        ClientProfile profile = clientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("수정할 프로필이 존재하지 않습니다."));

        // 2. 사업자 번호가 변경되었다면 중복 체크 (선택 사항)
        if (!profile.getBn().equals(request.getBn()) && clientProfileRepository.existsByBn(request.getBn())) {
            throw new IllegalArgumentException("이미 사용 중인 사업자 번호입니다.");
        }

        // 3. 데이터 업데이트
        profile.update(request);
    }
}