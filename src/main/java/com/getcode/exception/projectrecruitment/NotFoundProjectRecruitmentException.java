package com.getcode.exception.projectrecruitment;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotFoundProjectRecruitmentException extends BusinessException {

    public NotFoundProjectRecruitmentException(){
        super(ErrorCode.NOT_FOUND_PROJECT_RECRUITMENT_EXCEPTION);
    }

}
