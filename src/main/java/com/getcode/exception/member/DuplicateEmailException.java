package com.getcode.exception.member;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL_EXCEPTION);
    }
}
