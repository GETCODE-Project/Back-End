package com.getcode.exception.member;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

import static com.getcode.exception.common.ErrorCode.WRONG_VERIFIED_CODE_EXCEPTION;

public class WrongVerifiedCodeException extends BusinessException {
    public WrongVerifiedCodeException() {
        super(WRONG_VERIFIED_CODE_EXCEPTION);
    }
}
