package com.devnear.web.exception;

public class ProjectAccessDeniedException extends RuntimeException {
    public ProjectAccessDeniedException(String message) {
        super(message);
    }
}