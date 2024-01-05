package com.getcode.exception.study;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class MatchMemberException extends BusinessException {
    public MatchMemberException() {
        super(ErrorCode.MATCH_MEMBER_EXCEPTION);
    }
}
