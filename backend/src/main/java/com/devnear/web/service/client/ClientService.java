package com.devnear.web.service.client;

import com.devnear.web.domain.client.ClientProfile;
import com.devnear.web.domain.client.ClientProfileRepository;
import com.devnear.web.domain.user.User;
import com.devnear.web.domain.user.UserRepository;
import com.devnear.web.dto.client.ClientProfileRequest;
import com.devnear.web.dto.client.ClientProfileResponse; // 추가
import com.devnear.web.exception.DuplicateProfileException;
import com.devnear.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

    private final ClientProfileRepository clientProfileRepository;

    @Transactional
    public Long registerProfile(User user, ClientProfileRequest request) {
        if (clientProfileRepository.existsByUser(user)) {
            throw new DuplicateProfileException("이미 프로필이 등록된 사용자입니다.");
        }
        if (clientProfileRepository.existsByBn(request.getBn())) {
            throw new DuplicateProfileException("이미 등록된 사업자 번호입니다.");
        }

        try {
            return clientProfileRepository.save(request.toEntity(user)).getId();
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateProfileException("이미 등록된 프로필 또는 사업자 번호입니다.");
        }
    }

    public ClientProfileResponse getMyProfile(User user) {
        ClientProfile profile = clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("클라이언트 프로필을 찾을 수 없습니다."));
        return ClientProfileResponse.from(profile);
    }

    @Transactional
    public void updateProfile(User user, ClientProfileRequest request) {
        ClientProfile profile = findProfileByUser(user);
        profile.update(request);
    }

    @Transactional
    public void deleteProfile(User user) {
        clientProfileRepository.delete(findProfileByUser(user));
    }

    private ClientProfile findProfileByUser(User user) {
        return clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("클라이언트 프로필을 찾을 수 없습니다."));
    }
}

//    @Transactional
//    public void updateLogo(String email, String logoUrl) {
//        ClientProfile profile = findProfileByEmail(email);
//        profile.updateLogo(logoUrl);
//    }