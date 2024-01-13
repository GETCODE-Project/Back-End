package com.getcode.exception.project;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;


public class NotFoundProjectException extends BusinessException {

    public NotFoundProjectException(){super(ErrorCode.NOT_FOUND_PROJECT_EXCEPTION);}
}
