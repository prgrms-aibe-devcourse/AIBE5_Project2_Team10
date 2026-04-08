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
            @RequestAttribute("userId") Long userId,
            @RequestBody @Valid ClientProfileRequest request) {

        Long clientId = clientService.registerProfile(userId, request);
        return ResponseEntity.ok(clientId);
    }

    @Operation(summary = "클라이언트 프로필 조회", description = "현재 로그인한 유저의 클라이언트 프로필 상세 정보를 조회합니다.")
    @GetMapping("/profile")
    public ResponseEntity<ClientProfileResponse> getProfile(
            @RequestAttribute("userId") Long userId) {

        ClientProfileResponse response = clientService.getProfileResponse(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "기업 로고 업로드", description = "MultipartFile을 이용해 로고 이미지를 업로드합니다.")
    @PostMapping("/profile/logo")
    public ResponseEntity<String> uploadLogo(
            @RequestAttribute("userId") Long userId,
            @RequestParam("file") MultipartFile file) {

        // 1. 파일 저장 로직 (실무에선 S3 사용 권장)
        // 2. 저장된 URL 반환 (예: "https://s3.amazon.com/bucket/logo.jpg")
        String uploadedUrl = "temp_url_from_file_service";

        clientService.updateLogo(userId, uploadedUrl);
        return ResponseEntity.ok(uploadedUrl);
    }

    @Operation(summary = "클라이언트 프로필 수정", description = "로그인한 유저의 기업 정보를 수정합니다.")
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            @RequestAttribute("userId") Long userId,
            @RequestBody @Valid ClientProfileRequest request) {

        clientService.updateProfile(userId, request);
        return ResponseEntity.ok().build();
    }
}