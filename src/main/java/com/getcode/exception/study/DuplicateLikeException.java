package com.getcode.exception.study;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class DuplicateLikeException extends BusinessException {
    public DuplicateLikeException() {
        super(ErrorCode.DUPLICATE_LIKE_EXCEPTION);
    }
}
