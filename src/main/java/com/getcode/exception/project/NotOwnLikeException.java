package com.getcode.exception.project;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotOwnLikeException extends BusinessException {
    public NotOwnLikeException(){
        super(ErrorCode.NOT_LIKE_EXCEPTION);}
}
