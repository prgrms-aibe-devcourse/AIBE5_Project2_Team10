package com.devnear.web.controller.client;

import com.devnear.web.dto.client.ClientProfileRequest;
import com.devnear.web.dto.client.ClientProfileResponse;
import com.devnear.web.service.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Client", description = "클라이언트(의뢰인) 프로필 관련 API")
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "클라이언트 프로필 등록", description = "로그인한 유저의 기업/의뢰인 정보를 등록합니다.")
    @PostMapping("/profile")
    public ResponseEntity<Long> registerProfile(
            @RequestAttribute("userEmail") String email,
            @RequestBody @Valid ClientProfileRequest request) {

        Long clientId = clientService.registerProfile(email, request);
        return ResponseEntity.ok(clientId);
    }

    @Operation(summary = "클라이언트 프로필 조회", description = "로그인한 유저 본인의 기업 프로필 정보를 가져옵니다.")
    @GetMapping("/profile")
    public ResponseEntity<ClientProfileResponse> getMyProfile(
            @RequestAttribute("userEmail") String email) {
        // 서비스의 메서드명과 일치시킴
        return ResponseEntity.ok(clientService.getMyProfile(email));
    }

    @Operation(summary = "클라이언트 프로필 수정", description = "로그인한 유저 본인의 기업 정보를 수정합니다.")
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            @RequestAttribute("userEmail") String email,
            @RequestBody @Valid ClientProfileRequest request) {

        clientService.updateProfile(email, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클라이언트 프로필 삭제", description = "로그인한 유저의 클라이언트 프로필 정보를 삭제합니다.")
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(
            @RequestAttribute("userEmail") String email) {

        clientService.deleteProfile(email);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

}