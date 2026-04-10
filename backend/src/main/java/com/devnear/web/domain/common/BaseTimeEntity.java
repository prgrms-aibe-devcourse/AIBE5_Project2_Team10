package com.devnear.web.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * [보고] 모든 엔티티의 생성 및 수정 시간을 자동으로 관리하기 위한 공통 도메인.
 * 본 클래스를 상속받는 모든 엔티티는 아래 필드들을 공통으로 사용하게 됨.
 *
 * @MappedSuperclass: JPA 엔티티 클래스들이 해당 추상 클래스를 상속할 경우,
 *                  createdAt, updatedAt과 같은 필드들을 컬럼으로 인식하도록 함.
 * @EntityListeners(AuditingEntityListener.class): 엔티티의 생성, 수정 이벤트를 감지하여
 *                                                자동으로 시간을 기록하는 Auditing 기능을 활성화함.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    // [보고] 엔티티 생성 시점의 시간을 자동으로 기록함.
    // updatable = false: 해당 필드는 생성 이후 수정되지 않음을 명시함.
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // [보고] 엔티티 최종 수정 시점의 시간을 자동으로 기록함.
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
