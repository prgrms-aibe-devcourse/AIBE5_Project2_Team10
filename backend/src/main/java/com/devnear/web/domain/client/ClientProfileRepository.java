package com.devnear.web.domain.client;

import com.devnear.web.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {

    // 1. 객체로 찾기
    Optional<ClientProfile> findByUser(User user);

    @Query("select cp from ClientProfile cp join fetch cp.user where cp.user = :user")
    Optional<ClientProfile> findByUserWithUser(@Param("user") User user);

    // 3. 존재 여부 확인
    boolean existsByUser(User user);

    // 4. 사업자 번호 중복 확인
    boolean existsByBn(String bn);
}