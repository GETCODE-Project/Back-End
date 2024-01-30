package com.getcode.exception.study;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotWishException extends BusinessException {
    public NotWishException() {
        super(ErrorCode.NOT_WISH_EXCEPTION);
    }
}
