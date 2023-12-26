package com.getcode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationResultDto {
    private boolean result;
    public static EmailVerificationResultDto toDto(boolean result) {
        return new EmailVerificationResultDto(result);
    }
}
