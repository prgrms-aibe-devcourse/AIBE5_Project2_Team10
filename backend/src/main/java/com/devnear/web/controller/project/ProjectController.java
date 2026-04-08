package com.devnear.web.controller.project;

import com.devnear.web.dto.project.ProjectRequest;
import com.devnear.web.dto.project.ProjectResponse;
import com.devnear.web.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Project", description = "프로젝트 공고 관련 API")
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "프로젝트 공고 등록", description = "클라이언트가 새로운 프로젝트 공고를 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> createProject(
            @RequestAttribute("userEmail") String email,
            @RequestBody @Valid ProjectRequest request) {
        return ResponseEntity.ok(projectService.createProject(email, request));
    }

    @Operation(summary = "프로젝트 공고 수정", description = "본인이 등록한 프로젝트 공고 내용을 수정합니다.")
    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(
            @RequestAttribute("userEmail") String email,
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectRequest request) {
        projectService.updateProject(email, projectId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로젝트 공고 삭제", description = "본인이 등록한 프로젝트 공고를 삭제합니다.")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @RequestAttribute("userEmail") String email,
            @PathVariable Long projectId) {
        projectService.deleteProject(email, projectId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "전체 프로젝트 목록 조회", description = "최신순으로 프로젝트 공고를 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getProjectList(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(projectService.getProjectList(pageable));
    }

    @Operation(summary = "내 프로젝트 목록 조회", description = "로그인한 유저가 작성한 프로젝트 공고만 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<Page<ProjectResponse>> getMyProjects(
            @RequestAttribute("userEmail") String email,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(projectService.getMyProjectList(email, pageable));
    }
}