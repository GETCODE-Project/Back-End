package com.getcode.exception.member;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotFoundMemberException extends BusinessException {
    public NotFoundMemberException() {
        super(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION);
    }
}
