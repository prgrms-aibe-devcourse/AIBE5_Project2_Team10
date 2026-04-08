package com.devnear.web.domain.client;

import com.devnear.web.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {

    // 1. 객체로 찾기
    Optional<ClientProfile> findByUser(User user);

    // 2. 유저의 ID(Long)만으로 바로 찾기 (Service에서 에러 났던 부분 해결)
    Optional<ClientProfile> findByUserId(Long userId);

    // 3. 존재 여부 확인
    boolean existsByUser(User user);

    // 4. 사업자 번호 중복 확인
    boolean existsByBn(String bn);
}