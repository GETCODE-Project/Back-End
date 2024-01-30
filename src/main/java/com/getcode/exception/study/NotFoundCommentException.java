package com.getcode.exception.study;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotFoundCommentException extends BusinessException {
    public NotFoundCommentException() {
        super(ErrorCode.NOT_FOUND_COMMENT_EXCEPTION);
    }
}
