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
            @RequestAttribute("userId") Long userId,
            @RequestBody @Valid ProjectRequest request) {

        Long projectId = projectService.createProject(userId, request);
        return ResponseEntity.ok(projectId);
    }

    @Operation(summary = "프로젝트 공고 수정", description = "본인이 등록한 프로젝트 공고 내용을 수정합니다.")
    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectRequest request) {

        projectService.updateProject(userId, projectId, request);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "프로젝트 목록 조회(페이징)", description = "최신순으로 프로젝트 공고를 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getProjects(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ProjectResponse> responses = projectService.getProjectList(pageable);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "프로젝트 공고 삭제", description = "본인이 등록한 프로젝트 공고를 삭제합니다.")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long projectId) {

        projectService.deleteProject(userId, projectId);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
}