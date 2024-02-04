package com.getcode.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationResultDto {

    private boolean verificationResult;

    public static EmailVerificationResultDto toDto(boolean result) {
        return new EmailVerificationResultDto(result);
    }
}
