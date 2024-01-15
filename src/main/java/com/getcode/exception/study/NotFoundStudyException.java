package com.getcode.exception.study;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotFoundStudyException extends BusinessException {
    public NotFoundStudyException() {
        super(ErrorCode.NOT_FOUND_STUDY_EXCEPTION);
    }
}
