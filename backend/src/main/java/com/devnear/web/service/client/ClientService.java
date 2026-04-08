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

    @Transactional
    public Long registerProfile(String email, ClientProfileRequest request) {
        User user = findUserByEmail(email);

        if (clientProfileRepository.existsByUser(user)) {
            throw new IllegalStateException("이미 프로필이 등록된 사용자입니다.");
        }

        if (clientProfileRepository.existsByBn(request.getBn())) {
            throw new IllegalArgumentException("이미 등록된 사업자 번호입니다.");
        }

        ClientProfile profile = request.toEntity(user);
        return clientProfileRepository.save(profile).getId();
    }

    @Transactional(readOnly = true) // 트랜잭션을 열어 영속성 컨텍스트를 유지
    public ClientProfileResponse getMyProfile(String email) {
        User user = findUserByEmail(email);

        ClientProfile profile = clientProfileRepository.findByUserWithUser(user)
                .orElseThrow(() -> new IllegalArgumentException("클라이언트 프로필을 찾을 수 없습니다."));

        return ClientProfileResponse.from(profile);
    }

    @Transactional
    public void updateProfile(String email, ClientProfileRequest request) {
        ClientProfile profile = findProfileByEmail(email);
        profile.update(request);
    }

    @Transactional
    public void deleteProfile(String email) {
        ClientProfile profile = findProfileByEmail(email);
        clientProfileRepository.delete(profile);
    }

    //    @Transactional
    //    public void updateLogo(String email, String logoUrl) {
    //        ClientProfile profile = findProfileByEmail(email);
    //        profile.updateLogo(logoUrl);
    //    }

    // --- 공통 내부 헬퍼 메서드 ---

    // --- 내부 헬퍼 메서드 ---
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private ClientProfile findProfileByEmail(String email) {
        User user = findUserByEmail(email);
        return clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("클라이언트 프로필을 찾을 수 없습니다."));
    }
}
