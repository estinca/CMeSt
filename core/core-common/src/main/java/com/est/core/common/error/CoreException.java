package com.est.core.common.error;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CoreError error;

    public CoreException(CoreError error) {
        super(error.getKey());
        this.error = error;
    }

    public CoreException(CoreError error, Throwable cause) {
        super(error.getKey(), cause);
        this.error = error;
    }
}
