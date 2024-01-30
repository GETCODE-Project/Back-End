package com.getcode.exception.member;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotVerifiedException extends BusinessException {
    public NotVerifiedException() {
        super(ErrorCode.NOT_VERIFIED_EXCEPTION);
    }
}
