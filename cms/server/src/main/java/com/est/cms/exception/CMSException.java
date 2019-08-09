package com.est.cms.exception;

import com.est.core.common.error.CoreException;

public class CMSException extends CoreException {
	
	private static final long serialVersionUID = -6477229924809029994L;

	public CMSException(CMSError error) {
        super(error);
    }

    public CMSException(CMSError error, Throwable cause) {
        super(error, cause);
    }
}