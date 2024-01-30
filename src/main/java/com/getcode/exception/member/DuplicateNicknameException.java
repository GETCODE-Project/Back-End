package com.getcode.exception.member;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class DuplicateNicknameException extends BusinessException {
    public DuplicateNicknameException() {
        super(ErrorCode.DUPLICATE_NICKNAME_EXCEPTION);
    }
}
