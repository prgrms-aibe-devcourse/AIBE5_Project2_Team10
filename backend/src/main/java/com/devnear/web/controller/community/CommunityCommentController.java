package com.devnear.web.controller.community;

import com.devnear.web.dto.community.CommunityCommentCreateRequest;
import com.devnear.web.dto.community.CommunityCommentResponse;
import com.devnear.web.dto.community.CommunityCommentUpdateRequest;
import com.devnear.web.dto.community.CommunitySuccessResponse;
import com.devnear.web.service.community.CommunityCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommunityCommentController {

    private final CommunityCommentService communityCommentService;

    @PostMapping("/api/community/comments")
    public ResponseEntity<Long> create(@RequestBody CommunityCommentCreateRequest request,
                                       Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communityCommentService.create(request, userId));
    }

    @GetMapping("/api/community/posts/{postId}/comments")
    public ResponseEntity<List<CommunityCommentResponse>> findByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(communityCommentService.findByPostId(postId));
    }

    @PutMapping("/api/community/comments/{commentId}")
    public ResponseEntity<CommunitySuccessResponse> update(@PathVariable Long commentId,
                                                           @RequestBody CommunityCommentUpdateRequest request,
                                                           Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        communityCommentService.update(commentId, request, userId);
        return ResponseEntity.ok(new CommunitySuccessResponse(true));
    }

    @DeleteMapping("/api/community/comments/{commentId}")
    public ResponseEntity<CommunitySuccessResponse> delete(@PathVariable Long commentId,
                                                           Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        communityCommentService.delete(commentId, userId);
        return ResponseEntity.ok(new CommunitySuccessResponse(true));
    }
}