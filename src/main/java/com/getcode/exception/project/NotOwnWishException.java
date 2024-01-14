package com.getcode.exception.project;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotOwnWishException extends BusinessException {
    public NotOwnWishException(){
        super(ErrorCode.NOT_WISH_EXCEPTION);
    }
}
