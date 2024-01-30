package com.getcode.exception.project;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotMatchMemberException extends BusinessException {
    public NotMatchMemberException(){
        super(ErrorCode.MATCH_MEMBER_EXCEPTION);
    }
}
