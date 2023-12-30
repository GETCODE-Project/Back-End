package com.getcode.exception.common;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    DUPLICATE_EMAIL_EXCEPTION(409, "이미 사용중인 이메일입니다.", BAD_REQUEST),
    NOT_VERIFIED_EXCEPTION(401, "아직 인증되지 않은 이메일입니다.", UNAUTHORIZED),
    NOT_FOUND_MEMBER_EXCEPTION(404, "사용자가 존재하지 않습니다.", NOT_FOUND);

    private final int status;
    private final String message;
    private final HttpStatus  httpStatus;
}
