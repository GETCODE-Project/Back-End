package com.getcode.exception.community;

import com.getcode.exception.common.BusinessException;
import com.getcode.exception.common.ErrorCode;

public class NotFoundCommunityException extends BusinessException {
    public NotFoundCommunityException() {
        super(ErrorCode.NOT_FOUND_COMMUNITY_EXCEPTION);
    }
}
