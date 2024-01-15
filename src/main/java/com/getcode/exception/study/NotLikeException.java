package com.getcode.exception.study;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotLikeException extends BusinessException {
    public NotLikeException() {
        super(ErrorCode.NOT_LIKE_EXCEPTION);
    }
}
